(ns codes.clj.docs.frontend.core
  (:require ["@mantine/core" :refer [MantineProvider SimpleGrid]]
            ["react-dom/client" :as rdom]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [helix.core :refer [$]]
            [helix.dom :as dom]))

(defnc app []
  ($ MantineProvider
     ($ SimpleGrid {:cols 3}
        (dom/div "1")
        (dom/div "2")
        (dom/div "3")
        (dom/div "4")
        (dom/div "5")
        (dom/div "Helix + Mantine"))))

(defonce root
  (rdom/createRoot (js/document.getElementById "app")))

(defn render []
  (.render root ($ app)))

(defn ^:export init []
  (render))
