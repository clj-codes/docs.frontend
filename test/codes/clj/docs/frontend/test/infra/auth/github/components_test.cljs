(ns codes.clj.docs.frontend.test.infra.auth.github.components-test
  (:require [cljs.test :refer [async deftest is testing use-fixtures]]
            [clojure.string :as str]
            [codes.clj.docs.frontend.infra.auth.github.components :refer [auth-button
                                                                          auth-navlink]]
            [codes.clj.docs.frontend.test.aux.fixtures.user :as fixtures.user]
            [codes.clj.docs.frontend.test.aux.init :refer [async-cleanup
                                                           async-setup]]
            [codes.clj.docs.frontend.test.aux.testing-library :as tl]
            [helix.core :refer [$]]
            [promesa.core :as p]))

(use-fixtures :each
  {:before async-setup
   :after async-cleanup})

(deftest github-component-login-test
  (testing "should render login btns"
    (async done
      (p/catch
        (p/let [login-btn (tl/wait-for
                           #(-> (tl/mantine-render
                                 ($ auth-button {:login-link "/login"
                                                 :logoff identity
                                                 :user nil}))
                                (.findByTestId "github-login-button")))
                login-nav (tl/wait-for
                           #(-> (tl/mantine-render
                                 ($ auth-navlink {:login-link "/login"
                                                  :logoff identity
                                                  :close identity
                                                  :user nil}))
                                (.findByTestId "github-login-navlink")))
                logoff-btn (tl/wait-for
                            #(-> (tl/mantine-render
                                  ($ auth-button {:login-link "/login"
                                                  :logoff identity
                                                  :user fixtures.user/user}))
                                 (.findByTestId "github-logoff-button")))
                logoff-nav (tl/wait-for
                            #(-> (tl/mantine-render
                                  ($ auth-navlink {:login-link "/login"
                                                   :logoff identity
                                                   :close identity
                                                   :user fixtures.user/user}))
                                 (.findByTestId "github-logoff-navlink")))]

          (is (= "login"
                 (-> login-btn
                     (aget "href")
                     (str/split "/")
                     last)))

          (is (= "login"
                 (-> login-nav
                     (aget "href")
                     (str/split "/")
                     last)))

          (is (= nil
                 (-> logoff-btn
                     (aget "href"))))

          (is (= ""
                 (-> logoff-nav
                     (aget "href"))))

          (done))
        (fn [err] (is (= nil err))
          (done))))))
