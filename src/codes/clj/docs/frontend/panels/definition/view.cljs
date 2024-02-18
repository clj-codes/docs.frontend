(ns codes.clj.docs.frontend.panels.definition.view
  (:refer-clojure :exclude [namespace])
  (:require ["@mantine/core" :refer [Container LoadingOverlay Space]]
            [codes.clj.docs.frontend.components.navigation :refer [back-to-top
                                                                   breadcrumbs]]
            [codes.clj.docs.frontend.infra.flex.hook :refer [use-flex]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.panels.definition.state :refer [definition-response]]
            [helix.core :refer [$]]))

;; TODO
(defnc definition-detail []
  (let [{:keys [value loading?]} (use-flex definition-response)
        {:keys [project namespace definition]} value
        project-id (:id project)
        namespace-id (:id namespace)
        {:keys [id name]} definition]
    ($ Container {:size "md"}
      ($ LoadingOverlay {:visible loading? :zIndex 1000
                         :overlayProps #js {:radius "sm" :blur 2}})

      ($ breadcrumbs {:items [{:id "projects" :href "/projects" :title "Projects"}
                              {:id project-id :href (str "/" project-id) :title project-id}
                              {:id namespace-id :href (str "/" namespace-id) :title namespace-id}
                              {:id id :title name}]})

      ($ Space {:h "lg"})
      (str value)

      ($ back-to-top))))
