(ns codes.clj.docs.frontend.panels.shell.view
  (:require ["@mantine/core" :refer [AppShell Container SimpleGrid Switch]]
            [codes.clj.docs.frontend.panels.shell.components :as shell.components]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [helix.core :refer [$]]
            [helix.dom :as dom]))

(defnc app-shell []
  ($ AppShell {:padding "md"
               :header #js {:height 60}}

     ($ shell.components/Header)

     ($ AppShell.Main
        ($ Container {:size "md"}
           ($ SimpleGrid {:cols 3}
              (dom/div  "1")
              (dom/div "2")
              (dom/div "3")
              (dom/div "4")
              ($ Switch {:defaultChecked true :label "I agree to sell my privacy "})

              (dom/div "Helix + Mantine"))))

     ($ shell.components/Footer)))
