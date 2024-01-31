(ns codes.clj.docs.frontend.core
  (:require ["@mantine/core" :refer [MantineProvider]]
            ["react-dom/client" :as rdom]
            [codes.clj.docs.frontend.config :refer [theme]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.panels.home.view :refer [home]]
            [codes.clj.docs.frontend.panels.shell.view :refer [app-shell]]
            [helix.core :refer [$]]))

(defnc app []
  ($ MantineProvider {:theme theme}
     ($ app-shell home)))

(defonce root
  (rdom/createRoot (js/document.getElementById "app")))

(defn render []
  (.render root ($ app)))

(defn ^:export init []
  (render))
