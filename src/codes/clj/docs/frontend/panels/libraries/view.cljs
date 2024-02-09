(ns codes.clj.docs.frontend.panels.libraries.view
  (:require ["@mantine/core" :refer [Container Grid Title]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [helix.core :refer [$]]
            [helix.dom :as dom]))

;; TODO 
(defnc libraries []
  ($ Container {:size "md"}
     ($ Grid {:id "libraries"}
        ($ Grid.Col {:span 12}
           (dom/section
             ($ Title {:order 2} "Currently Imported Libraries"))))))
