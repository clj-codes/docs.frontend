(ns codes.clj.docs.frontend.panels.projects.view
  (:require ["@mantine/core" :refer [Accordion ActionIcon Anchor Avatar Badge
                                     Card Code Container Grid Group Text Title]]
            ["@tabler/icons-react" :refer [IconArrowRight]]
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

(defnc card-project [{:keys [id name manifest paths sha tag url]}]
  ($ Card {:withBorder true :shadow "sm" :padding "lg"}
    ($ Card.Section {:withBorder true :inheritPadding true :py "sm"}
      ($ Group {:justify "space-between"}
        ($ Anchor {:href (str "/" id)
                   :fw 500} name)
        ($ ActionIcon {:component "a"
                       :href (str "/" id)
                       :variant "light"}
          ($ IconArrowRight))))
    ($ Card.Section {:withBorder true :inheritPadding true :py "sm"}
      ($ Grid {:c "dimmed"}
        ($ Grid.Col
          ($ Grid
            ($ Grid.Col {:span #js {:base 12 :md 5}}
              ($ Title {:order 6} "Git")
              ($ Anchor {:size "sm" :href url} url))
            ($ Grid.Col {:span #js {:base 12 :md 5}}
              ($ Title {:order 6} "Sha")
              ($ Code sha))
            ($ Grid.Col {:span #js {:base 12 :md 2}}
              ($ Title {:order 6} "Tag")
              ($ Badge {:variant "primary"} tag))))
        ($ Grid.Col {:span 24}
          ($ Group
            ($ Group
              ($ Title {:order 6} "Manifest")
              ($ Text {:size "sm" :c "bright" :fw 500} manifest))
            ($ Group
              ($ Title {:order 6} "Paths")
              ($ Group
                (map (fn [path] ($ Code {:key path :size "sm"} path))
                  paths)))))))))

(defnc accordion-label [{:keys [label image urls count-projects]}]
  ($ Group {:wrap "nowrap"}
    (dom/div
      (if image
        ($ Avatar {:src image :radius "xl" :size "lg"})
        ($ Avatar {:radius "xl" :size "lg"} (subs label 0 3))))
    (dom/div
      ($ Text label)
      ($ Text {:size "sm" :c "dimmed" :fw 400}
        (mapv #($ Anchor {:key (str "a-" %)
                          :component "a"
                          :inherit true
                          :href %} %)
          urls)
        " has " count-projects " indexed projects"))))

(defnc accordion-item [{:keys [id image count-projects urls projects]}]
  (let [project-cards (map (fn [{:keys [id] :as props}]
                             ($ card-project {:key id :& props})) projects)]
    ($ Accordion.Item {:key id :value id}
      ($ Accordion.Control
        ($ accordion-label {:label id
                            :image image
                            :urls urls
                            :count-projects count-projects}))
      ($ Accordion.Panel
        ($ Group {:justify "space-between"}
          project-cards)))))

(defnc group-by-orgs []
  (let [groups (adapters/projects->groups projects-data)
        group-item (mapv (fn [{:keys [id] :as props}]
                           ($ accordion-item {:key id :& props})) groups)]

    ($ Container {:size "md"}
      ($ Grid {:id "group-by-orgs"}
        ($ Grid.Col {:key "organization-title" :span 12}
          (dom/section
            ($ Title {:order 1}
              "Projects by "
              ($ Text {:component "span" :inherit true :variant "gradient"
                       :gradient #js {:from "cyan" :to "green" :deg 90}}
                "Organizations"))))

        (dom/div (dom/br))

        ($ Grid.Col {:key "organization-list" :span 12}
          ($ Accordion {:defaultValue "org.clojure"
                        :chevronPosition "right"
                        :variant "contained"}
            group-item))))))
