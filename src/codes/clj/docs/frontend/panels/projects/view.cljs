(ns codes.clj.docs.frontend.panels.projects.view
  (:require ["@mantine/core" :refer [Accordion Anchor Avatar Container Grid
                                     Group Text Title]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.panels.projects.adapters :as adapters]
            [helix.core :refer [$]]
            [helix.dom :as dom]))
;
(def projects-data
  [{:artifact "core.memoize",
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
    :url "https://github.com/clojure/clojure"}
   {:artifact "helix",
    :group "lilactown",
    :id "lilactown/helix",
    :manifest "deps",
    :name "lilactown/helix",
    :paths ["/src" "/resources"],
    :sha "35127b79405e5dff6d9b74dfc674280eb93fab6d",
    :tag "v0.2.0",
    :url "https://github.com/lilactown/helix"}
   {:artifact "flex",
    :group "lilactown",
    :id "lilactown/flex",
    :manifest "deps",
    :name "lilactown/flex",
    :paths ["/src" "/resources"],
    :sha "35127b79405e5dff6d9b74dfc674280eb93fab6d",
    :tag "v0.2.0",
    :url "https://github.com/lilactown/flex"}
   {:artifact "dummy",
    :group "someone",
    :id "someone/dummy",
    :manifest "deps",
    :name "someone/dummy",
    :paths ["/src" "/resources"],
    :sha "35127b79405e5dff6d9b74dfc674280eb93fab6d",
    :tag "v0.0.1",
    :url "https://gitlab.com/someone/dummy"}])

(defnc accordion-label [{:keys [label image urls count-projects]}]
  ($ Group {:wrap "nowrap"}
    (if image
      ($ Avatar {:src image :radius "xl" :size "lg"})
      ($ Avatar {:radius "xl" :size "lg"} label))
    (dom/div
      ($ Text label)
      ($ Text {:size "sm" :c "dimmed" :fw 400}
        (mapv #($ Anchor {:component "a" :inherit true :href %} %) urls)
        " has " count-projects " indexed projects"))))

(defnc accordion-item [{:keys [id image count-projects urls projects]}]
  ($ Accordion.Item {:value id :key id}
    ($ Accordion.Control
      ($ accordion-label {:label id
                          :image image
                          :urls urls
                          :count-projects count-projects})
      ($ Accordion.Panel
        ($ Text {:size "sm"}
          (str projects))))))

(defnc group-by-orgs []
  (let [groups (adapters/projects->groups projects-data)
        group-item (map (fn [group] ($ accordion-item {:& group})) groups)]

    ($ Container {:size "md"}
      ($ Grid {:id "group-by-orgs"}
        ($ Grid.Col {:span 12}
          (dom/section
            ($ Title {:order 1}
              "Projects by "
              ($ Text {:component "span" :inherit true :variant "gradient"
                       :gradient #js {:from "cyan" :to "green" :deg 90}}
                "Organizations"))))

        (dom/div (dom/br))

        ($ Grid.Col {:span 12}
          ($ Accordion {:defaultValue "org.clojure"
                        :chevronPosition "right"
                        :variant "contained"}
            group-item))))))
