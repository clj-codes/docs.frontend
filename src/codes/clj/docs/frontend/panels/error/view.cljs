(ns codes.clj.docs.frontend.panels.error.view
  (:require ["@mantine/core" :refer [Container Grid Title]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [helix.core :refer [$]]
            [helix.dom :as dom]))

;; TODO 
(defnc not-found []
  ($ Container {:size "md"}
     ($ Grid {:id "not-found"}
        ($ Grid.Col {:span 12}
           (dom/section
             ($ Title {:order 2} "404 - Not Found"))))))
