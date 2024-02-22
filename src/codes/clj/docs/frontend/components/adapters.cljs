(ns codes.clj.docs.frontend.components.adapters
  (:require [clojure.string :as str]))

;; TODO tests
(defn href->safe-href [href]
  (when href
    (cond
      (re-find #"_..$" href) ".."
      (re-find #"_.$" href) "."
      (re-find #"_fs$" href) "/"
      :else (-> href
                (str/replace #"_bs" "\\\\")
                (str/replace #"_q" "?")))))

;; TODO tests
(defn safe-href->href [visual-encoded]
  (when visual-encoded
    (cond
      (re-find #"\.\.$" visual-encoded) "_.."
      (re-find #"\.$" visual-encoded) "_."
      (re-find #"/$" visual-encoded) "_fs"
      :else (-> visual-encoded
                (str/replace #"\\" "_bs")
                (str/replace #"\?" "_q")))))

;; TODO tests
(defn safe-href->url-encoded [visual-encoded]
  (when visual-encoded
    (-> visual-encoded
        href->safe-href
        js/encodeURIComponent)))
