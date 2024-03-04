(ns codes.clj.docs.frontend.infra.http.component
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

(defrecord Http [config]
  HttpProvider
  (request
    [_self request-input]
    (let [{:keys [url path]} request-input
          request-url (or url (str (:base-url config) path))]
      (-> (fetch/request request-url request-input)
          (.then (fn [{:keys [status] :as resp}]
                   (if (> status 400)
                     (throw-info resp)
                     (->cljs resp))))
          (.catch (fn [resp]
                    (throw-info resp)))))))

(defn new-http [config] (->Http config))

(defrecord HttpMock [responses requests]
  HttpProvider
  (request
    [_self {:keys [url path] :as req}]
    (swap! requests merge
           (assoc req :instant (current-time)))
    (if-let [{:keys [status lag] :as response} (get-in @responses [(or url path)])]
      (if (> status 400)
        (p/rejected (ex-info "Response error" response))
        (p/delay (or lag 10)
                 (dissoc response :lag)))
      (p/rejected (do (js/console.debug "Error:" "Response not set in mocks!" (clj->js req))
                      (ex-info "Response error"
                               {:status 500 :body "Response not set in mocks!"}))))))

(defn new-http-mock
  [mocked-responses]
  (map->HttpMock {:responses (atom mocked-responses)
                  :requests (atom [])}))

