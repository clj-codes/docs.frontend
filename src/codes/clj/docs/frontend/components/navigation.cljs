(ns codes.clj.docs.frontend.components.navigation
  (:require ["@mantine/core" :refer [Anchor Breadcrumbs Text]]
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
