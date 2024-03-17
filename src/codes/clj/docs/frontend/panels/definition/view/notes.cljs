(ns codes.clj.docs.frontend.panels.definition.view.notes
  (:require ["@mantine/core" :refer [Anchor Avatar Card Center Grid Group Text
                                     Title Tooltip]]
            ["@tabler/icons-react" :refer [IconInfoCircle]]
            [codes.clj.docs.frontend.components.markdown :refer [markdown-viewer
                                                                 previewer-markdown]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.panels.definition.state.notes :as state.notes]
            [codes.clj.docs.frontend.panels.definition.view.editor :refer [editor-base]]
            [helix.core :refer [$]]
            [helix.hooks :as hooks]))

(defnc editor-note [{:keys [py on-save on-cancel note]}]
  ($ editor-base {:py py
                  :on-save on-save
                  :on-cancel on-cancel
                  :body (str (:body note))
                  :placeholder "Leave a note"
                  :description ($ Group {:gap "xs"}
                                 ($ IconInfoCircle {:style #js {:width "1.0rem" :height "1.0rem"}})
                                 ($ Text {:size "xs"}
                                   ($ Anchor {:href "https://github.github.com/gfm/"}
                                     "GFM Markdown") " is supported."))
                  :previewer previewer-markdown}))

(defnc card-note [{:keys [note user set-delete-modal-fn]}]
  (let [{:keys [note-id body author created-at definition-id]} note
        [show-note-editor set-show-note-editor] (hooks/use-state false)
        is-note-author? (= (-> user :author :author-id) (:author-id author))]
    ($ Card {:id (str "card-note-" note-id)
             :className "card-note"
             :withBorder true
             :shadow "sm"
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
              ($ editor-note {:py "sm"
                              :note note
                              :on-cancel #(set-show-note-editor false)
                              :on-save (fn [body]
                                         (do (set-show-note-editor false)
                                             (state.notes/edit! (assoc note
                                                                       :definition-id definition-id
                                                                       :body body))))})
              ($ markdown-viewer body))))))))

(defnc card-notes [{:keys [definition notes user set-delete-modal-fn]}]
  (let [[show-new-note-editor set-new-note-show-editor] (hooks/use-state false)]
    ($ Card {:id "card-notes"
             :key "card-notes"
             :data-testid "card-notes"
             :withBorder true
             :shadow "sm"}

      ($ Card.Section {:withBorder true :inheritPadding true :py "sm"}
        ($ Title {:id "card-notes-title" :order 4}
          (str (count notes) " Notes")))

      ($ Card.Section {:inheritPadding true
                       :p "sm"}
        ($ Grid {:py 0}
          ($ Grid.Col {:span 12}
            (if (seq notes)
              (map #($ card-note {:key (:note-id %)
                                  :note %
                                  :user user
                                  :set-delete-modal-fn set-delete-modal-fn})
                   notes)
              ($ Center
                ($ Text "No notes"))))))

      ($ Card.Section {:inheritPadding true :pb "sm"}
        (if user
          (if show-new-note-editor
            ($ editor-note {:note nil
                            :on-cancel #(set-new-note-show-editor false)
                            :on-save (fn [body]
                                       (do (set-new-note-show-editor false)
                                           (state.notes/new! {:definition-id (:id definition)
                                                              :body body})))})
            ($ Group {:data-testid "add-note-logged"
                      :justify "flex-end"}
              ($ Anchor {:data-testid "add-note-btn"
                         :onClick #(set-new-note-show-editor true)
                         :size "sm"} "Add a Note")))
          ($ Group {:data-testid "add-note-logout"
                    :justify "flex-end"}
            ($ Text {:size "sm"} "Log in to add a note")))))))
