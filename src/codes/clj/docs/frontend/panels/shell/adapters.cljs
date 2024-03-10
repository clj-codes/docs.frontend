(ns codes.clj.docs.frontend.panels.shell.adapters)

(defn path->github-link [current config-github]
  (let [{:keys [login-url client-id redirect-uri]} config-github]
    (str login-url
         "?client_id=" client-id
         "&redirect_uri=" (str redirect-uri "?page=" current))))
