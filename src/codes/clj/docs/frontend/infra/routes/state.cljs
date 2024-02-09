(ns codes.clj.docs.frontend.infra.routes.state
  (:require [reitit.frontend.controllers :as rfc]
            [town.lilac.flex :as flex]))

(def routes-db (flex/source {:current-route nil}))

(defn navigated [new-match]
  (let [old-match (:current-route @routes-db)
        controllers (rfc/apply-controllers (:controllers old-match) new-match)]
    (routes-db assoc :current-route (assoc new-match :controllers controllers))))
