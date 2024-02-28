(ns codes.clj.docs.frontend.panels.search.view
  (:require ["@mantine/core" :refer [Alert Container FocusTrap LoadingOverlay
                                     Space Text TextInput Title]]
            ["@mantine/hooks" :refer [useDebouncedValue]]
            ["@tabler/icons-react" :refer [IconInfoCircle]]
            [codes.clj.docs.frontend.components.navigation :refer [back-to-top]]
            [codes.clj.docs.frontend.infra.flex.hook :refer [use-flex]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.infra.routes.state :refer [routes-db]]
            [codes.clj.docs.frontend.panels.search.state :refer [search-response]]
            [helix.core :refer [$]]
            [helix.dom :as dom]
            [helix.hooks :as hooks]
            [react :as react]
            [reitit.frontend.easy :as rfe]))

(defnc search []
  (let [{:keys [current-route]} (use-flex routes-db)
        query (-> current-route :query-params :q)
        [deb-query set-deb-query] (react/useState query)
        [debounced] (useDebouncedValue deb-query 300)
        {:keys [state error loading? value]} (use-flex search-response)]

    (hooks/use-effect
      [debounced]
      (rfe/set-query {:q deb-query}))

    ($ Container {:size "md"}

      (if loading?

        ($ LoadingOverlay {:visible loading? :zIndex 1000
                           :overlayProps #js {:radius "sm" :blur 2}})

        (if (= state :error)
          ($ Alert {:variant "light" :color "red"
                    :radius "md" :title "Error"
                    :icon ($ IconInfoCircle)}
            (str error))

          (dom/div

            (dom/section
              ($ Title {:order 1}
                "Search results for: "
                ($ Text {:component "span" :inherit true :variant "gradient"
                         :gradient #js {:from "cyan" :to "green" :deg 90}}
                  query)))

            ($ Space {:h "lg"})

            ($ FocusTrap
              ($ TextInput {:placeholder "Type your search here"
                            :size "lg"
                            :value deb-query
                            ;; TODO debounce
                            :onChange #(set-deb-query (-> % .-currentTarget .-value))
                            :autofocus true}))

            ($ Space {:h "lg"})

            (dom/div
              (map (fn [item] (dom/div (str item)))
                value))

            ($ back-to-top)))))))
