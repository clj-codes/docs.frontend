(ns codes.clj.docs.frontend.panels.shell.view
  (:require ["@mantine/core" :refer [AppShell]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.panels.shell.components :as shell.components]
            [helix.core :refer [$]]))

(def header-links
  [{:label "Projects"
    :href "/projects"}])

(defnc app-shell [{:keys [children]}]
  ($ AppShell {:padding "md"
               :header #js {:height 60}}
    ($ shell.components/header {:links header-links})
    ($ AppShell.Main ($ children))
    ($ shell.components/footer)))
