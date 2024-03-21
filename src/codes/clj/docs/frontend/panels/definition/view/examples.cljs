(ns codes.clj.docs.frontend.panels.definition.view.examples
  (:require ["@mantine/core" :refer [Anchor Avatar Card Center Grid Group Text
                                     Title Tooltip]]
            ["@tabler/icons-react" :refer [IconInfoCircle]]
            [codes.clj.docs.frontend.components.markdown :refer [code-viewer
                                                                 previewer-code]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.panels.definition.state.examples :as state.examples]
            [codes.clj.docs.frontend.panels.definition.view.editor :refer [editor-base]]
            [helix.core :refer [$]]
            [helix.hooks :as hooks]))

(defnc editor-example [{:keys [py on-save on-cancel example]}]
  ($ editor-base {:py py
                  :on-save on-save
                  :on-cancel on-cancel
                  :body (str (:body example))
                  :placeholder "Leave a example"
                  :description ($ Group {:gap "xs"}
                                 ($ IconInfoCircle {:style #js {:width "1.0rem" :height "1.0rem"}})
                                 ($ Text {:size "xs"} "Add your snippet of clojure."))
                  :previewer previewer-code}))

(defnc avatar-editors [{:keys [editors]}]
  (let [shown-editors 3]
    (if (>= shown-editors (count editors))
      ($ Avatar.Group
        (map
          #($ Tooltip {:key (str (:login %) (:edited-at %))
                       :label (str (:login %)
                                   " reviewed at "
                                   (.toGMTString (:edited-at %)))
                       :withArrow true}
             ($ Avatar {:size "sm" :src (:avatar-url %)}))
          editors))
      ($ Avatar.Group
        ($ Tooltip {:key "older-edits"
                    :label "Older revisions"
                    :withArrow true}
          ($ Avatar {:size "sm"} "+"))
        (map
          #($ Tooltip {:key (str (:login %) (:edited-at %))
                       :label (str (:login %)
                                   " reviewed at "
                                   (.toGMTString (:edited-at %)))
                       :withArrow true}
             ($ Avatar {:size "sm" :src (:avatar-url %)}))
          (take-last shown-editors editors))))))

(defnc card-example [{:keys [example user set-delete-modal-fn]}]
  (let [{:keys [example-id body author created-at definition-id editors]} example
        [show-example-editor set-show-example-editor] (hooks/use-state false)
        is-example-author? (= (-> user :author :author-id) (:author-id author))]
    ($ Card {:id (str "card-example-" example-id)
             :className "card-example"
             :withBorder true
             :shadow "sm"
             :mb "sm"}

      ($ Card.Section {:withBorder true :inheritPadding true :py "sm"}
        ($ Group {:gap "xs"}
          ($ avatar-editors {:editors editors})

          ($ Text {:size "xs"} (.toGMTString created-at))

          (when user
            ($ Group {:className "author-edit-delete-example"
                      :gap "xs"}
              ($ Anchor {:className "example-author-edit-button"
                         :id (str "example-author-edit-button-" example-id)
                         :data-testid (str "example-author-edit-button-" example-id)
                         :onClick #(set-show-example-editor true)
                         :size "xs"} "edit")
              (when is-example-author?
                ($ Anchor {:className "example-author-delete-button"
                           :id (str "example-author-delete-button-" example-id)
                           :data-testid (str "example-author-delete-button-" example-id)
                           :onClick #(set-delete-modal-fn
                                      {:fn (fn []
                                             (state.examples/delete! example))})
                           :size "xs"} "delete"))))))

      ($ Card.Section {:inheritPadding true :py 0}
        ($ Grid
          ($ Grid.Col {:span 12}
            (if show-example-editor
              ($ editor-example {:py "sm"
                                 :example example
                                 :on-cancel #(set-show-example-editor false)
                                 :on-save (fn [body]
                                            (do (set-show-example-editor false)
                                                (state.examples/edit! (assoc example
                                                                             :definition-id definition-id
                                                                             :body body))))})
              ($ code-viewer {:language "clojure"} body))))))))

(defnc card-examples [{:keys [definition examples user set-delete-modal-fn]}]
  (let [[show-new-example-editor set-new-example-show-editor] (hooks/use-state false)]
    ($ Card {:id "card-examples"
             :key "card-examples"
             :data-testid "card-examples"
             :withBorder true
             :shadow "sm"}

      ($ Card.Section {:withBorder true :inheritPadding true :py "sm"}
        ($ Title {:id "card-examples-title" :order 4}
          (str (count examples) " examples")))

      ($ Card.Section {:inheritPadding true
                       :p "sm"}
        ($ Grid {:py 0}
          ($ Grid.Col {:span 12}
            (if (seq examples)
              (map #($ card-example {:key (:example-id %)
                                     :example %
                                     :user user
                                     :set-delete-modal-fn set-delete-modal-fn})
                   examples)
              ($ Center
                ($ Text "No examples"))))))

      ($ Card.Section {:inheritPadding true :pb "sm"}
        (if user
          (if show-new-example-editor
            ($ editor-example {:example nil
                               :on-cancel #(set-new-example-show-editor false)
                               :on-save (fn [body]
                                          (do (set-new-example-show-editor false)
                                              (state.examples/new! {:definition-id (:id definition)
                                                                    :body body})))})
            ($ Group {:data-testid "add-example-logged"
                      :justify "flex-end"}
              ($ Anchor {:data-testid "add-example-btn"
                         :onClick #(set-new-example-show-editor true)
                         :size "sm"} "Add a example")))
          ($ Group {:data-testid "add-example-logout"
                    :justify "flex-end"}
            ($ Text {:size "sm"} "Log in to add a example")))))))
