(ns codes.clj.docs.frontend.panels.definition.view.editor
  (:require ["@mantine/core" :refer [Button Grid Group]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [helix.core :refer [$]]
            [helix.hooks :as hooks]))

(defnc editor-base [{:keys [py
                            on-save on-cancel
                            body
                            description
                            previewer
                            placeholder]}]
  (let [[text-body set-text-body] (hooks/use-state body)]
    ($ Grid {:data-testid "editor-base" :py py :align "center"}
      ($ Grid.Col {:span 12}
        ($ previewer {:text text-body
                      :set-text set-text-body
                      :placeholder placeholder}))

      ($ Grid.Col {:span #js {:base 12 :md 8}}
        description)

      ($ Grid.Col {:span #js {:base 12 :md 4}}
        ($ Group {:justify "flex-end" :gap "xs"}
          ($ Button {:id "editor-base-cancel-btn"
                     :data-testid "editor-base-cancel-btn"
                     :onClick #(do (set-text-body body)
                                   (on-cancel))
                     :variant "light" :color "red"} "Cancel")
          ($ Button {:id "editor-base-save-btn"
                     :data-testid "editor-base-save-btn"
                     :disabled (zero? (count text-body))
                     :onClick #(do (set-text-body body)
                                   (on-save text-body))
                     :variant "filled" :color "teal"} "Save"))))))
