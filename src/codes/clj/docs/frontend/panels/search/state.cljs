(ns codes.clj.docs.frontend.panels.search.state
  (:require [codes.clj.docs.frontend.infra.http :as http]
            [town.lilac.flex :as flex]
            [town.lilac.flex.promise :as flex.promise]))

(def search-fetch
  (flex.promise/resource
   (fn [query top]
     (-> (http/request! {:method :get
                         :path "document/search/"
                         :query-params {:q query :top top}})
         (.then (fn [response]
                  (-> response
                      :body)))
         (.catch (fn [error]
                   (js/console.error error)
                   (throw error)))))))

(def search-response
  (flex/signal {:state @(:state search-fetch)
                :value @(:value search-fetch)
                :error @(:error search-fetch)
                :loading? @(:loading? search-fetch)}))
