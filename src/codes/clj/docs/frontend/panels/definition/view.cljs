(ns codes.clj.docs.frontend.panels.definition.view
  (:refer-clojure :exclude [namespace])
  (:require ["@mantine/core" :refer [Alert Anchor Badge Card Code Container
                                     Grid Group LoadingOverlay Space Text
                                     Title]]
            ["@tabler/icons-react" :refer [IconInfoCircle]]
            [clojure.string :as str]
            [codes.clj.docs.frontend.components.navigation :refer [back-to-top
                                                                   breadcrumbs]]
            [codes.clj.docs.frontend.infra.flex.hook :refer [use-flex]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.panels.definition.state :refer [definition-response]]
            [helix.core :refer [$]]
            [helix.dom :as dom]))

(defnc card-definition [{:keys [id added defined-by arglist-strs doc filename
                                git-source col row deprecated macro private]
                         :as definition}]
  ($ Card {:id (str "card-definition-" id)
           :key (str "card-definition-" id)
           :data-testid (str "card-definition-" id)
           :withBorder true
           :shadow "sm"
           :padding "lg"}

    ($ Card.Section {:withBorder true :inheritPadding true :py "sm"}
      ($ Title {:id "card-definition-title" :order 2}
        (:name definition)))

    (when (or added deprecated macro private)
      ($ Card.Section {:withBorder true :inheritPadding true :py "sm"}
        ($ Group {:id "card-definition-metadata"
                  :justify "flex-start"}
          ($ Title {:order 6} "Metadata")
          (when added
            ($ Badge {:variant "light" :color "moonstone"}
              (str "since: " added)))
          (when deprecated
            ($ Badge {:variant "light" :color "orange"}
              (str "deprecated: " deprecated)))
          (when macro
            ($ Badge {:variant "light" :color "grape"} "macro"))
          (when private
            ($ Badge {:variant "light" :color "gray"} "private")))))

    ($ Card.Section {:withBorder true :inheritPadding true :py "sm"}
      ($ Group {:justify "space-between"}
        ($ Group
          ($ Title {:order 6} "Source")
          ($ Anchor {:size "md" :href git-source :fw 500}
            ($ Text (rest (str filename ":" row ":" col)))))
        ($ Group
          ($ Title {:order 6} "Defined by")
          ($ Text defined-by))))

    ($ Card.Section {:withBorder true :inheritPadding true :py "sm"}
      ($ Grid {:justify "space-between"}
        (when arglist-strs
          ($ Grid.Col {:span 12}
            ($ Title {:order 6} "Arguments")
            (map
              #($ Group {:key (str %) :my "sm"}
                 ($ Code {:c "white" :color "moonstone"}
                   ($ Text
                     ($ Text {:span true :size "sm"} (str "(" (:name definition) " "))
                     ($ Text {:span true :size "sm" :fw 1000}
                       (str/replace % #"\[|\]" ""))
                     ($ Text {:span true :size "sm"} ")"))))
              arglist-strs)))
        (when doc
          ($ Grid.Col {:span 12}
            ($ Title {:order 6} "Doc")
            ($ Code {:style #js {:fontSize "var(--mantine-font-size-sm)"}
                     :block true}
              doc)))))))

(defnc definition-detail []
  (let [{:keys [state error value loading?]} (use-flex definition-response)
        {:keys [project namespace definition]} value
        project-id (:id project)
        namespace-id (:id namespace)]

    ($ Container {:size "md"}
      ($ LoadingOverlay {:visible loading? :zIndex 1000
                         :overlayProps #js {:radius "sm" :blur 2}})

      (if (= state :error)
        ($ Alert {:variant "light" :color "red"
                  :radius "md" :title "Error"
                  :icon ($ IconInfoCircle)}
          (str error))

        (dom/div
          ($ breadcrumbs {:items [{:id "projects" :href "/projects" :title "Projects"}
                                  {:id project-id :href (str "/" project-id) :title project-id}
                                  {:id namespace-id :href (str "/" namespace-id) :title (:name namespace)}
                                  {:id (:id definition) :title (:name definition)}]})

          ($ Space {:h "lg"})
          ($ card-definition {:& definition})

          ($ back-to-top))))))
