(ns codes.clj.docs.frontend.panels.definition.view.notes
  (:require ["@mantine/core" :refer [Anchor Avatar Button Card Center Grid
                                     Group Text Title Tooltip]]
            [codes.clj.docs.frontend.components.markdown :refer [markdown
                                                                 markdown-editor]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.panels.definition.state.notes :as state.notes]
            [helix.core :refer [$]]
            [helix.hooks :as hooks]))

(defnc editor-note [{:keys [py on-save on-cancel note definition-id]}]
  (let [[note-body set-note-body] (hooks/use-state (if note (:body note) ""))]
    ($ Grid {:data-testid "editor-note" :py py}
      ($ Grid.Col {:span 12}
        ($ markdown-editor {:text note-body
                            :set-text set-note-body
                            :placeholder "Leave a note"}))
      ($ Grid.Col {:span 12}
        ($ Group {:justify "flex-end" :gap "xs"}
          ($ Button {:id "editor-note-cancel-btn"
                     :data-testid "editor-note-cancel-btn"
                     :onClick #(do (if note
                                     (set-note-body (:body note))
                                     (set-note-body ""))
                                   (on-cancel))
                     :variant "light" :color "red"} "Cancel")
          ($ Button {:id "editor-note-save-btn"
                     :data-testid "editor-note-save-btn"
                     :disabled (zero? (count note-body))
                     :onClick #(do (if note
                                     (set-note-body (:body note))
                                     (set-note-body ""))
                                   (on-save
                                    (assoc note
                                           :definition-id definition-id
                                           :body note-body)))
                     :variant "filled" :color "teal"} "Save"))))))

(defnc card-note [{:keys [children user set-delete-modal-fn]}]
  (let [{:keys [note-id body author created-at definition-id] :as note} children
        [show-note-editor set-show-note-editor] (hooks/use-state false)
        is-note-author? (= (-> user :author :author-id) (:author-id author))]
    ($ Card {:id (str "card-note-" note-id)
             :className "card-note"
             :withBorder true
             :shadow "sm"
             :padding "sm"
             :mb "sm"}

      ($ Card.Section {:withBorder true :inheritPadding true :py "sm"}
        ($ Group {:gap "xs"}
          ($ Tooltip {:label (:login author) :withArrow true}
            ($ Avatar {:size "sm" :src (:avatar-url author)}))
          ($ Text {:size "xs"} (.toGMTString created-at))

          (when is-note-author?
            ($ Group {:className "author-edit-delete-note"
                      :gap "xs"}
              ($ Anchor {:className "note-author-edit-button"
                         :id (str "note-author-edit-button-" note-id)
                         :data-testid (str "note-author-edit-button-" note-id)
                         :onClick #(set-show-note-editor true)
                         :size "xs"} "edit")
              ($ Text {:size "xs"} "/")
              ($ Anchor {:className "note-author-delete-button"
                         :id (str "note-author-delete-button-" note-id)
                         :data-testid (str "note-author-delete-button-" note-id)
                         :onClick #(set-delete-modal-fn
                                    {:fn (fn []
                                           (state.notes/delete! note))})
                         :size "xs"} "delete")))))

      ($ Card.Section {:inheritPadding true :py 0}
        ($ Grid
          ($ Grid.Col {:span 12}
            (if show-note-editor
              ($ editor-note {:on-save (fn [note]
                                         (do (set-show-note-editor false)
                                             (state.notes/edit! note author)))
                              :on-cancel #(set-show-note-editor false)
                              :note note
                              :definition-id definition-id
                              :py "sm"})
              ($ markdown body))))))))

(defnc card-notes [{:keys [definition notes user set-delete-modal-fn]}]
  (let [[show-new-note-editor set-new-note-show-editor] (hooks/use-state false)]
    ($ Card {:id "card-notes"
             :key "card-notes"
             :data-testid "card-notes"
             :withBorder true
             :shadow "sm"
             :padding "xs"}

      ($ Card.Section {:withBorder true :inheritPadding true :py "sm"}
        ($ Title {:id "card-notes-title" :order 4}
          (str (count notes) " Notes")))

      ($ Card.Section {:inheritPadding true :py "sm"}
        ($ Grid {:py 0}
          ($ Grid.Col {:span 12}
            (if (seq notes)
              (map #($ card-note {:key (:note-id %)
                                  :user user
                                  :set-delete-modal-fn set-delete-modal-fn} %)
                   notes)
              ($ Center
                ($ Text "No notes"))))))

      ($ Card.Section {:inheritPadding true :pb "sm"}
        (if user
          (if show-new-note-editor
            ($ editor-note {:on-save (fn [note author]
                                       (do (set-new-note-show-editor false)
                                           (state.notes/new! note author)))
                            :on-cancel #(set-new-note-show-editor false)
                            :definition-id (:id definition)})
            ($ Group {:data-testid "add-note-logged"
                      :justify "flex-end"}
              ($ Anchor {:data-testid "add-note-btn"
                         :onClick #(set-new-note-show-editor true)
                         :size "sm"} "Add a Note")))
          ($ Group {:data-testid "add-note-logout"
                    :justify "flex-end"}
            ($ Text {:size "sm"} "Log in to add a note")))))))
