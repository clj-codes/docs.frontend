(ns codes.clj.docs.frontend.panels.definition.view.examples
  (:require ["@mantine/core" :refer [Anchor Avatar Button Card Center Grid
                                     Group Text Title Tooltip]]
            ["@tabler/icons-react" :refer [IconInfoCircle]]
            [codes.clj.docs.frontend.components.markdown :refer [code-editor
                                                                 code-viewer]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.panels.definition.state.examples :as state.examples]
            [helix.core :refer [$]]
            [helix.hooks :as hooks]))

; TODO make editor and reuse code between note and example
(defnc editor-example [{:keys [py on-save on-cancel example definition-id]}]
  (let [[example-body set-example-body] (hooks/use-state (if example (:body example) ""))]
    ($ Grid {:data-testid "editor-example" :py py :align "center"}
      ($ Grid.Col {:span 12}
        ($ code-editor {:text example-body
                        :set-text set-example-body
                        :placeholder "Leave a example"}))

      ($ Grid.Col {:span #js {:base 12 :md 8}}
        ($ Group {:gap "xs"}
          ($ IconInfoCircle {:style #js {:width "1.0rem" :height "1.0rem"}})
          ($ Text {:size "xs"} "Add your snippet of clojure.")))

      ($ Grid.Col {:span #js {:base 12 :md 4}}
        ($ Group {:justify "flex-end" :gap "xs"}
          ($ Button {:id "editor-example-cancel-btn"
                     :data-testid "editor-example-cancel-btn"
                     :onClick #(do (if example
                                     (set-example-body (:body example))
                                     (set-example-body ""))
                                   (on-cancel))
                     :variant "light" :color "red"} "Cancel")
          ($ Button {:id "editor-example-save-btn"
                     :data-testid "editor-example-save-btn"
                     :disabled (zero? (count example-body))
                     :onClick #(do (if example
                                     (set-example-body (:body example))
                                     (set-example-body ""))
                                   (on-save
                                    (assoc example
                                           :definition-id definition-id
                                           :body example-body)))
                     :variant "filled" :color "teal"} "Save"))))))

; TODO tests
(defnc card-example [{:keys [example user]}]
  (let [{:keys [example-id body author created-at definition-id]} example
        [show-example-editor set-show-example-editor] (hooks/use-state false)
        is-example-author? (= (-> user :author :author-id) (:author-id author))]
    ($ Card {:id (str "card-example-" example-id)
             :className "card-example"
             :withBorder true
             :shadow "sm"
             :mb "sm"}

      ($ Card.Section {:withBorder true :inheritPadding true :py "sm"}
        ($ Group {:gap "xs"}
          ($ Tooltip {:label (:login author) :withArrow true}
            ($ Avatar {:size "sm" :src (:avatar-url author)}))
          ($ Text {:size "xs"} (.toGMTString created-at))

          (when is-example-author?
            ($ Group {:className "author-edit-delete-example"
                      :gap "xs"}
              ($ Anchor {:className "example-author-edit-button"
                         :id (str "example-author-edit-button-" example-id)
                         :data-testid (str "example-author-edit-button-" example-id)
                         :onClick #(set-show-example-editor true)
                         :size "xs"} "edit")))))

      ($ Card.Section {:inheritPadding true :py 0}
        ($ Grid
          ($ Grid.Col {:span 12}
            (if show-example-editor
              ($ editor-example {:on-save (fn [example]
                                            (do (set-show-example-editor false)
                                                (state.examples/edit! example author)))
                                 :on-cancel #(set-show-example-editor false)
                                 :example example
                                 :definition-id definition-id
                                 :py "sm"})
              ($ code-viewer {:language "clojure"} body))))))))

; TODO tests
(defnc card-examples [{:keys [definition examples user]}]
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
                                     :user user})
                   examples)
              ($ Center
                ($ Text "No examples"))))))

      ($ Card.Section {:inheritPadding true :pb "sm"}
        (if user
          (if show-new-example-editor
            ($ editor-example {:on-save (fn [example author]
                                          (do (set-new-example-show-editor false)
                                              (state.examples/new! example author)))
                               :on-cancel #(set-new-example-show-editor false)
                               :definition-id (:id definition)})
            ($ Group {:data-testid "add-example-logged"
                      :justify "flex-end"}
              ($ Anchor {:data-testid "add-example-btn"
                         :onClick #(set-new-example-show-editor true)
                         :size "sm"} "Add a example")))
          ($ Group {:data-testid "add-example-logout"
                    :justify "flex-end"}
            ($ Text {:size "sm"} "Log in to add a example")))))))
