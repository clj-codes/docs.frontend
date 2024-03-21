  (ns codes.clj.docs.frontend.dev.shadow.hooks
    (:require [clojure.java.io :as io]
              [clojure.string :as str]
              [shadow.build :as build]
              [shadow.cljs.util :as util]))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn hashed-files
  {:shadow.build/stage :flush}
  [{::build/keys [mode] :as build-state} files]
  (doall
   (assoc build-state ::hashed-files
          (for [old-file-full-path files]
            (if (= :dev mode)
              {:old old-file-full-path
               :new old-file-full-path}

              (let [contents (slurp old-file-full-path)
                    old-file (io/file old-file-full-path)
                    old-file-name (.getName old-file)
                    old-file-path (.getParentFile old-file)
                    new-file-name (str (util/md5hex contents) "." old-file-name)
                    new-file-full-path (str old-file-path "/" new-file-name)
                    new-file (io/file new-file-full-path)]
                (.renameTo old-file new-file)
                {:old old-file-full-path
                 :new new-file-full-path}))))))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn replace-hashed-files
  {:shadow.build/stage :flush}
  [{::build/keys [mode] :as build-state} source target]
  (let [source-file (io/file source)
        target-file (io/file target)]
    (cond
      (not (.exists source-file))
      (do (util/log build-state {:type ::source-does-not-exist :source source})
          build-state)

      ;; in dev mode we don't need to copy more than once
      (and (= :dev mode)
           (= (.lastModified source-file) (::source-last-mod build-state))
           (.exists target-file))
      build-state

      ;; only replace files with the hashed name if release
      (= :release mode)
      (let [html (slurp source-file)
            new-html (reduce
                      (fn [accum {:keys [old new]}]
                        (let [old-file (io/file old)
                              new-file (io/file new)]
                          (str/replace accum (.getName old-file) (.getName new-file))))
                      html
                      (::hashed-files build-state))]

        (io/make-parents target-file)
        (spit target-file new-html)
        (assoc build-state ::source-last-mod (.lastModified source-file)))

      :else
      (let [html (slurp source-file)]
        (io/make-parents target-file)
        (spit target-file html)
        (assoc build-state ::source-last-mod (.lastModified source-file))))))
