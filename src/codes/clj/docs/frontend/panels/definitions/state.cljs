(ns codes.clj.docs.frontend.panels.definitions.state
  (:refer-clojure :exclude [namespace])
  (:require [codes.clj.docs.frontend.components.adapters :refer [safe-href->url-encoded]]
            [codes.clj.docs.frontend.infra.http :as http]
            [town.lilac.flex :as flex]
            [town.lilac.flex.promise :as flex.promise]))

(def definitions-fetch
  (flex.promise/resource
   (fn [organization project namespace]
     (-> (http/request! {:path (->> (str organization "/" project "/" namespace)
                                    safe-href->url-encoded
                                    (str "document/definitions/"))
                         :method :get})
         (.then (fn [response]
                  (-> response
                      :body)))
         (.catch (fn [error]
                   (js/console.error error)
                   (throw error)))))))

(def definitions-response
  (flex/signal {:state @(:state definitions-fetch)
                :value @(:value definitions-fetch)
                :error @(:error definitions-fetch)
                :loading? @(:loading? definitions-fetch)}))
