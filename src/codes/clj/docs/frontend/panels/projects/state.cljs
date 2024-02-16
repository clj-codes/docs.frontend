(ns codes.clj.docs.frontend.panels.projects.state
  (:require [codes.clj.docs.frontend.infra.http :as http]
            [codes.clj.docs.frontend.panels.projects.adapters :as adapters]
            [town.lilac.flex :as flex]
            [town.lilac.flex.promise :as flex.promise]))

(def document-projects-fetch
  (flex.promise/resource
   #(-> (http/request! {:path "document/projects/" :method :get})
        (.then (fn [response]
                 (-> response
                     :body
                     adapters/projects->groups)))
        (.catch (fn [error]
                  (js/console.error error)
                  error)))))

(def document-projects-response
  (flex/signal {:state @(:state document-projects-fetch)
                :value @(:value document-projects-fetch)
                :error @(:error document-projects-fetch)
                :loading? @(:loading? document-projects-fetch)}))
