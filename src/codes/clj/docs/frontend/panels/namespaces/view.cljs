(ns codes.clj.docs.frontend.panels.namespaces.view
  (:require ["@mantine/core" :refer [ActionIcon Anchor Avatar Card Code
                                     Container Grid Group LoadingOverlay Text
                                     Title]]
            ["@tabler/icons-react" :refer [IconArrowRight]]
            [codes.clj.docs.frontend.infra.flex.hook :refer [use-flex]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.panels.namespaces.state :refer [namespaces-response]]
            [helix.core :refer [$]]
            [helix.dom :as dom]))

(defnc card-namespace [{:keys [id name author doc filename git-source col row]}]
  ($ Grid.Col
    ($ Card {:id (str "card-namespace-" id)
             :withBorder true
             :shadow "sm"
             :padding "lg"}
      ($ Card.Section {:withBorder true :inheritPadding true :py "sm"}
        ($ Group {:justify "space-between"}
          ($ Anchor {:href (str "/" id)
                     :fw 500} name)
          ($ ActionIcon {:component "a"
                         :href (str "/" id)
                         :variant "light"}
            ($ IconArrowRight))))

      ($ Card.Section {:withBorder true :inheritPadding true :py "sm"}
        ($ Grid
          ($ Grid.Col
            ($ Grid
              ($ Grid.Col {:span #js {:base 12 :md 2}}
                ($ Group
                  ($ Title {:order 6} "Source")
                  ($ Anchor {:size "sm" :href git-source} "Source")))
              ($ Grid.Col {:span #js {:base 12 :md 5}}
                ($ Group
                  ($ Title {:order 6} "Filename")
                  ($ Text filename)))
              ($ Grid.Col {:span #js {:base 12 :md 2}}
                ($ Group
                  ($ Title {:order 6} "Row")
                  ($ Text (str row ":" col))))))
          (when doc
            ($ Grid.Col
              ($ Title {:order 6} "Doc")
              ($ Code {:block true} doc)))))

      (when author
        ($ Card.Section {:withBorder true :inheritPadding true :py "sm"}
          ($ Group {:justify "flex-start"}
            ($ Avatar {:alt "Author"})
            ($ Text author)))))))

;; TODO view tests
(defnc org-projects []
  (let [{:keys [value loading?]} (use-flex namespaces-response)
        {:keys [namespaces project]} value
        card-namespaces (->> namespaces
                             (sort-by :name)
                             (mapv
                              (fn [{:keys [id] :as props}]
                                ($ card-namespace {:key id :& props}))))]
    ($ LoadingOverlay {:visible loading? :zIndex 1000
                       :overlayProps #js {:radius "sm" :blur 2}})

    ($ Container {:size "md"}
      ($ Title {:order 1}
        "Namespaces on "
        ($ Text {:component "span" :inherit true :variant "gradient"
                 :gradient #js {:from "cyan" :to "green" :deg 90}}
          (:id project)))

      (dom/div (dom/br))

      ($ Grid card-namespaces))))
