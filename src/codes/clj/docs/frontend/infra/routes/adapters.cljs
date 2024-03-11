(ns codes.clj.docs.frontend.infra.routes.adapters
  (:require [reitit.frontend :as rf]))

(defn href->route [href router]
  (let [match  (rf/match-by-path router href)
        route  (-> match :data :name)
        {:keys [path-params query-params]} match]
    (when match
      {:route route
       :path-params path-params
       :query-params query-params})))
