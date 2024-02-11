(ns codes.clj.docs.frontend.infra.http
  (:require [codes.clj.docs.frontend.infra.http.component :as http.component]
            [codes.clj.docs.frontend.infra.system.state :as system.state]))

(defn request! [request-input]
  (http.component/request (:http @system.state/components) request-input))

(comment
  ; sample using url
  (-> (http.component/request
       (http.component/new-http {})
       {:url "https://pokeapi.co/api/v2/pokemon/3/"
        :method :get})
      (.then #(prn "then" %))
      (.catch  #(prn "catch" %)))

  ; sample using path
  (-> (http.component/request
       (http.component/new-http {:base-url "https://pokeapi.co/api/v2/"})
       {:path "pokemon/3/"
        :method :get})
      (.then #(prn "then" %))
      (.catch  #(prn "catch" %))))
