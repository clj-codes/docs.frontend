(ns codes.clj.docs.frontend.infra.auth.github.view
  (:require ["@mantine/core" :refer [Loader]]
            [codes.clj.docs.frontend.infra.auth.github.state :as github.state]
            [codes.clj.docs.frontend.infra.auth.state :as auth.state]
            [codes.clj.docs.frontend.infra.flex.hook :refer [use-flex]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.infra.routes.adapters :as routes.adapters]
            [codes.clj.docs.frontend.infra.routes.state :as routes.state]
            [helix.core :refer [$]]
            [helix.hooks :as hooks]
            [reitit.frontend.easy :as rfe]))

; TODO: tests
(defnc page []
  (let [{:keys [router current-route]} (use-flex routes.state/routes-db)
        {:keys [code page]} (:query-params current-route)
        {:keys [route path-params query-params]} (routes.adapters/href->route
                                                  page
                                                  router)
        user (use-flex auth.state/user-signal)]

    (hooks/use-effect
      [code]
      (when code
        (github.state/login! code)))

    (hooks/use-effect
      [user]
      (when user
        (rfe/push-state route path-params query-params)))

    ($ Loader {:color "moonstone"})))
