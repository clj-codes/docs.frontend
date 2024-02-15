(ns codes.clj.docs.frontend.panels.projects.view
  (:require ["@mantine/core" :refer [Accordion ActionIcon Anchor Avatar Badge
                                     Card Code Container Grid Group
                                     LoadingOverlay Text Title]]
            ["@tabler/icons-react" :refer [IconArrowRight]]
            [codes.clj.docs.frontend.infra.flex.hook :refer [use-flex]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.panels.projects.state :refer [document-projects-response]]
            [helix.core :refer [$]]
            [helix.dom :as dom]))

(defnc card-project [{:keys [id name manifest paths sha tag url]}]
  ($ Card {:id (str "card-project-" id)
           :withBorder true :shadow "sm" :padding "lg"}
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
              ($ Text {:size "sm" :c "bright" :fw 500} (str manifest)))
            ($ Group
              ($ Title {:order 6} "Paths")
              ($ Group
                (mapv (fn [path] ($ Code {:key path :size "sm"} path)) paths)))))))))

(defnc accordion-label [{:keys [label image urls count-projects]}]
  ($ Group {:data-testid (str "accordion-label-" label)
            :wrap "nowrap"}
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
  (let [project-cards (->> projects
                           (sort-by :artifact)
                           (mapv (fn [{:keys [id] :as props}]
                                   ($ card-project {:key id :& props}))))]
    ($ Accordion.Item {:id (str "accordion-item-" id)
                       :data-testid (str "accordion-item-" id)
                       :key id :value id}
      ($ Accordion.Control
        ($ accordion-label {:label id
                            :image image
                            :urls urls
                            :count-projects count-projects}))
      ($ Accordion.Panel
        ($ Group {:justify "space-between"}
          project-cards)))))

(defnc group-by-orgs []
  (let [{:keys [value loading?]} (use-flex document-projects-response)]
    ($ Container {:size "md"}
      ($ Grid
        ($ Grid.Col {:key "organization-title" :span 12}
          (dom/section
            ($ Title {:order 1}
              "Projects by "
              ($ Text {:component "span" :inherit true :variant "gradient"
                       :gradient #js {:from "cyan" :to "green" :deg 90}}
                "Organizations"))))

        (dom/div (dom/br))

        ($ Grid.Col {:key "organization-list" :span 12}
          ($ LoadingOverlay {:visible loading? :zIndex 1000 :overlayProps #js {:radius "sm" :blur 2}})
          ($ Accordion {:defaultValue "org.clojure"
                        :chevronPosition "right"
                        :variant "contained"}
            (mapv (fn [{:keys [id] :as props}]
                    ($ accordion-item {:key id :& props})) value)))))))
