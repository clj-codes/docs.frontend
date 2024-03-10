(ns codes.clj.docs.frontend.infra.routes.core
  (:require [codes.clj.docs.frontend.infra.routes.state :as routes.state]
            [codes.clj.docs.frontend.routes :refer [routes]]
            [reitit.coercion.malli :as rcm]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]))

(defn on-navigate [new-match]
  (when new-match
    (routes.state/navigated new-match)))

(defn init-routes! []
  (let [router (rf/router routes {:data {:coercion rcm/coercion}})]
    (routes.state/routes-db assoc :router router)
    (rfe/start! router on-navigate {:use-fragment false})))
