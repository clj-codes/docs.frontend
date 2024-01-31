(ns codes.clj.docs.frontend.panels.shell.components-test
  (:require ["@mantine/core" :refer [AppShell]]
            [cljs.test :refer [async deftest is testing use-fixtures]]
            [clojure.string :as str]
            [codes.clj.docs.frontend.aux.init :refer [async-cleanup
                                                      async-setup]]
            [codes.clj.docs.frontend.aux.testing-library :as tl]
            [codes.clj.docs.frontend.panels.shell.components :refer [Header
                                                                     HeaderDrawer]]
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
                                 ($ Header {:links [{:href "/link"
                                                     :label "My link"}
                                                    {:href "/link2"
                                                     :label "My link 2"}]})))
                             (tl/get-by-testid "header-root-links")))
                links (->> (tl/query-all header ".mantine-Button-root")
                           (mapv #(-> % .-href (str/split "/") last)))]

          (is (= ["link" "link2" "login"]
                 links))

          (done))
        (fn [err] (js/console.error err))))))

(deftest header-drawer-component-test
  (testing "drawer should render component links"
    (async done
      (p/catch
        (p/let [_ (tl/mantine-render
                   ($ HeaderDrawer {:links [{:href "/link"
                                             :label "My link"}
                                            {:href "/link2"
                                             :label "My link 2"}]
                                    :opened true
                                    :close identity}))
                drawer (tl/wait-for #(tl/get-by-testid (tl/document) "header-drawer-scrollarea"))
                links (->> (tl/query-all drawer ".mantine-NavLink-root")
                           (mapv #(-> % .-href (str/split "/") last)))]

          (is (= ["link" "link2" "login"]
                 links))

          (done))
        (fn [err] (js/console.error err))))))
