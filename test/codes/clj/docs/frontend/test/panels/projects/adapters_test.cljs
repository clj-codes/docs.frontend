(ns codes.clj.docs.frontend.test.panels.projects.adapters-test
  (:require [cljs.test :refer [deftest is use-fixtures]]
            [codes.clj.docs.frontend.panels.projects.adapters :as adapters]
            [codes.clj.docs.frontend.test.aux.fixtures.projects :as fixtures]
            [codes.clj.docs.frontend.test.aux.init :refer [sync-setup]]
            [matcher-combinators.test :refer [match?]]))

(use-fixtures :each sync-setup)

(def expected-output
  [{:id "org.clojure"
    :image "https://github.com/clojure.png?size=200"
    :count-projects 3
    :urls #{"https://github.com/clojure"}
    :projects
    [{:id "org.clojure/core.memoize"}
     {:id "org.clojure/core.logic"}
     {:id "org.clojure/clojure"}]}
   {:id "lilactown"
    :image "https://github.com/lilactown.png?size=200"
    :count-projects 2
    :urls #{"https://github.com/lilactown"}
    :projects
    [{:id "lilactown/helix"}
     {:id "lilactown/flex"}]}
   {:id "someone"
    :image nil
    :count-projects 1
    :urls #{"https://gitlab.com/someone"}
    :projects
    [{:id "someone/dummy"}]}])

(deftest projects->groups-test
  (is (match? expected-output
              (adapters/projects->groups fixtures/projects))))
