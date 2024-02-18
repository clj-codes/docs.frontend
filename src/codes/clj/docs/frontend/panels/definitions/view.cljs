(ns codes.clj.docs.frontend.panels.definitions.view
  (:refer-clojure :exclude [namespace])
  (:require ["@mantine/core" :refer [Anchor Container Divider Grid
                                     LoadingOverlay Space Text Title SimpleGrid]]
            [codes.clj.docs.frontend.components.documents :refer [card-namespace]]
            [codes.clj.docs.frontend.components.navigation :refer [back-to-top
                                                                   breadcrumbs]]
            [codes.clj.docs.frontend.infra.flex.hook :refer [use-flex]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.panels.definitions.adapters :as adapters]
            [codes.clj.docs.frontend.panels.definitions.state :refer [definitions-response]]
            [helix.core :refer [$]]
            [helix.dom :as dom]))

(defnc definition-line [{:keys [id name doc]}]
  (dom/div {:className "definition-line-row"}
    (dom/div {:className "definition-line-name-column"}
      ($ Anchor {:size "sm" :href id}
        ($ Text name)))
    (dom/div {:className "definition-line-doc-column"}
      ($ Title {:fw 450 :order 6 :lineClamp 1}
        (or doc
          ($ Text {:c "dimmed" :size "sm"}
            "no documentation"))))))

(defnc definitions-gruoped [{:keys [group sub-definitions]}]
  (dom/div
    ($ Divider {:id group :my "xs" :size "sm"
                :labelPosition "left"
                :label ($ Anchor {:fz "lg" :fw 500 :c "dimmed"
                                  :href (str "#" group)}
                         group)})

    (map
      (fn [{:keys [id] :as definition}]
        ($ definition-line {:key id :& definition}))
      sub-definitions)))

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
      ($ card-namespace {:header true :key (:id namespace) :& namespace})
      ($ Space {:h "lg"})
      ($ Title {:order 3} "Definitions")
      ($ Space {:h "xl"})
      (when definitions
        ($ Grid {:data-testid "definition-lines-grid"}
          (mapv (fn [[group sub-definitions]]
                  ($ Grid.Col {:key group}
                    ($ definitions-gruoped {:key (str "grouped-" group)
                                            :group group
                                            :sub-definitions sub-definitions})))

            grouped-definitions)))

      ($ back-to-top))))
