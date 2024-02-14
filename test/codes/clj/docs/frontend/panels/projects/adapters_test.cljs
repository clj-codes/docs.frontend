(ns codes.clj.docs.frontend.panels.projects.adapters-test
  (:require [cljs.test :refer [deftest is use-fixtures]]
            [codes.clj.docs.frontend.aux.init :refer [sync-setup]]
            [codes.clj.docs.frontend.panels.projects.adapters :as adapters]
            [codes.clj.docs.frontend.panels.projects.fixtures-test :as fixtures]))

(use-fixtures :each sync-setup)

(def expected-output
  [{:id "org.clojure"
    :image "https://github.com/clojure.png?size=200"
    :count-projects 3
    :urls #{"https://github.com/clojure"}
    :projects
    [{:group "org.clojure"
      :name "org.clojure/core.memoize"
      :paths ["/src/main/clojure"]
      :manifest :deps
      :id "org.clojure/core.memoize"
      :url "https://github.com/clojure/core.memoize"
      :artifact "core.memoize"
      :tag "v1.0.257"
      :sha "30adac08491ab6dd23db452215dd0c38ea0a42f4"}
     {:group "org.clojure"
      :name "org.clojure/core.logic"
      :paths
      ["/src/main/clojure" "/src/main/java" "/src/main/resources"]
      :manifest :pom
      :id "org.clojure/core.logic"
      :url "https://github.com/clojure/core.logic"
      :artifact "core.logic"
      :tag "v1.0.1"
      :sha "d854548a1eb0706150bd5f5d939c7bca162c07fb"}
     {:group "org.clojure"
      :name "org.clojure/clojure"
      :paths
      ["/src/clj" "/src/main/clojure" "/src/main/java" "/src/resources"]
      :manifest :pom
      :id "org.clojure/clojure"
      :url "https://github.com/clojure/clojure"
      :artifact "clojure"
      :tag "clojure-1.11.1"
      :sha "ce55092f2b2f5481d25cff6205470c1335760ef6"}]}
   {:id "lilactown"
    :image "https://github.com/lilactown.png?size=200"
    :count-projects 2
    :urls #{"https://github.com/lilactown"}
    :projects
    [{:group "lilactown"
      :name "lilactown/helix"
      :paths ["/src" "/resources"]
      :manifest :deps
      :id "lilactown/helix"
      :url "https://github.com/lilactown/helix"
      :artifact "helix"
      :tag "v0.2.0"
      :sha "35127b79405e5dff6d9b74dfc674280eb93fab6d"}
     {:group "lilactown"
      :name "lilactown/flex"
      :paths ["/src" "/resources"]
      :manifest :deps
      :id "lilactown/flex"
      :url "https://github.com/lilactown/flex"
      :artifact "flex"
      :tag "v0.2.0"
      :sha "35127b79405e5dff6d9b74dfc674280eb93fab6d"}]}
   {:id "someone"
    :image nil
    :count-projects 1
    :urls #{"https://gitlab.com/someone"}
    :projects
    [{:group "someone"
      :name "someone/dummy"
      :paths ["/src" "/resources"]
      :manifest :deps
      :id "someone/dummy"
      :url "https://gitlab.com/someone/dummy"
      :artifact "dummy"
      :tag "v0.0.1"
      :sha "35127b79405e5dff6d9b74dfc674280eb93fab6d"}]}])

(deftest projects->groups-test
  (is (= expected-output
         (adapters/projects->groups fixtures/projects))))
