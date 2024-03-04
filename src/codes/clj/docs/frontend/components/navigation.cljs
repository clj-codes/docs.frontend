(ns codes.clj.docs.frontend.components.navigation
  (:require ["@mantine/core" :refer [ActionIcon Affix Anchor Breadcrumbs Text
                                     Transition]]
            ["@mantine/hooks" :refer [useWindowScroll]]
            ["@tabler/icons-react" :refer [IconArrowUp]]
            [codes.clj.docs.frontend.adapters.url :refer [href->safe-href]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [helix.core :refer [$]]))

(defnc safe-anchor [{:keys [href] :as props}]
  (let [safe-href (href->safe-href href)]
    ($ Anchor {:& (assoc props :href safe-href)})))

(defnc breadcrumbs [{:keys [items]}]
  (let [links (map (fn [{:keys [id href title]}]
                     (if href
                       ($ safe-anchor
                         {:key id :id (str "a-" id)
                          :href href
                          :className "components-navigation-breadcrumbs"} title)
                       ($ Text
                         {:key id :id (str "t-" id)
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
    ($ Affix {:position #js {:bottom "6%" :right "6%"}}
      ($ Transition {:transition "slide-up" :mounted (> (.-y scroll) 0)}
        (fn [transition-style]
          ($ ActionIcon {:style transition-style :onClick #(scrollTo #js {:y 0})}
            ($ IconArrowUp)))))))
