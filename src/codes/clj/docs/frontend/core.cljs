(ns codes.clj.docs.frontend.core
  (:require ["@mantine/core" :refer [MantineProvider]]
            ["react-dom/client" :as rdom]
            [codes.clj.docs.frontend.config :refer [theme]]
            [codes.clj.docs.frontend.infra.flex.hook :refer [use-flex]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.infra.routes.core :refer [init-routes!
                                                               router]]
            [codes.clj.docs.frontend.infra.routes.state :refer [routes-db]]
            [codes.clj.docs.frontend.panels.error.view :refer [not-found]]
            [codes.clj.docs.frontend.panels.shell.view :refer [app-shell]]
            [helix.core :refer [$]]))

(defnc app [{:keys [_router]}]
  (let [{:keys [current-route]} (use-flex routes-db)]
    ($ MantineProvider {:theme theme}
       (if-let [view (-> current-route :data :view)]
         ($ app-shell view)
         ($ app-shell not-found)))))

(defonce root
  (rdom/createRoot (js/document.getElementById "app")))

(defn render []
  (.render root ($ app {:router router})))

(defn ^:export init []
  (init-routes!)
  (render))
