(ns codes.clj.docs.frontend.panels.search.components
  (:require ["@mantine/core" :refer [ActionIcon Badge Button Card FocusTrap
                                     Group Group Loader SimpleGrid Space
                                     Spotlight Text Text TextInput Title Title]]
            ["@mantine/spotlight" :refer [Spotlight]]
            ["@tabler/icons-react" :refer [IconFileCode IconFolderCode
                                           IconSearch IconSocial IconZoomCode]]
            [clojure.string :as str]
            [codes.clj.docs.frontend.components.navigation :refer [safe-anchor]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [helix.core :refer [$]]
            [helix.dom :as dom]))

(defnc spotlight-modal
  [{:keys [query-limit debounced-query loading? items close-fn query] :as props}]
  ($ Spotlight.Root {:data-testid "spotlight-modal-search-root"
                     :& (dissoc props
                                :query-limit
                                :debounced-query
                                :loading?
                                :close-fn
                                :items)}

    ($ Spotlight.Search {:data-testid "spotlight-modal-search-input"
                         :placeholder "Search"
                         :leftSection ($ IconSearch {:stroke 1.5})})

    ($ Spotlight.ActionsList
      (cond
        loading? ($ Spotlight.Empty {:data-testid "spotlight-loading"
                                     :key "spotlight-loading"}
                   ($ Loader {:color "moonstone"}))

        (seq items) (->> items
                         (group-by :type)
                         (map
                          (fn [[type-key grouped-items]]
                            ($ Spotlight.ActionsGroup
                              {:data-testid (str "spotlight-actions-group" type-key)
                               :key (str "spotlight-actions-group" type-key)
                               :label (name type-key)}
                              (map
                                (fn [{:keys [id name doc type]}]
                                  ($ Spotlight.Action
                                    {:data-testid (str "spotlight-action-" id)
                                     :key (str "spotlight-action-" id)
                                     :label name
                                     :description ($ Text {:component "div"
                                                           :size "xs"
                                                           :lineClamp 3}
                                                    ($ Text {:fw "bold"}
                                                      (str/replace id #"/0$" ""))
                                                    ($ Text
                                                      (or doc "no documentation")))
                                     :component safe-anchor
                                     :underline "never"
                                     :href (str "/" id)
                                     :leftSection (case type
                                                    :definition ($ IconZoomCode)
                                                    :namespace ($ IconFileCode)
                                                    :project ($ IconFolderCode)
                                                    ($ IconSocial))}))
                                grouped-items)))))

        :else (if (and (zero? (count items))
                       (not (str/blank? query))
                       (= query debounced-query))
                ($ Spotlight.Empty {:data-testid "spotlight-no-results"
                                    :key "spotlight-no-results"}
                  "No results were found")
                ($ Spotlight.Empty {:data-testid "spotlight-no-query"
                                    :key "spotlight-no-query"}
                  "Search the documentation")))

      (when (>= (count items) query-limit)
        ($ Spotlight.Empty {:data-testid "spotlight-limit-reached"
                            :key "spotlight-limit-reached"}
          ($ Button {:component "a"
                     :href (str "/search?q=" debounced-query)
                     :onClick close-fn}
            (str "See all " (count items) " results")))))))

(defnc spotlight-search-button [{:keys [on-click]}]
  (let [props {:onClick on-click
               :variant "outline"
               :color "gray"
               :label "Search"
               :aria-label "Search"}]
    ($ Group
      ($ Button {:id "spotlight-search-button"
                 :data-testid "spotlight-search-button"
                 :& (conj props
                          {:visibleFrom "sm"
                           :size "compact-md"
                           :leftSection ($ IconSearch)})}
        "Search")
      ($ ActionIcon {:& (conj props
                              {:hiddenFrom "sm"
                               :size "1.9rem"})}
        ($ IconSearch)))))

(defnc page-result-card [{:keys [id type doc group] :as item}]
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

      ($ Card.Section {:withBorder false
                       :inheritPadding true
                       :pt "xs"
                       :pb "0"}
        ($ Text {:size "xs" :fw "bold"}
          (str/replace id #"/0$" "")))

      ($ Title {:fw 450 :order 6 :lineClamp 1 :mt "sm"}
        ($ Text {:component "div"
                 :size "sm"
                 :lineClamp 3}
          (or doc
            ($ Text {:c "dimmed"}
              "no documentation")))))))

(defnc page-results-section [{:keys [query debounced set-debounced items]}]
  (dom/div
    (dom/section
      (if-not (str/blank? query)
        ($ Title {:order 1}
          "Search results for: "
          ($ Text {:component "span" :inherit true :variant "gradient"
                   :gradient #js {:from "cyan" :to "green" :deg 90}}
            query))
        ($ Title {:order 1} "Search the documentation")))

    ($ Space {:h "lg"})

    ($ FocusTrap {:tabIndex 0}
      ($ TextInput {:placeholder "Type your search here"
                    :size "lg"
                    :value (or debounced "")
                    :onChange #(set-debounced (-> % .-currentTarget .-value))
                    :autoFocus true}))

    ($ Space {:h "lg"})

    (if (seq items)
      ($ SimpleGrid {:cols 1 :verticalSpacing "xs" :data-testid "page-result-cards"}
        (map (fn [item] ($ page-result-card {:key (:id item) :& item}))
          items))

      (when-not (str/blank? query)
        ($ Title {:order 5 :c "dimmed"}
          "No results were found")))))
