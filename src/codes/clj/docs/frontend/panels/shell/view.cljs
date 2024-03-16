(ns codes.clj.docs.frontend.panels.shell.view
  (:require ["@mantine/core" :refer [AppShell]]
            [codes.clj.docs.frontend.infra.auth.state :as auth.state]
            [codes.clj.docs.frontend.infra.flex.hook :refer [use-flex]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.infra.system.state :as system.state]
            [codes.clj.docs.frontend.panels.shell.adapters :as shell.adapters]
            [codes.clj.docs.frontend.panels.shell.components :as shell.components]
            [helix.core :refer [$]]))

(def header-links
  [{:label "Projects"
    :href "/projects"}])

(defnc app-shell [{:keys [children path]}]
  (let [config-github (-> @system.state/components :config :github)
        login-link (shell.adapters/path->github-link path config-github)
        user (use-flex auth.state/user-signal)
        logoff-fn #(auth.state/user assoc :value nil)]
    ($ AppShell
      ($ shell.components/header {:user user
                                  :logoff logoff-fn
                                  :links header-links
                                  :login-link login-link})
      ($ AppShell.Main ($ children))
      ($ shell.components/footer))))
