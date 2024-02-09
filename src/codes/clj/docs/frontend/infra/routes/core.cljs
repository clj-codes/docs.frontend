(ns codes.clj.docs.frontend.infra.routes.core
  (:require [codes.clj.docs.frontend.infra.routes.state :refer [navigated]]
            [codes.clj.docs.frontend.routes :refer [routes]]
            [reitit.coercion.malli :as rcm]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]))

(defn on-navigate [new-match]
  (when new-match
    (navigated new-match)))

(def router
  (rf/router routes {:data {:coercion rcm/coercion}}))

(defn init-routes! []
  (rfe/start!
   router
   on-navigate
   {:use-fragment false}))
