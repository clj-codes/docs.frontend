(ns codes.clj.docs.frontend.panels.namespaces.view
  (:require ["@mantine/core" :refer [Container Grid LoadingOverlay Space Title]]
            [codes.clj.docs.frontend.components.documents :refer [card-namespace
                                                                  card-project]]
            [codes.clj.docs.frontend.components.navigation :refer [back-to-top
                                                                   breadcrumbs]]
            [codes.clj.docs.frontend.infra.flex.hook :refer [use-flex]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.panels.namespaces.state :refer [namespaces-response]]
            [helix.core :refer [$]]))

(defnc org-projects []
  (let [{:keys [value loading?]} (use-flex namespaces-response)
        {:keys [namespaces project]} value
        card-namespaces (->> namespaces
                             (sort-by :name)
                             (map (fn [{:keys [id] :as props}]
                                    ($ Grid.Col {:key id}
                                      ($ card-namespace {:& props})))))
        project-id (:id project)]

    ($ Container {:size "md"}
      ($ LoadingOverlay {:visible loading? :zIndex 1000
                         :overlayProps #js {:radius "sm" :blur 2}})

      ($ breadcrumbs {:items [{:id "projects" :href "/projects" :title "Projects"}
                              {:id project-id :title project-id}]})

      ($ Space {:h "lg"})
      ($ card-project {:header true :key project-id :& project})
      ($ Space {:h "lg"})
      ($ Title {:order 3} "Namespaces")
      ($ Space {:h "xl"})
      (when namespaces
        ($ Grid {:data-testid "namespace-cards-grid"}
          card-namespaces))

      ($ back-to-top))))
