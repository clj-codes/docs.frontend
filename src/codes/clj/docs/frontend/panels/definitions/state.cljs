(ns codes.clj.docs.frontend.panels.definitions.state
  (:refer-clojure :exclude [namespace])
  (:require [codes.clj.docs.frontend.infra.http :as http]
            [town.lilac.flex :as flex]
            [town.lilac.flex.promise :as flex.promise]))

(def definitions-fetch
  (flex.promise/resource
   (fn [organization project namespace]
     (-> (http/request! {:path (str "document/definitions/" organization "/" project "/" namespace)
                         :method :get})
         (.then (fn [response]
                  (-> response
                      :body)))
         (.catch (fn [error]
                   (js/console.error error)
                   error))))))

(def definitions-response
  (flex/signal {:state @(:state definitions-fetch)
                :value @(:value definitions-fetch)
                :error @(:error definitions-fetch)
                :loading? @(:loading? definitions-fetch)}))
