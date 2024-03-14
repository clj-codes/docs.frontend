(ns codes.clj.docs.frontend.panels.definition.view
  (:refer-clojure :exclude [namespace])
  (:require ["@mantine/core" :refer [Alert Anchor Badge Button Card Center
                                     Code Container Grid Group LoadingOverlay
                                     Skeleton Space Text Title]]
            ["@tabler/icons-react" :refer [IconInfoCircle]]
            [clojure.string :as str]
            [codes.clj.docs.frontend.components.markdown :refer [markdown-editor]]
            [codes.clj.docs.frontend.components.navigation :refer [back-to-top
                                                                   breadcrumbs]]
            [codes.clj.docs.frontend.infra.flex.hook :refer [use-flex]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.panels.definition.state :refer [definition-docs-results
                                                                     definition-social-results]]
            [helix.core :refer [$]]
            [helix.dom :as dom]
            [helix.hooks :as hooks]))

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
          ($ Anchor {:size "md" :href git-source :fw "bold"}
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
                     ($ Text {:span true :size "sm"} (str "(" (:name definition)))
                     ($ Text {:span true :size "sm" :fw 1000}
                       (let [args (str/replace % #"\[|\]" "")]
                         (when (not (str/blank? args))
                           (str " " args))))
                     ($ Text {:span true :size "sm"} ")"))))
              arglist-strs)))
        (when doc
          ($ Grid.Col {:span 12}
            ($ Title {:order 6} "Doc")
            ($ Code {:style #js {:fontSize "var(--mantine-font-size-sm)"}
                     :block true}
              doc)))))))

(defnc card-loading-social []
  ($ Card {:id "card-social-loading"
           :key "card-social-loading"
           :data-testid "card-social-loading"
           :withBorder true
           :shadow "sm"
           :padding "lg"}
    ($ Group {:mb "md"}
      ($ Skeleton {:height 30 :circle true})
      ($ Skeleton {:height 100 :radius "md"}))))

;; todo finish & test
(defnc card-notes [{:keys [_definition notes current-user]}]
  (let [[new-note set-new-note] (hooks/use-state "")]

    ($ Card {:id "card-notes"
             :key "card-notes"
             :data-testid "card-notes"
             :withBorder true
             :shadow "sm"
             :padding "lg"}

      ($ Card.Section {:withBorder true :inheritPadding true :py "sm"}
        ($ Title {:id "card-notes-title" :order 4}
          (str (count notes) " Notes")))

      ($ Card.Section {:inheritPadding true :py "sm"}
        ($ Grid
          ($ Grid.Col {:span 12}
            (if (seq notes)
              (map #(dom/div (str %)) notes)
              ($ Center
                ($ Text "No notes"))))))

      ($ Card.Section {:inheritPadding true :py "sm"}
        (if current-user
          ($ Grid
            ($ Grid.Col {:span 12}
              ($ markdown-editor {:text new-note
                                  :set-text set-new-note
                                  :placeholder "Leave a note"}))
            ($ Grid.Col {:span 12}
              ($ Group {:justify "flex-end" :gap "xs"}
                ($ Button {:variant "light" :color "red"} "Cancel")
                ($ Button {:variant "filled" :color "teal"} "Save"))))

          ($ Group {:justify "flex-end"}
            ($ Text {:size "sm"} "Log in to add a note")))))))

(defnc definition-detail []
  (let [{docs-state :state
         docs-error :error
         docs-value :value
         docs-loading? :loading?} (use-flex definition-docs-results)
        {social-error :error
         social-value :value
         social-loading? :loading?} (use-flex definition-social-results)
        {:keys [project namespace definition]} docs-value
        project-id (:id project)
        namespace-id (:id namespace)]

    (prn social-error social-value social-loading?)

    ($ Container {:size "md"}
      ($ LoadingOverlay {:visible docs-loading? :zIndex 1000
                         :overlayProps #js {:radius "sm" :blur 2}})

      (if (= docs-state :error)
        ($ Alert {:variant "light" :color "red"
                  :radius "md" :title "Error"
                  :icon ($ IconInfoCircle)}
          (str docs-error))

        (dom/div
          ($ breadcrumbs {:items [{:id "projects" :href "/projects" :title "Projects"}
                                  {:id project-id :href (str "/" project-id) :title project-id}
                                  {:id namespace-id :href (str "/" namespace-id) :title (:name namespace)}
                                  {:id (:id definition) :title (:name definition)}]})

          ($ Space {:h "lg"})
          ($ card-definition {:& definition})

          ($ Space {:h "lg"})
          (if false ;social-loading?
            ($ card-loading-social)
            ($ card-notes {:notes [] :current-user true :definition definition}))

          ($ back-to-top))))))
