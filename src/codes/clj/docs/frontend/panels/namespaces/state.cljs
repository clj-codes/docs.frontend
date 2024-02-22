(ns codes.clj.docs.frontend.panels.namespaces.state
  (:require [codes.clj.docs.frontend.components.adapters :refer [safe-href->url-encoded]]
            [codes.clj.docs.frontend.infra.http :as http]
            [town.lilac.flex :as flex]
            [town.lilac.flex.promise :as flex.promise]))

(def namespaces-fetch
  (flex.promise/resource
   (fn [organization project]
     (-> (http/request! {:path (safe-href->url-encoded
                                (str "document/namespaces/"
                                     organization "/"
                                     project))
                         :method :get})
         (.then (fn [response]
                  (-> response
                      :body)))
         (.catch (fn [error]
                   (js/console.error error)
                   (throw error)))))))

(def namespaces-response
  (flex/signal {:state @(:state namespaces-fetch)
                :value @(:value namespaces-fetch)
                :error @(:error namespaces-fetch)
                :loading? @(:loading? namespaces-fetch)}))
