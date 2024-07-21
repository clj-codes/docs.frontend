(ns codes.clj.docs.frontend.panels.definition.view.author
  (:require ["@mantine/core" :refer [Anchor Avatar]]
            ["react" :as react]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [helix.core :refer [$]]
            [helix.dom :as dom]))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(def forward-ref react/forwardRef)

(defnc avatar
  "Custom components that are rendered inside Tooltip are required to support ref prop
  https://mantine.dev/core/tooltip/#required-ref-prop"
  {:wrap [(forward-ref)]}
  [{:keys [author id children]} ref]
  (let [{:keys [login account-source avatar-url]} author]
    (dom/div {:ref ref}
      ($ Anchor {:key (str "avatar" id)
                 :href (str "/author/" login "/" account-source)}
        ($ Avatar {:size "sm" :src avatar-url}))
      children)))
