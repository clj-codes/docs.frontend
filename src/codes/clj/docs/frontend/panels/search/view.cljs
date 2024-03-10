(ns codes.clj.docs.frontend.panels.search.view
  (:require ["@mantine/core" :refer [Alert Container LoadingOverlay]]
            ["@mantine/hooks" :refer [useDebouncedValue]]
            ["@mantine/spotlight" :refer [spotlight]]
            ["@tabler/icons-react" :refer [IconInfoCircle]]
            [codes.clj.docs.frontend.components.navigation :refer [back-to-top]]
            [codes.clj.docs.frontend.infra.flex.hook :refer [use-flex]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.infra.routes.state :refer [routes-db]]
            [codes.clj.docs.frontend.panels.search.components :refer [page-results-section
                                                                      spotlight-modal
                                                                      spotlight-search-button]]
            [codes.clj.docs.frontend.panels.search.state :refer [page-results
                                                                 search-fetch
                                                                 spotlight-results]]
            [helix.core :refer [$]]
            [helix.dom :as dom]
            [helix.hooks :as hooks]
            [react :as react]
            [reitit.frontend.easy :as rfe]))

;; TODO test
(defnc search-spotlight []
  (let [query-limit 30
        [query set-query] (hooks/use-state nil)
        [debounced] (useDebouncedValue query 500)
        {:keys [value loading?]} (use-flex spotlight-results)
        open-fn (.-open spotlight)
        close-fn (.-close spotlight)]

    (hooks/use-effect
      [debounced]
      (when debounced
        (search-fetch spotlight-results (or debounced "") query-limit)))

    (dom/div
      ($ spotlight-modal {:key "spotlight-modal"
                          :query-limit query-limit
                          :debounced-query debounced
                          :loading? loading?
                          :close-fn close-fn
                          :items value
                          :query query
                          :onQueryChange set-query
                          :scrollable true
                          :size "xl"
                          :maxHeight "400rem"})
      ($ spotlight-search-button {:on-click open-fn}))))

;; TODO test
(defnc search-page []
  (let [{:keys [current-route]} (use-flex routes-db)
        query (-> current-route :query-params :q)
        [deb-query set-deb-query] (react/useState query)
        [debounced] (useDebouncedValue deb-query 500)
        {:keys [value loading? error]} (use-flex page-results)]

    (hooks/use-effect
      [query]
      (when query
        (set-deb-query query)))

    (hooks/use-effect
      [debounced]
      (when debounced
        (rfe/set-query {:q deb-query})))

    ($ Container {:size "md"}
      (if loading?

        ($ LoadingOverlay {:visible loading? :zIndex 1000
                           :overlayProps #js {:radius "sm" :blur 2}})

        (if error
          ($ Alert {:variant "light" :color "red"
                    :radius "md" :title "Error"
                    :icon ($ IconInfoCircle)}
            (str error))

          (dom/div
            ($ page-results-section {:query query
                                     :debounced deb-query
                                     :set-debounced set-deb-query
                                     :items value})

            ($ back-to-top)))))))
