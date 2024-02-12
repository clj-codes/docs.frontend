(ns codes.clj.docs.frontend.panels.projects.view
  (:require ["@mantine/core" :refer [Container Grid Title]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [helix.core :refer [$]]
            [helix.dom :as dom]
            [clojure.string :as str]))

(def projects-data
  [{:artifact "core.cache",
    :group "org.clojure",
    :id "org.clojure/core.cache",
    :manifest "deps",
    :name "org.clojure/core.cache",
    :paths ["/src/main/clojure"],
    :sha "b9b3192fd7beda68a06af7de4b6d4c2a54515094",
    :tag "v1.0.225",
    :url "https://github.com/clojure/core.cache"}
   {:artifact "data.xml",
    :group "org.clojure",
    :id "org.clojure/data.xml",
    :manifest "pom",
    :name "org.clojure/data.xml",
    :paths ["/src/main/clojure" "/src/main/java" "/src/main/resources"],
    :sha "4fbff240e0e4d57537b616fc4c2b7f28f6555e20",
    :tag "v0.2.0-alpha8",
    :url "https://github.com/clojure/data.xml"}
   {:artifact "data.csv",
    :group "org.clojure",
    :id "org.clojure/data.csv",
    :manifest "deps",
    :name "org.clojure/data.csv",
    :paths ["/src/main/clojure"],
    :sha "80c94ef6592f07d62c489359e8535343689d8135",
    :tag "v1.0.1",
    :url "https://github.com/clojure/data.csv"}
   {:artifact "core.async",
    :group "org.clojure",
    :id "org.clojure/core.async",
    :manifest "deps",
    :name "org.clojure/core.async",
    :paths ["/src/main/clojure"],
    :sha "96adc333bb02c8fc60bd51306950b3ad291b3460",
    :tag "v1.6.673",
    :url "https://github.com/clojure/core.async"}
   {:artifact "data.json",
    :group "org.clojure",
    :id "org.clojure/data.json",
    :manifest "deps",
    :name "org.clojure/data.json",
    :paths ["/src/main/clojure"],
    :sha "43c122e91c5c5e46dc58dc7e8fcbb64bb9f88a14",
    :tag "v2.4.0",
    :url "https://github.com/clojure/data.json"}
   {:artifact "core.memoize",
    :group "org.clojure",
    :id "org.clojure/core.memoize",
    :manifest "deps",
    :name "org.clojure/core.memoize",
    :paths ["/src/main/clojure"],
    :sha "30adac08491ab6dd23db452215dd0c38ea0a42f4",
    :tag "v1.0.257",
    :url "https://github.com/clojure/core.memoize"}
   {:artifact "core.logic",
    :group "org.clojure",
    :id "org.clojure/core.logic",
    :manifest "pom",
    :name "org.clojure/core.logic",
    :paths ["/src/main/clojure" "/src/main/java" "/src/main/resources"],
    :sha "d854548a1eb0706150bd5f5d939c7bca162c07fb",
    :tag "v1.0.1",
    :url "https://github.com/clojure/core.logic"}
   {:artifact "clojure",
    :group "org.clojure",
    :id "org.clojure/clojure",
    :manifest "pom",
    :name "org.clojure/clojure",
    :paths ["/src/clj" "/src/main/clojure" "/src/main/java" "/src/resources"],
    :sha "ce55092f2b2f5481d25cff6205470c1335760ef6",
    :tag "clojure-1.11.1",
    :url "https://github.com/clojure/clojure"}])

(defn projects->groups [projects]
  (->> projects
       (group-by :group)
       (map (fn [[group projects]]
              (let [urls (->> projects
                              (map (fn [{:keys [url artifact]}]
                                     (str/replace-first url (re-pattern (str "/" artifact "$")) "")))
                              distinct
                              (into #{}))
                    image (when-let [github (first (filter #(re-matches #".*github\.com.*" %) urls))]
                            (str github ".png?size=50"))]
                {:id group
                 :image image
                 :count-projects (count projects)
                 :urls urls
                 :projects projects})))))

(projects->groups projects-data)

;; TODO 
(defnc group-by-orgs []
  ($ Container {:size "md"}
    ($ Grid {:id "group-by-orgs"}
      ($ Grid.Col {:span 12}
        (dom/section
          ($ Title {:order 2} "Projects Grouped by Organizations"))))))
