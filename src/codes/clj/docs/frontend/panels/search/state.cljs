(ns codes.clj.docs.frontend.panels.search.state
  (:require [codes.clj.docs.frontend.infra.http :as http]
            [town.lilac.flex :as flex]
            [town.lilac.flex.promise :as flex.promise]))

(def spotlight-results (flex/source {:error nil
                                     :loading? false
                                     :value nil}))

(def page-results (flex/source {:error nil
                                :loading? false
                                :value nil}))

(def search-fetch
  (flex.promise/resource
   (fn [result-source query top]
     (result-source assoc :loading? true)
     (-> (http/request! {:method :get
                         :path "document/search/"
                         :query-params {:q query :top top}})
         (.then (fn [response]
                  (result-source {:error nil
                                  :loading? false
                                  :value (:body response)})))
         (.catch (fn [error]
                   (js/console.error error)
                   (result-source {:error error
                                   :loading? false
                                   :value nil})
                   (throw error)))))))
