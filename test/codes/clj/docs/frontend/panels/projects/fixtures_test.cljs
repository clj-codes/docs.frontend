(ns codes.clj.docs.frontend.panels.projects.fixtures-test)

(def projects
  [{:artifact "core.memoize"
    :group "org.clojure"
    :id "org.clojure/core.memoize"
    :manifest :deps
    :name "org.clojure/core.memoize"
    :paths ["/src/main/clojure"]
    :sha "30adac08491ab6dd23db452215dd0c38ea0a42f4"
    :tag "v1.0.257"
    :url "https://github.com/clojure/core.memoize"}
   {:artifact "core.logic"
    :group "org.clojure"
    :id "org.clojure/core.logic"
    :manifest :pom
    :name "org.clojure/core.logic"
    :paths ["/src/main/clojure" "/src/main/java" "/src/main/resources"]
    :sha "d854548a1eb0706150bd5f5d939c7bca162c07fb"
    :tag "v1.0.1"
    :url "https://github.com/clojure/core.logic"}
   {:artifact "clojure"
    :group "org.clojure"
    :id "org.clojure/clojure"
    :manifest :pom
    :name "org.clojure/clojure"
    :paths ["/src/clj" "/src/main/clojure" "/src/main/java" "/src/resources"]
    :sha "ce55092f2b2f5481d25cff6205470c1335760ef6"
    :tag "clojure-1.11.1"
    :url "https://github.com/clojure/clojure"}
   {:artifact "helix"
    :group "lilactown"
    :id "lilactown/helix"
    :manifest :deps
    :name "lilactown/helix"
    :paths ["/src" "/resources"]
    :sha "35127b79405e5dff6d9b74dfc674280eb93fab6d"
    :tag "v0.2.0"
    :url "https://github.com/lilactown/helix"}
   {:artifact "flex"
    :group "lilactown"
    :id "lilactown/flex"
    :manifest :deps
    :name "lilactown/flex"
    :paths ["/src" "/resources"]
    :sha "35127b79405e5dff6d9b74dfc674280eb93fab6d"
    :tag "v0.2.0"
    :url "https://github.com/lilactown/flex"}
   {:artifact "dummy"
    :group "someone"
    :id "someone/dummy"
    :manifest :deps
    :name "someone/dummy"
    :paths ["/src" "/resources"]
    :sha "35127b79405e5dff6d9b74dfc674280eb93fab6d"
    :tag "v0.0.1"
    :url "https://gitlab.com/someone/dummy"}])
