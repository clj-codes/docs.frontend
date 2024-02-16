(ns codes.clj.docs.frontend.panels.namespaces.state
  (:require [codes.clj.docs.frontend.infra.http :as http]
            [town.lilac.flex :as flex]
            [town.lilac.flex.promise :as flex.promise]))

(def namespaces-fetch
  (flex.promise/resource
   (fn [organization project]
     (-> (http/request! {:path (str "document/namespaces/" organization "/" project)
                         :method :get})
         (.then (fn [response]
                  (-> response
                      :body)))
         (.catch (fn [error]
                   (js/console.error error)
                   error))))))

(def namespaces-response
  (flex/signal {:state @(:state namespaces-fetch)
                :value @(:value namespaces-fetch)
                :error @(:error namespaces-fetch)
                :loading? @(:loading? namespaces-fetch)}))
