(ns codes.clj.docs.frontend.panels.shell.view
  (:require ["@mantine/core" :refer [AppShell]]
            [codes.clj.docs.frontend.infra.auth.state :as auth.state]
            [codes.clj.docs.frontend.infra.flex.hook :refer [use-flex]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.infra.system.state :as system.state]
            [codes.clj.docs.frontend.panels.shell.components :as shell.components]
            [helix.core :refer [$]]))

(def header-links
  [{:label "Projects"
    :href "/projects"}])

; todo move to logic & test
(defn build-github-link [current]
  (let [{:keys [login-url client-id redirect-uri]} (-> @system.state/components
                                                       :config
                                                       :github)]
    (str login-url
         "?client_id=" client-id
         "&redirect_uri=" (str redirect-uri "?page=" current))))

(defnc app-shell [{:keys [children path]}]
  (let [login-link (build-github-link path)
        user (use-flex auth.state/user-signal)
        logoff-fn #(auth.state/user assoc :value nil)]
    ($ AppShell {:padding "md"}
      ($ shell.components/header {:user user
                                  :logoff logoff-fn
                                  :links header-links
                                  :login-link login-link})
      ($ AppShell.Main ($ children))
      ($ shell.components/footer))))
