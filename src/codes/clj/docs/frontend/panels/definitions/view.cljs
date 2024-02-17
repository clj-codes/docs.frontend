(ns codes.clj.docs.frontend.panels.definitions.view
  (:refer-clojure :exclude [namespace])
  (:require ["@mantine/core" :refer [Anchor Container Divider Grid
                                     LoadingOverlay Space Text Title]]
            [codes.clj.docs.frontend.components.documents :refer [card-namespace]]
            [codes.clj.docs.frontend.components.navigation :refer [breadcrumbs]]
            [codes.clj.docs.frontend.infra.flex.hook :refer [use-flex]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.panels.definitions.adapters :as adapters]
            [codes.clj.docs.frontend.panels.definitions.state :refer [definitions-response]]
            [helix.core :refer [$]]))

;; TODO test
(defnc namespace-definitions []
  (let [{:keys [value loading?]} (use-flex definitions-response)
        {:keys [definitions namespace project]} value
        project-id (:id project)
        namespace-name (:name namespace)
        grouped-definitions (adapters/definitions->alphabetic-grouped-list definitions)]

    ($ Container {:size "md"}

      ($ LoadingOverlay {:visible loading? :zIndex 1000
                         :overlayProps #js {:radius "sm" :blur 2}})

      ($ breadcrumbs {:items [{:id "projects" :href "/projects" :title "Projects"}
                              {:id project-id :href (str "/" project-id) :title project-id}
                              {:id (:id namespace) :title namespace-name}]})

      ($ Space {:h "lg"})

      ($ card-namespace {:list? false :key (:id namespace) :& namespace})

      ($ Space {:h "lg"})

      ($ Title {:order 3} "Definitions")

      ($ Space {:h "xl"})

      (when definitions
        ($ Grid {:data-testid "definition-cards-grid"}
          (mapv (fn [[group sub-definitions]]
                  ($ Grid.Col {:key group}
                    ($ Divider {:id group :my "xs" :size "sm"
                                :labelPosition "left"
                                :label ($ Anchor {:fz "lg" :fw 500 :c "dimmed"
                                                  :href (str "#" group)}
                                         group)})
                    (mapv
                      (fn [{:keys [id name doc]}]
                        ; TODO move to component
                        ($ Grid {:key id :gutter "xs"}
                          ($ Grid.Col {:span 3}
                            ($ Anchor {:href id}
                              ($ Text name)))
                          ($ Grid.Col {:span 9}
                            (if doc
                              ($ Text {:size "sm" :lineClamp 1} doc)
                              ($ Text {:size "sm" :c "dimmed"} "no documentation")))))
                      sub-definitions)))

            grouped-definitions))))))
