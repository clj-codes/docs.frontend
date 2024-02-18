(ns codes.clj.docs.frontend.infra.routes.state
  (:require [reitit.frontend.controllers :as rfc]
            [town.lilac.flex :as flex]))

(def routes-db (flex/source {:current-route nil}))

(defn navigated [new-match]
  (let [old-match (:current-route @routes-db)
        controllers (rfc/apply-controllers (:controllers old-match) new-match)]

    ; if hasn't a anchor link in the new navigation scroll back to top
    (when-not (:fragment new-match)
      (.scrollTo js/window 0 0))

    (routes-db assoc :current-route (assoc new-match :controllers controllers))))
