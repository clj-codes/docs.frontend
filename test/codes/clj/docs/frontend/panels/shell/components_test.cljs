(ns codes.clj.docs.frontend.panels.shell.components-test
  (:require ["@mantine/core" :refer [AppShell]]
            [clojure.string :as str]
            [clojure.test :refer [async deftest is testing use-fixtures]]
            [codes.clj.docs.frontend.aux.testing-library :as tl]
            [codes.clj.docs.frontend.panels.shell.components :refer [Header
                                                                     HeaderDrawer]]
            [helix.core :refer [$]]
            [promesa.core :as p]))

(use-fixtures :each
  {:before #(async done (tl/cleanup) (done))
   :after #(async done (tl/cleanup) (done))})

(deftest header-component-test
  (testing "header should render component links"
    (async done
           (p/let [_ (tl/mantine-render
                      ($ AppShell
                         ($ Header {:links [{:href "/link"
                                             :label "My link"}
                                            {:href "/link2"
                                             :label "My link 2"}]})))

                   header (tl/wait-for #(tl/get-by-testid tl/screen "header-root-links"))]

             (is (= ["link" "link2" "login"]
                    (doall
                     (map #(-> % .-href (str/split "/") last)
                          (tl/query-all header ".mantine-Button-root")))))

             (done)))))

(deftest header-drawer-component-test
  (testing "drawer should render component links"
    (async done
           (p/let [_ (tl/mantine-render
                      ($ HeaderDrawer {:links [{:href "/link"
                                                :label "My link"}
                                               {:href "/link2"
                                                :label "My link 2"}]
                                       :opened true
                                       :close identity}))
                   drawer (tl/wait-for #(tl/get-by-testid tl/screen "header-drawer"))]

             (is (= ["link" "link2" "login"]
                    (doall
                     (map #(-> % .-href (str/split "/") last)
                          (tl/query-all drawer ".mantine-NavLink-root")))))

             (done)))))
