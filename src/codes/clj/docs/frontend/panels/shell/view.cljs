(ns codes.clj.docs.frontend.panels.shell.view
  (:require ["@mantine/core" :refer [AppShell]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.infra.system.state :as system.state]
            [codes.clj.docs.frontend.panels.shell.components :as shell.components]
            [helix.core :refer [$]]))

(def header-links
  [{:label "Projects"
    :href "/projects"}])

(def login-link
  (let [{:keys [login-url client-id]} (-> @system.state/components :config :github)]
    (str login-url "?client_id=" client-id)))

(defnc app-shell [{:keys [children]}]
  ($ AppShell {:padding "md"}
    ($ shell.components/header {:links header-links
                                :login-link login-link})
    ($ AppShell.Main ($ children))
    ($ shell.components/footer)))
