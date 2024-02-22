(ns codes.clj.docs.frontend.test.components.documents-test
  (:require [cljs.test :refer [async deftest is testing use-fixtures]]
            [codes.clj.docs.frontend.components.documents :refer [card-namespace
                                                                  card-project]]
            [codes.clj.docs.frontend.test.aux.init :refer [async-cleanup
                                                           async-setup]]
            [codes.clj.docs.frontend.test.aux.testing-library :as tl]
            [helix.core :refer [$]]
            [matcher-combinators.test :refer [match?]]
            [promesa.core :as p]))

(use-fixtures :each
  {:before async-setup
   :after async-cleanup})

(def project-fixture
  {:artifact "core.memoize"
   :group "org.clojure"
   :id "org.clojure/core.memoize"
   :manifest :deps
   :name "org.clojure/core.memoize"
   :paths ["/src/main/clojure"]
   :sha "30adac08491ab6dd23db452215dd0c38ea0a42f4"
   :tag "v1.0.257"
   :url "https://github.com/clojure/core.memoize"})

(def namespace-fixture
  {:artifact "clojure",
   :author "Rich Hickey",
   :col 1,
   :doc "XML reading/writing.",
   :end-col 60,
   :end-row 13,
   :filename "/src/clj/clojure/xml.clj",
   :git-source "https://github.com/clojure/clojure/blob/clojure-1.11.1/src/clj/clojure/xml.clj#L9",
   :group "org.clojure",
   :id "org.clojure/clojure/clojure.xml",
   :meta {},
   :name "clojure.xml",
   :name-col 3,
   :name-end-col 14,
   :name-end-row 11,
   :name-row 11,
   :project-id "org.clojure/clojure",
   :row 9})

(deftest card-project-component-test
  (testing "card-project should render component links and texts depending on header prop"
    (async done
      (p/catch
        (p/let [list-project-card (tl/wait-for
                                   #(-> (tl/mantine-render ($ card-project {:& project-fixture}))
                                        (.findByTestId "card-project-org.clojure/core.memoize")))
                header-project-card (tl/wait-for
                                     #(-> (tl/mantine-render ($ card-project {:header true :& project-fixture}))
                                          (.findByTestId "card-project-org.clojure/core.memoize")))]

          (is (match? #"/org.clojure/core.memoize$"
                      (-> list-project-card
                          (.querySelector ".mantine-Card-section")
                          (aget "href"))))

          (is (match? "org.clojure/core.memoize"
                      (-> header-project-card
                          (.querySelector ".mantine-Card-section")
                          (.querySelector ".mantine-Title-root")
                          .-textContent)))

          (is (nil? (-> header-project-card
                        (.querySelector ".mantine-Card-section")
                        (aget "href"))))

          (done))
        (fn [err] (is (= nil err))
          (done))))))

(deftest card-namespace-component-test
  (testing "card-namespace should render component links and texts depending on header prop"
    (async done
      (p/catch
        (p/let [list-project-card (tl/wait-for
                                   #(-> (tl/mantine-render ($ card-namespace {:& namespace-fixture}))
                                        (.findByTestId "card-namespace-org.clojure/clojure/clojure.xml")))
                header-project-card (tl/wait-for
                                     #(-> (tl/mantine-render ($ card-namespace {:header true :& namespace-fixture}))
                                          (.findByTestId "card-namespace-org.clojure/clojure/clojure.xml")))]

          (is (match? #"org.clojure/clojure/clojure.xml$"
                      (-> list-project-card
                          (.querySelector ".mantine-Card-section")
                          (aget "href"))))

          (is (match? "clojure.xml"
                      (-> header-project-card
                          (.querySelector ".mantine-Card-section")
                          (.querySelector ".mantine-Title-root")
                          .-textContent)))

          (is (nil? (-> header-project-card
                        (.querySelector ".mantine-Card-section")
                        (aget "href"))))

          (done))
        (fn [err] (is (= nil err))
          (done))))))
