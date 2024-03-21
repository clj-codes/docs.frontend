(ns codes.clj.docs.frontend.panels.definition.view
  (:refer-clojure :exclude [namespace])
  (:require ["@mantine/core" :refer [Alert Anchor Badge Button Card Code
                                     Container Grid Group LoadingOverlay Modal
                                     Skeleton Space Text Title]]
            ["@tabler/icons-react" :refer [IconInfoCircle]]
            [clojure.string :as str]
            [codes.clj.docs.frontend.components.navigation :refer [back-to-top
                                                                   breadcrumbs]]
            [codes.clj.docs.frontend.infra.auth.state :as auth.state]
            [codes.clj.docs.frontend.infra.flex.hook :refer [use-flex]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.panels.definition.state :refer [definition-docs-results
                                                                     definition-social-results]]
            [codes.clj.docs.frontend.panels.definition.view.examples :refer [card-examples]]
            [codes.clj.docs.frontend.panels.definition.view.notes :refer [card-notes]]
            [codes.clj.docs.frontend.panels.definition.view.see-alsos :refer [card-see-alsos]]
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
           :shadow "sm"}

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
           :shadow "sm"}
    ($ Group {:mb "md"}
      ($ Skeleton {:height 30 :circle true})
      ($ Skeleton {:height 100 :radius "md"}))))

(defnc delete-alert [{:keys [delete-fn on-close-fn]}]
  ($ Modal {:id "delete-alert-modal"
            :opened delete-fn :centered true
            :onClose on-close-fn
            :title "Are you sure?" :size "xs"}
    ($ Group {:justify "flex-end" :gap "xs"}
      ($ Button {:data-testid "definition-delete-alert-no-btn"
                 :onClick on-close-fn
                 :variant "light" :color "red"} "No")
      ($ Button {:data-testid "definition-delete-alert-yes-btn"
                 :onClick #(do ((:fn delete-fn))
                               (on-close-fn))
                 :variant "filled" :color "teal"} "Yes"))))

(defnc definition-detail []
  (let [{docs-state :state
         docs-error :error
         docs-value :value
         docs-loading? :loading?} (use-flex definition-docs-results)
        ; TODO social error
        {_social-error :error
         social-value :value
         social-loading? :loading?} (use-flex definition-social-results)
        {:keys [notes examples see-alsos]} social-value
        {:keys [project namespace definition]} docs-value
        user (use-flex auth.state/user-signal)
        project-id (:id project)
        namespace-id (:id namespace)
        [delete-modal-fn set-delete-modal-fn] (hooks/use-state nil)]

    ($ Container {:p "sm"}

      ($ LoadingOverlay {:id "loading-overlay"
                         :visible docs-loading? :zIndex 1000
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

          ($ card-definition {:& definition})

          ($ Space {:h "lg"})
          (if social-loading?
            ($ card-loading-social)
            (dom/div
              ($ card-examples {:examples (sort-by :created-at examples)
                                :user user
                                :definition definition
                                :set-delete-modal-fn set-delete-modal-fn})

              ($ Space {:h "lg"})

              ($ card-see-alsos {:see-alsos (sort-by :created-at see-alsos)
                                 :user user
                                 :definition definition
                                 :set-delete-modal-fn set-delete-modal-fn})

              ($ Space {:h "lg"})

              ($ card-notes {:notes (sort-by :created-at notes)
                             :user user
                             :definition definition
                             :set-delete-modal-fn set-delete-modal-fn})))

          ($ back-to-top)))

      ($ delete-alert {:delete-fn delete-modal-fn
                       :on-close-fn #(set-delete-modal-fn nil)}))))
