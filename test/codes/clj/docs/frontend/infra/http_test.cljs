(ns codes.clj.docs.frontend.infra.http-test
  (:require [cljs.test :refer [async deftest is testing use-fixtures]]
            [codes.clj.docs.frontend.aux.init :refer [async-cleanup
                                                      async-setup
                                                      mock-http-with]]
            [codes.clj.docs.frontend.infra.http :as http]
            [promesa.core :as p]))

(use-fixtures :each
  {:before async-setup
   :after async-cleanup})

(deftest http-mock-url-ok-component-test
  (mock-http-with {"https://pokeapi.co/api/v2/pokemon/3/"
                   {:lag 500
                    :status 200
                    :body {:poke :fake}}})

  (testing "http should mock request"
    (async done
      (p/catch
        (p/let [response (-> (http/request! {:url "https://pokeapi.co/api/v2/pokemon/3/"
                                             :method :get}))]

          (is (= {:status 200, :body {:poke :fake}}
                 response))

          (done))
        (fn [err] (is (= nil err))
          (done))))))

(deftest http-mock-path-ok-component-test
  (mock-http-with {"pokemon/3/"
                   {:lag 500
                    :status 200
                    :body {:poke :fake}}})

  (testing "http should mock request"
    (async done
      (p/catch
        (p/let [response (-> (http/request! {:path "pokemon/3/"
                                             :method :get}))]

          (is (= {:status 200, :body {:poke :fake}}
                 response))

          (done))
        (fn [err] (is (= nil err))
          (done))))))

(deftest http-mock-error-component-test
  (mock-http-with {"https://pokeapi.co/api/v2/pokemon/3/"
                   {:status 404
                    :body {:error :not-found}}})

  (testing "http should error request status > 400"
    (async done
      (p/catch
        (p/let [response (-> (http/request! {:url "https://pokeapi.co/api/v2/pokemon/3/"
                                             :method :get}))]
          (is (= nil response))
          (done))

        (fn [err] (is (= {:status 404
                          :body {:error :not-found}}
                         (ex-data err)))
          (done))))))

(deftest http-non-existing-mock-error-component-test
  (mock-http-with {})

  (testing "http should error request not set in mocks"
    (async done
      (p/catch
        (p/let [response (-> (http/request! {:url "https://pokeapi.co/api/v2/pokemon/3/"
                                             :method :get}))]
          (is (= nil response))
          (done))

        (fn [err] (is (= {:status 500
                          :body "Response not set in mocks!"}
                         (ex-data err)))
          (done))))))
