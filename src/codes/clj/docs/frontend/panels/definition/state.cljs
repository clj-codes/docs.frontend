(ns codes.clj.docs.frontend.panels.definition.state
  (:refer-clojure :exclude [namespace])
  (:require [codes.clj.docs.frontend.adapters.url :refer [safe-href->url-encoded]]
            [codes.clj.docs.frontend.infra.http :as http]
            [town.lilac.flex :as flex]
            [town.lilac.flex.promise :as flex.promise]))

(def definition-docs-fetch
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

(def definition-docs-results
  (flex/signal {:state @(:state definition-docs-fetch)
                :value @(:value definition-docs-fetch)
                :error @(:error definition-docs-fetch)
                :loading? @(:loading? definition-docs-fetch)}))

(def definition-social-results (flex/source {:error nil
                                             :loading? false
                                             :value nil}))

(def definition-social-fetch
  (flex.promise/resource
   (fn [organization project namespace definition index]
     (definition-social-results assoc :loading? true)
     (-> (http/request! {:path (safe-href->url-encoded
                                (str "social/definition/"
                                     organization "/"
                                     project "/"
                                     namespace "/"
                                     definition "/"
                                     index))
                         :method :get})
         (.then (fn [response]
                  (definition-social-results {:error nil
                                              :loading? false
                                              :value (:body response)})))
         (.catch (fn [error]
                   (js/console.error error)
                   (definition-social-results {:error error
                                               :loading? false
                                               :value nil})
                   (throw error)))))))
