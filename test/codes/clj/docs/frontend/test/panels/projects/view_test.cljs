(ns codes.clj.docs.frontend.test.panels.projects.view-test
  (:require [cljs.test :refer [async deftest is testing use-fixtures]]
            [codes.clj.docs.frontend.panels.projects.state :as projects.state]
            [codes.clj.docs.frontend.panels.projects.view :refer [group-by-orgs]]
            [codes.clj.docs.frontend.test.aux.fixtures.projects :as fixtures]
            [codes.clj.docs.frontend.test.aux.init :refer [async-cleanup
                                                           async-setup
                                                           mock-http-with]]
            [codes.clj.docs.frontend.test.aux.testing-library :as tl]
            [helix.core :refer [$]]
            [promesa.core :as p]))

(use-fixtures :each
  {:before async-setup
   :after async-cleanup})

(deftest group-by-orgs-view-test
  ; mock http request
  (mock-http-with {"document/projects/"
                   {:lag 500
                    :status 200
                    :body fixtures/projects}})

  ; call initial db fetch
  (projects.state/document-projects-fetch)

  (testing "group-by-orgs should render group accordion and project cards"
    (async done
      (p/catch
        (p/let [view (tl/mantine-render ($ group-by-orgs))
                find-by-test-id-fn (fn [test-id] #(.findByTestId ^js/Object view test-id))
                org-clojure-items (tl/wait-for (find-by-test-id-fn "accordion-item-org.clojure"))
                lilactown-items (tl/wait-for (find-by-test-id-fn "accordion-item-lilactown"))
                someone-items (tl/wait-for (find-by-test-id-fn "accordion-item-someone"))
                extract-cards-fn (fn [items]
                                   (->> (.querySelectorAll items ".mantine-Card-root")
                                        (mapv #(-> % .-id))))]

          (is (= ["card-project-org.clojure/clojure"
                  "card-project-org.clojure/core.logic"
                  "card-project-org.clojure/core.memoize"]
                 (extract-cards-fn org-clojure-items)))

          (is (= ["card-project-lilactown/flex"
                  "card-project-lilactown/helix"]
                 (extract-cards-fn lilactown-items)))

          (is (= ["card-project-someone/dummy"]
                 (extract-cards-fn someone-items)))

          (done))
        (fn [err] (is (= nil err))
          (done))))))
