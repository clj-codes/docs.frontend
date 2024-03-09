(ns codes.clj.docs.frontend.test.panels.shell.components-test
  (:require ["@mantine/core" :refer [AppShell]]
            [cljs.test :refer [async deftest is testing use-fixtures]]
            [clojure.string :as str]
            [codes.clj.docs.frontend.panels.shell.components :refer [header
                                                                     header-drawer]]
            [codes.clj.docs.frontend.test.aux.init :refer [async-cleanup
                                                           async-setup]]
            [codes.clj.docs.frontend.test.aux.testing-library :as tl]
            [helix.core :refer [$]]
            [promesa.core :as p]))

(use-fixtures :each
  {:before async-setup
   :after async-cleanup})

(deftest header-component-test
  (testing "header should render component links"
    (async done
      (p/catch
        (p/let [header (tl/wait-for
                        #(-> (tl/mantine-render
                              ($ AppShell
                                ($ header {:links [{:href "/link"
                                                    :label "My link"}
                                                   {:href "/link2"
                                                    :label "My link 2"}]
                                           :login-link "/login"})))
                             (.findByTestId "header-root-links")))
                links (->> (.querySelectorAll header ".mantine-Button-root")
                           (map #(-> % .-href (str/split "/") last)))]

          (is (= ["link" "link2" "login"]
                 links))

          (done))
        (fn [err] (is (= nil err))
          (done))))))

(deftest header-drawer-component-test
  (testing "drawer should render component links"
    (async done
      (p/catch
        (p/let [_ (tl/mantine-render
                   ($ header-drawer {:links [{:href "/link"
                                              :label "My link"}
                                             {:href "/link2"
                                              :label "My link 2"}]
                                     :login-link "/login"
                                     :opened true
                                     :close identity}))
                drawer (tl/wait-for #(.findByTestId (tl/document) "header-drawer-scrollarea"))
                links (->> (.querySelectorAll drawer ".mantine-NavLink-root")
                           (map #(-> % .-href (str/split "/") last)))]

          (is (= ["link" "link2" "login"]
                 links))

          (done))
        (fn [err] (is (= nil err))
          (done))))))
