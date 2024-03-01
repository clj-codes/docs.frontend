(ns codes.clj.docs.frontend.panels.search.view
  (:require ["@mantine/core" :refer [Alert Badge Card Container FocusTrap
                                     Group LoadingOverlay SimpleGrid Space
                                     Text TextInput Title]]
            ["@mantine/hooks" :refer [useDebouncedValue]]
            ["@tabler/icons-react" :refer [IconInfoCircle]]
            [clojure.string :refer [blank?]]
            [codes.clj.docs.frontend.components.navigation :refer [back-to-top
                                                                   safe-anchor]]
            [codes.clj.docs.frontend.infra.flex.hook :refer [use-flex]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.infra.routes.state :refer [routes-db]]
            [codes.clj.docs.frontend.panels.search.state :refer [search-response]]
            [helix.core :refer [$]]
            [helix.dom :as dom]
            [helix.hooks :as hooks]
            [react :as react]
            [reitit.frontend.easy :as rfe]))

(defnc card-search-result [{:keys [id type doc group] :as item}]
  (let [result-name (if group (str group "/" (:name item)) (:name item))]
    ($ Card {:id (str "card-search-result-" id)
             :key (str "card-search-result-" id)
             :data-testid (str "card-search-result-" id)
             :withBorder true
             :shadow "sm"
             :padding "lg"}

      ($ Card.Section {:component safe-anchor
                       :href (str "/" id)
                       :withBorder true
                       :inheritPadding true
                       :py "sm"}
        ($ Group {:justify "space-between" :gap "xs"}
          ($ Title {:order 4} result-name)
          ($ Badge {:variant "light" :color "moonstone" :size "xs"}
            (name type))))

      ($ Title {:fw 450 :order 6 :lineClamp 1 :mt "sm"}
        (or doc
          ($ Text {:c "dimmed" :size "sm"}
            "no documentation"))))))

;; TODO test
(defnc search []
  (let [{:keys [current-route]} (use-flex routes-db)
        query (-> current-route :query-params :q)
        [deb-query set-deb-query] (react/useState query)
        [debounced] (useDebouncedValue deb-query 500)
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
              (if-not (blank? query)
                ($ Title {:order 1}
                  "Search results for: "
                  ($ Text {:component "span" :inherit true :variant "gradient"
                           :gradient #js {:from "cyan" :to "green" :deg 90}}
                    query))
                ($ Title {:order 1} "Search the documentation")))

            ($ Space {:h "lg"})

            ($ FocusTrap
              ($ TextInput {:placeholder "Type your search here"
                            :size "lg"
                            :value deb-query
                            :onChange #(set-deb-query (-> % .-currentTarget .-value))
                            :autofocus true}))

            ($ Space {:h "lg"})

            (if (seq value)
              ($ SimpleGrid {:cols 1 :verticalSpacing "xs"}
                (map (fn [item] ($ card-search-result {:key (:id item) :& item}))
                  value))
              (when-not (blank? query)
                ($ Title {:order 5 :c "dimmed"}
                  "No results were found")))

            ($ back-to-top)))))))
