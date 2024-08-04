(ns codes.clj.docs.frontend.panels.dashboards.state
  (:require [codes.clj.docs.frontend.infra.http :as http]
            [town.lilac.flex :as flex]
            [town.lilac.flex.promise :as flex.promise]))

(def latest-interactions-fetch
  (flex.promise/resource
   #(-> (http/request! {:path "social/query/latest-interactions"
                        :method :get
                        :query-params {:limit 15}})
        (.then (fn [response]
                 (-> response
                     :body
                     (subvec 0 10))))
        (.catch (fn [error]
                  (js/console.error error)
                  (throw error))))))

(def latest-interactions-response
  (flex/signal {:state @(:state latest-interactions-fetch)
                :value @(:value latest-interactions-fetch)
                :error @(:error latest-interactions-fetch)
                :loading? @(:loading? latest-interactions-fetch)}))

(def top-authors-fetch
  (flex.promise/resource
   #(-> (http/request! {:path "social/query/top-authors"
                        :method :get
                        :query-params {:limit 100}})
        (.then (fn [response]
                 (:body response)))
        (.catch (fn [error]
                  (js/console.error error)
                  (throw error))))))

(def top-authors-response
  (flex/signal {:state @(:state top-authors-fetch)
                :value @(:value top-authors-fetch)
                :error @(:error top-authors-fetch)
                :loading? @(:loading? top-authors-fetch)}))

