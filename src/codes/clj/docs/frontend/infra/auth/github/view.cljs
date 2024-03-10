(ns codes.clj.docs.frontend.infra.auth.github.view
  (:require ["@mantine/core" :refer [Alert Anchor Container LoadingOverlay
                                     Space Text Title]]
            ["@tabler/icons-react" :refer [IconInfoCircle]]
            [codes.clj.docs.frontend.infra.auth.github.state :as github.state]
            [codes.clj.docs.frontend.infra.auth.state :as auth.state]
            [codes.clj.docs.frontend.infra.flex.hook :refer [use-flex]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.infra.routes.adapters :as routes.adapters]
            [codes.clj.docs.frontend.infra.routes.state :as routes.state]
            [helix.core :refer [$]]
            [helix.hooks :as hooks]
            [reitit.frontend.easy :as rfe]))

(defnc page []
  (let [{:keys [router current-route]} (use-flex routes.state/routes-db)
        {:keys [code page error error_description error_uri]} (:query-params current-route)
        {:keys [route path-params query-params]} (routes.adapters/href->route
                                                  page
                                                  router)
        {user-error :error user :value} (use-flex auth.state/user)]

    (hooks/use-effect
      [code]
      (when code
        (github.state/login! code)))

    (hooks/use-effect
      [user]
      (when user
        (rfe/push-state route path-params query-params)))

    ($ Container {:size "md"}
      ($ Space {:h "xl"})
      (cond
        error ($ Alert {:variant "light" :color "red"
                        :radius "md" :title "Error"
                        :icon ($ IconInfoCircle)}
                ($ Title {:order 4} error)
                ($ Text error_description)
                ($ Anchor {:href error_uri} error_uri))
        user-error ($ Alert {:variant "light" :color "red"
                             :radius "md" :title "Error"
                             :icon ($ IconInfoCircle)}
                     ($ Title {:order 4} "Server Error")
                     ($ Text user-error))
        :else ($ LoadingOverlay {:loaderProps #js {:size "xl"}
                                 :visible true
                                 :zIndex 1000
                                 :overlayProps #js {:radius "sm" :blur 2}})))))
