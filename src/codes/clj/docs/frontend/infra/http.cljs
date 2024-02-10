(ns codes.clj.docs.frontend.infra.http
  (:require [lambdaisland.fetch :as fetch]
            [promesa.core :as p]))

(defn- current-time [] (.getTime (js/Date.)))

(defn- ->cljs [obj]
  (js->clj obj :keywordize-keys true))

(defn- throw-info [resp]
  (throw (ex-info "Response error" (->cljs resp))))

(defprotocol HttpProvider
  (request
    [self request-input]))

(defrecord Http [_]
  HttpProvider
  (request
    [_self request-input]
    (let [{:keys [url]} request-input]
      (-> (fetch/request url request)
          (.then (fn [{:keys [status] :as resp}]
                   (if (> status 400)
                     (throw-info resp)
                     (->cljs resp))))
          (.catch (fn [resp]
                    (throw-info resp)))))))

(defn new-http [] (map->Http {}))

(defrecord HttpMock [responses requests]
  HttpProvider
  (request
    [_self {:keys [url] :as req}]
    (swap! requests merge
           (assoc req :instant (current-time)))
    (if-let [{:keys [status lag] :as response} (get-in @responses [url])]
      (if (> status 400)
        (p/rejected (ex-info "Response error" response))
        (p/delay (or lag 10) response))
      (p/rejected (ex-info "Response error"
                           {:status 500 :body "Response not set in mocks!"})))))

(defn reset-responses! [added-responses {:keys [responses]}]
  (reset! responses added-responses))

(defn new-http-mock
  [mocked-responses]
  (map->HttpMock {:responses (atom mocked-responses)
                  :requests (atom [])}))
(comment
  (-> (request (new-http)
               {:url "https://pokeapi.co/api/v2/pokemon/3/"
                :method :get})
      (.then #(prn "then" %))
      (.catch  #(prn "catch" %)))

  (-> (request (new-http-mock {"https://pokeapi.co/api/v2/pokemon/3/"
                               {:lag 500
                                :status 200
                                :body {:poke :fake}}})
               {:url "https://pokeapi.co/api/v2/pokemon/3/"
                :method :get})
      (.then #(prn "then" %))
      (.catch  #(prn "catch" %))))
