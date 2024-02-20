(ns codes.clj.docs.frontend.panels.projects.view
  (:require ["@mantine/core" :refer [Accordion Anchor Avatar Container Grid
                                     Group LoadingOverlay Space Text Title]]
            [codes.clj.docs.frontend.components.documents :refer [card-project]]
            [codes.clj.docs.frontend.infra.flex.hook :refer [use-flex]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.panels.projects.state :refer [document-projects-response]]
            [helix.core :refer [$]]
            [helix.dom :as dom]))

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
        (map #($ Anchor {:key (str "a-" %)
                         :component "a"
                         :inherit true
                         :href %} %)
          urls)
        " has " count-projects " indexed projects"))))

(defnc accordion-item [{:keys [id image count-projects urls projects]}]
  (let [project-cards (->> projects
                           (sort-by :artifact)
                           (map (fn [{:keys [id] :as props}]
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

        ($ Space {:h "xl"})

        ($ Grid.Col {:key "organization-list" :span 12}
          ($ LoadingOverlay {:visible loading? :zIndex 1000 :overlayProps #js {:radius "sm" :blur 2}})
          ($ Accordion {:defaultValue "org.clojure"
                        :chevronPosition "right"
                        :variant "contained"}
            (map (fn [{:keys [id] :as props}]
                   ($ accordion-item {:key id :& props})) value)))))))
