(ns codes.clj.docs.frontend.panels.namespaces.view-test
  (:require [cljs.test :refer [async deftest is testing use-fixtures]]
            [codes.clj.docs.frontend.aux.fixtures.namespaces :as fixtures]
            [codes.clj.docs.frontend.aux.init :refer [async-cleanup
                                                      async-setup
                                                      mock-http-with]]
            [codes.clj.docs.frontend.aux.testing-library :as tl]
            [codes.clj.docs.frontend.panels.namespaces.state :as projects.state]
            [codes.clj.docs.frontend.panels.namespaces.view :refer [org-projects]]
            [helix.core :refer [$]]
            [promesa.core :as p]))

(use-fixtures :each
  {:before async-setup
   :after async-cleanup})

(deftest org-projects-view-test
  ; mock http request
  (mock-http-with {"document/namespaces/org.clojure/clojure"
                   {:lag 500
                    :status 200
                    :body fixtures/namespaces}})

  ; call initial db fetch
  (projects.state/namespaces-fetch "org.clojure" "clojure")

  (testing "org-projects should render project namespace cards"
    (async done
      (p/catch
        (p/let [view (tl/mantine-render ($ org-projects))
                items (tl/wait-for #(.findByTestId view "namespace-cards-grid"))
                extract-cards-fn (fn [items]
                                   (->> (.querySelectorAll items ".mantine-Card-root")
                                        (mapv #(-> % .-id))))]

          (is (= ["card-namespace-org.clojure/clojure/clojure.test.junit"
                  "card-namespace-org.clojure/clojure/clojure.xml"
                  "card-namespace-org.clojure/clojure/clojure.zip"]
                 (extract-cards-fn items)))

          (done))
        (fn [err] (is (= nil err))
          (done))))))
