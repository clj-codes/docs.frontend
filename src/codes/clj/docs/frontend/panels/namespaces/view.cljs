(ns codes.clj.docs.frontend.panels.namespaces.view
  (:require ["@mantine/core" :refer [Code Container LoadingOverlay Title]]
            [codes.clj.docs.frontend.infra.flex.hook :refer [use-flex]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.panels.namespaces.state :refer [namespaces-response]]
            [helix.core :refer [$]]))

;; TODO
(defnc org-projects []
  (let [{:keys [value loading?]} (use-flex namespaces-response)]
    ($ Container {:size "md"}
      ($ LoadingOverlay {:visible loading? :zIndex 1000 :overlayProps #js {:radius "sm" :blur 2}})
      ($ Title {:order 1} "Namespaces ...")
      ($ Code {:block true} (str value)))))
