(ns codes.clj.docs.frontend.test.panels.namespaces.view-test
  (:require [cljs.test :refer [async deftest is testing use-fixtures]]
            [codes.clj.docs.frontend.panels.namespaces.state :as projects.state]
            [codes.clj.docs.frontend.panels.namespaces.view :refer [org-projects]]
            [codes.clj.docs.frontend.test.aux.fixtures.namespaces :as fixtures]
            [codes.clj.docs.frontend.test.aux.init :refer [async-cleanup
                                                           async-setup
                                                           mock-http-with]]
            [codes.clj.docs.frontend.test.aux.testing-library :as tl]
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
                items (tl/wait-for #(.findByTestId ^js/Object view "namespace-cards-grid"))
                extract-cards-fn (fn [items]
                                   (->> (.querySelectorAll items ".mantine-Card-root")
                                        (map #(-> % .-id))))]

          (is (= ["card-namespace-org.clojure/clojure/clojure.test.junit"
                  "card-namespace-org.clojure/clojure/clojure.xml"
                  "card-namespace-org.clojure/clojure/clojure.zip"]
                 (extract-cards-fn items)))

          (done))
        (fn [err] (is (= nil err))
          (done))))))
