(ns codes.clj.docs.frontend.infra.cookie
  (:require ["js-cookie$default" :as cookie]
            [cljs.reader :as reader]))

(defn set-cookie
  ([name value]
   (.set cookie name (str value) #js {:sameSite "strict"}))
  ([name value expires]
   (.set cookie name (str value) #js {:sameSite "strict"
                                      :expires expires})))

(defn get-cookie
  [name]
  (.get cookie name))

(defn get-edn-cookie
  [name]
  (try
    (-> (get-cookie name)
        reader/read-string)
    (catch :default _ex
      nil)))

(defn remove-cookie
  [name]
  (.remove cookie name))
