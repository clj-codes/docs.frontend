(ns codes.clj.docs.frontend.test.panels.definition.view-test
  (:require [cljs.test :refer [async deftest is testing use-fixtures]]
            [codes.clj.docs.frontend.panels.definition.state :as definition.state]
            [codes.clj.docs.frontend.panels.definition.view :refer [definition-detail]]
            [codes.clj.docs.frontend.test.aux.fixtures.definition :as fixtures]
            [codes.clj.docs.frontend.test.aux.init :refer [async-cleanup
                                                           async-setup
                                                           mock-http-with]]
            [codes.clj.docs.frontend.test.aux.testing-library :as tl]
            [helix.core :refer [$]]
            [promesa.core :as p]))

(use-fixtures :each
  {:before async-setup
   :after async-cleanup})

(deftest definition-detail-view-test
  (testing "definition-detail should render definition detail card"
    ; mock http request
    (mock-http-with {"document/definition/org.clojure/clojure/clojure.core.server/prepl/0"
                     {:lag 0
                      :status 200
                      :body fixtures/definition}})

    ; call initial db fetch
    (definition.state/definition-docs-fetch "org.clojure" "clojure" "clojure.core.server" "prepl" 0)

    (async done
      (p/catch
        (p/let [view (tl/mantine-render ($ definition-detail))
                card (tl/wait-for #(.findByTestId ^js/Object view "card-definition-org.clojure/clojure/clojure.core.server/prepl/0"))]

          (is (= "prepl"
                 (-> (.querySelector card "#card-definition-title")
                     .-textContent)))

          (is (= "Metadatasince: 1.10"
                 (-> (.querySelector card "#card-definition-metadata")
                     .-textContent)))

          (done))
        (fn [err] (is (= nil err))
          (done))))))

(deftest definition-detail-no-metadata-view-test
  (testing "definition-detail should not render metadata section card"
    (mock-http-with {"document/definition/org.clojure/clojure/clojure.core.server/prepl/1"
                     {:lag 0
                      :status 200
                      :body (update-in fixtures/definition [:definition] dissoc :added)}})

    (definition.state/definition-docs-fetch "org.clojure" "clojure" "clojure.core.server" "prepl" 1)

    (async done
      (p/catch
        (p/let [view (tl/mantine-render ($ definition-detail))
                card (tl/wait-for #(.findByTestId ^js/Object view "card-definition-org.clojure/clojure/clojure.core.server/prepl/0"))]

          (is (= nil
                 (.querySelector card "#card-definition-metadata")))

          (done))
        (fn [err] (is (= nil err))
          (done))))))
