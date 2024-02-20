(ns codes.clj.docs.frontend.test.components.navigation-test
  (:require [cljs.test :refer [async deftest is testing use-fixtures]]
            [codes.clj.docs.frontend.components.navigation :refer [breadcrumbs]]
            [codes.clj.docs.frontend.test.aux.init :refer [async-cleanup
                                                           async-setup]]
            [codes.clj.docs.frontend.test.aux.testing-library :as tl]
            [helix.core :refer [$]]
            [promesa.core :as p]))

(use-fixtures :each
  {:before async-setup
   :after async-cleanup})

(def items [{:id "projects" :href "/projects" :title "Projects"}
            {:id "namespace" :title "Namespace"}])

(deftest breadcrumbs-component-test
  (testing "breadcrumbs should render component links and texts"
    (async done
      (p/catch
        (p/let [breads (tl/wait-for
                        #(-> (tl/mantine-render ($ breadcrumbs {:items items}))
                             (.findByTestId "breadcrumbs")))]

          (is (= [{:tag "A" :val "Projects"}
                  {:tag "P" :val "Namespace"}]
                 (->> (.querySelectorAll breads ".components-navigation-breadcrumbs")
                      (map (fn [elem]
                             {:tag (.-tagName elem)
                              :val (.-textContent elem)})))))

          (done))
        (fn [err] (is (= nil err))
          (done))))))
