(ns codes.clj.docs.frontend.infra.http
  (:require [codes.clj.docs.frontend.infra.http.component :as http.component]
            [codes.clj.docs.frontend.infra.system.state :as system.state]))

(defn request! [request-input]
  (http.component/request (:http @system.state/components) request-input))

(comment
  (-> (http.component/request
       (http.component/new-http)
       {:url "https://pokeapi.co/api/v2/pokemon/3/"
        :method :get})
      (.then #(prn "then" %))
      (.catch  #(prn "catch" %)))

  (-> (http.component/request
       (http.component/new-http-mock {"https://pokeapi.co/api/v2/pokemon/3/"
                                      {:lag 500
                                       :status 200
                                       :body {:poke :fake}}})
       {:url "https://pokeapi.co/api/v2/pokemon/3/"
        :method :get})
      (.then #(prn "then" %))
      (.catch  #(prn "catch" %))))
