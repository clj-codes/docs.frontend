(ns codes.clj.docs.frontend.panels.author.state
  (:require [codes.clj.docs.frontend.infra.http :as http]
            [town.lilac.flex :as flex]
            [town.lilac.flex.promise :as flex.promise]))

(def author-fetch
  (flex.promise/resource
   (fn [login source]
     (-> (http/request! {:path (str "social/author/" login "/" source)
                         :method :get})
         (.then (fn [response]
                  (-> response
                      :body)))
         (.catch (fn [error]
                   (js/console.error error)
                   (throw error)))))))

(def author-response
  (flex/signal {:state @(:state author-fetch)
                :value @(:value author-fetch)
                :error @(:error author-fetch)
                :loading? @(:loading? author-fetch)}))
