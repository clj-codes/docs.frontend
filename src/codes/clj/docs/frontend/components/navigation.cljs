(ns codes.clj.docs.frontend.components.navigation
  (:require ["@mantine/core" :refer [ActionIcon Affix Anchor Breadcrumbs Text
                                     Transition]]
            ["@mantine/hooks" :refer [useWindowScroll]]
            ["@tabler/icons-react" :refer [IconArrowUp]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [helix.core :refer [$]]))

(defnc breadcrumbs [{:keys [items]}]
  (let [links (mapv (fn [{:keys [id href title]}]
                      (if href
                        ($ Anchor {:key id :id (str "a-" id)
                                   :href href
                                   :className "components-navigation-breadcrumbs"} title)
                        ($ Text {:key id :id (str "t-" id)
                                 :className "components-navigation-breadcrumbs"} title)))
                    items)]
    ($ Breadcrumbs {:data-testid "breadcrumbs"
                    :visibleFrom "xs"
                    :separator "→"
                    :separatorMargin "xs"
                    :mt "0"}
      links)))

(defnc back-to-top []
  (let [[scroll scrollTo] (useWindowScroll)]
    ($ Affix {:position #js {:bottom 20 :right 20}}
      ($ Transition {:transition "slide-up" :mounted (> (.-y scroll) 0)}
        (fn [transition-style]
          ($ ActionIcon {:style transition-style :onClick #(scrollTo #js {:y 0})}
            ($ IconArrowUp)))))))
