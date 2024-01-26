(ns codes.clj.docs.frontend.core-test
  (:require ["@testing-library/react" :as tlr]
            [clojure.test :refer [deftest is use-fixtures]]
            [codes.clj.docs.frontend.aux.init :refer [setup-root]]
            [helix.dom :as dom]))

(use-fixtures :each setup-root)

(deftest a-component-test
  (let [container (dom/div "helix")
        container (tlr/render container)
        div       (.getByText container "helix")]
    (is (= "helix" (.-textContent div)))))
