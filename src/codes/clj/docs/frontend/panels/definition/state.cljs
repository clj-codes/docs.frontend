(ns codes.clj.docs.frontend.panels.definition.state
  (:refer-clojure :exclude [namespace])
  (:require [codes.clj.docs.frontend.components.adapters :refer [safe-href->url-encoded]]
            [codes.clj.docs.frontend.infra.http :as http]
            [town.lilac.flex :as flex]
            [town.lilac.flex.promise :as flex.promise]))

(def definition-fetch
  "Some macros and defs have the same namespace/name
   but are located in different files eg helix.core/$ (clj/cljs)
   so we generate an numeric index for those."
  (flex.promise/resource
   (fn [organization project namespace definition index]
     (-> (http/request! {:path (safe-href->url-encoded
                                (str "document/definition/"
                                     organization "/"
                                     project "/"
                                     namespace "/"
                                     definition "/"
                                     index))
                         :method :get})
         (.then (fn [response]
                  (-> response
                      :body)))
         (.catch (fn [error]
                   (js/console.error error)
                   (throw error)))))))

(def definition-response
  (flex/signal {:state @(:state definition-fetch)
                :value @(:value definition-fetch)
                :error @(:error definition-fetch)
                :loading? @(:loading? definition-fetch)}))
