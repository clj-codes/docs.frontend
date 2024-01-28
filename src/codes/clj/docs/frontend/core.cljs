(ns codes.clj.docs.frontend.core
  (:require ["@mantine/core" :refer [AppShell Container MantineProvider
                                     SimpleGrid Switch]]
            ["react-dom/client" :as rdom]
            [codes.clj.docs.frontend.config :refer [theme]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.panels.shell.view :refer [app-shell]]
            [helix.core :refer [$]]
            [helix.dom :as dom]))

(defnc main []
  ($ AppShell.Main
     ($ Container {:size "md"}
        ($ SimpleGrid {:cols 3}
           (dom/div  "1")
           (dom/div "2")
           (dom/div "3")
           (dom/div "4")
           ($ Switch {:defaultChecked true :label "I agree to sell my privacy "})
           (dom/div "Helix + Mantine")))))

(defnc app []
  ($ MantineProvider {:theme theme}
     ($ app-shell main)))

(defonce root
  (rdom/createRoot (js/document.getElementById "app")))

(defn render []
  (.render root ($ app)))

(defn ^:export init []
  (render))
