(ns codes.clj.docs.frontend.infra.cookie
  (:require ["js-cookie" :as cookie]
            [cljs.reader :as reader]))

(defn set-cookie
  ([name value]
   (cookie/set name (str value) #js {:sameSite "strict"}))
  ([name value expires]
   (cookie/set name (str value) #js {:sameSite "strict"
                                     :expires expires})))

(defn get-cookie
  [name]
  (cookie/get name))

(defn get-edn-cookie
  [name]
  (try
    (-> (get-cookie name)
        reader/read-string)
    (catch :default _ex
      nil)))

(defn remove-cookie
  [name]
  (cookie/remove name))
