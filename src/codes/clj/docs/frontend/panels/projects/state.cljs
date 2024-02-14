(ns codes.clj.docs.frontend.panels.projects.state
  (:require [codes.clj.docs.frontend.infra.flex.resource :as flex.promise]
            [codes.clj.docs.frontend.infra.http :as http]
            [codes.clj.docs.frontend.panels.projects.adapters :as adapters]
            [town.lilac.flex :as flex]))

(def document-projects-request
  (flex.promise/resource
   #(-> (http/request! {:path "document/projects/" :method :get})
        (.then (fn [response]
                 (-> response
                     :body
                     adapters/projects->groups)))
        (.catch (fn [error]
                  (js/console.error error))))))

(def document-projects-response
  (flex/signal {:state @(:state document-projects-request)
                :value @(:value document-projects-request)
                :error @(:error document-projects-request)
                :loading? @(:loading? document-projects-request)}))
