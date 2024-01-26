(ns codes.clj.docs.frontend.infra.http-client
  (:require [lambdaisland.fetch :as fetch]
            [promesa.core :as p]
            [refx.alpha :refer [dispatch reg-fx]]))

(defn- js->cljs-key [obj]
  (js->clj obj :keywordize-keys true))

(defn- send-request!
  [{:keys [url on-success on-failure] :as request} fn-request]
  (-> (fn-request url request)
      (.then (fn [{:keys [status] :as resp}]
               (if (> status 400)
                 (dispatch (conj on-failure (js->cljs-key resp)))
                 (dispatch (conj on-success (js->cljs-key resp))))))
      (.catch (fn [resp]
                (dispatch (conj on-failure (js->cljs-key resp)))))))

(defn http-effect
  [fn-request]
  (fn [request]
    (if (sequential? request)
      (doseq [req request]
        (send-request! req fn-request))
      (send-request! request fn-request))))

(defn fetch-request-mock [responses]
  (fn [url _request]
    (let [{:keys [lag] :as response} (get-in responses
                                             [url]
                                             {:status 500
                                              :body "Response not set in mocks!"})]
      (p/delay (or lag 100) response))))

(reg-fx :codes.clj.docs.frontend.infra/http-client (http-effect fetch/request))

(comment
  ; is possible to mock directly this effect
  (reg-fx
   :codes.clj.docs.frontend.infra/http-client
   (http-effect (fetch-request-mock
                 {"/login/send-email" {:status 201
                                       :body #js {:ok true}}}))))
