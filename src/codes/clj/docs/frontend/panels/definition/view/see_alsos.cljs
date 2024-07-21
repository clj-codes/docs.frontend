(ns codes.clj.docs.frontend.panels.definition.view.see-alsos
  (:require ["@mantine/core" :refer [Anchor Button Card Center Combobox Grid
                                     Group Loader Text TextInput Title Tooltip
                                     useCombobox]]
            ["@mantine/hooks" :refer [useDebouncedValue]]
            ["@tabler/icons-react" :refer [IconInfoCircle]]
            [clojure.string :as str]
            [codes.clj.docs.frontend.components.navigation :refer [safe-anchor]]
            [codes.clj.docs.frontend.infra.flex.hook :refer [use-flex]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.panels.definition.state.see-alsos :as state.see-alsos]
            [codes.clj.docs.frontend.panels.search.state :refer [search-fetch]]
            [helix.core :refer [$]]
            [helix.dom :as dom]
            [helix.hooks :as hooks]))

(defnc editor-see-also [{:keys [definition-id see-alsos on-save on-cancel]}]
  (let [query-limit 30
        combobox (useCombobox)
        ignored-ids (-> (map :definition-id-to see-alsos) (conj definition-id) set)
        [definition-id-to set-definition-id-to] (hooks/use-state "")
        [cb-value set-cb-value] (hooks/use-state "")
        [deb-query set-deb-query] (hooks/use-state "")
        [debounced] (useDebouncedValue deb-query 500)
        {:keys [value loading?]} (use-flex state.see-alsos/search-results)
        auto-complete-list (remove #(contains? ignored-ids (:id %)) value)]

    (hooks/use-effect
      [debounced]
      (when debounced
        (search-fetch state.see-alsos/search-results (or debounced "") query-limit)))

    ($ Grid {:data-testid "editor-see-also" :align "center"}
      ($ (-> Grid .-Col) {:span 12}
        ($ Combobox
          {:onOptionSubmit (fn [option-value option-props]
                             (set-definition-id-to (.-id option-props))
                             (set-cb-value option-value)
                             (.closeDropdown combobox))
           :withinPortal true
           :store combobox}
          ($ (-> Combobox .-Target)
            ($ TextInput {:id "editor-see-also-textarea"
                          :data-testid "editor-see-also-textarea"
                          :label "New see also"
                          :placeholder "Search definition by name"
                          :size "lg"
                          :value cb-value
                          :onChange (fn [event]
                                      (set-cb-value (-> event .-currentTarget .-value))
                                      (set-deb-query (-> event .-currentTarget .-value))
                                      (set-definition-id-to nil)
                                      (.resetSelectedOption combobox)
                                      (.openDropdown combobox))
                          :onClick (fn [] (.openDropdown combobox))
                          :onFocus (fn [] (.openDropdown combobox))
                          :onBlur (fn [] (.closeDropdown combobox))
                          :rightSection (when loading?
                                          ($ Loader {:size "md"}))}))

          ($ (-> Combobox .-Dropdown)
            ($ (-> Combobox .-Options) {:mah 200 :style #js {:overflowY "auto"}}
              (if-not (nil? (seq auto-complete-list))
                (map (fn [{:keys [id name]}]
                       ($ (-> Combobox .-Option) {:value name :id id :key id}
                         (dom/div
                           ($ Title {:order 4} name)
                           ($ Text {:size "xs" :variant "dimmed"}
                             (str/replace id #"/0$" "")))))
                     auto-complete-list)
                ($ (-> Combobox .-Empty) "No results found"))))))

      ($ (-> Grid .-Col) {:span #js {:base 12 :md 8}}
        ($ Group {:gap "xs"}
          ($ IconInfoCircle {:style #js {:width "1.0rem" :height "1.0rem"}})
          ($ Text {:size "xs"} "Any contributions to this site will be under "
            ($ Text {:component "a" :href "/license#contributions" :inherit true :fw 700} "public domain"))))

      ($ (-> Grid .-Col) {:span #js {:base 12 :md 4}}
        ($ Group {:justify "flex-end" :gap "xs"}
          ($ Button {:id "editor-see-also-cancel-btn"
                     :data-testid "editor-see-also-cancel-btn"
                     :onClick #(do (set-definition-id-to nil)
                                   (on-cancel))
                     :variant "light" :color "red"} "Cancel")
          ($ Button {:id "editor-see-also-save-btn"
                     :data-testid "editor-see-also-save-btn"
                     :disabled (str/blank? definition-id-to)
                     :onClick #(do (set-definition-id-to nil)
                                   (on-save definition-id-to))
                     :variant "filled" :color "teal"} "Save"))))))

(defnc card-see-also [{:keys [see-also user set-delete-modal-fn]}]
  (let [{:keys [see-also-id definition-id-to author created-at]} see-also
        is-see-also-author? (= (-> user :author :author-id) (:author-id author))
        id (str/replace definition-id-to #"/0$" "")
        link-name (->> (str/split id "/") last)]
    ($ Card {:id (str "card-see-also-" see-also-id)
             :className "card-see-also"
             :withBorder true
             :shadow "sm"
             :mb "sm"}

      ($ (-> Card .-Section) {:component safe-anchor
                              :href (str "/" id)
                              :withBorder true
                              :inheritPadding true
                              :py "sm"}
        ($ Group {:justify "space-between" :gap "xs"}
          ($ Title {:order 4} link-name)))

      ($ (-> Card .-Section) {:withBorder false
                              :inheritPadding true
                              :pt "xs"
                              :pb "0"}
        ($ Text {:size "sm" :fw "bold"}
          id))

      ($ Title {:fw 450 :order 6 :lineClamp 1 :mt "sm"}
        ($ Text {:component "div"
                 :size "xs"
                 :lineClamp 3}
          ($ Group {:gap "xs"}
            ($ Tooltip {:label (str "at " (.toGMTString created-at)) :withArrow true}
              ($ Text {:c "dimmed"}
                (str "By ")
                ($ Anchor {:href (str "/author/" (:login author) "/" (:account-source author))}
                  (:login author))))
            (when is-see-also-author?
              ($ Group {:className "author-edit-delete-see-also"
                        :gap "xs"}
                ($ Anchor {:className "see-also-author-delete-button"
                           :id (str "see-also-author-delete-button-" see-also-id)
                           :data-testid (str "see-also-author-delete-button-" see-also-id)
                           :onClick #(set-delete-modal-fn
                                      {:fn (fn []
                                             (state.see-alsos/delete! see-also))})
                           :size "xs"}
                  "delete")))))))))

(defnc card-see-alsos [{:keys [definition see-alsos user set-delete-modal-fn]}]
  (let [[show-new-see-also-editor set-new-see-also-show-editor] (hooks/use-state false)]
    ($ Card {:id "card-see-alsos"
             :key "card-see-alsos"
             :data-testid "card-see-alsos"
             :withBorder true
             :shadow "sm"}

      ($ (-> Card .-Section) {:withBorder true :inheritPadding true :py "sm"}
        ($ Title {:id "card-see-alsos-title" :order 4}
          (str (count see-alsos) " see alsos")))

      ($ (-> Card .-Section) {:inheritPadding true
                              :p "sm"}
        ($ Grid {:py 0 :gutter "xs"}
          (if (seq see-alsos)
            (map #($ (-> Grid .-Col) {:key (:see-also-id %)
                                      :span #js {:base 12 :sm 6}}
                    ($ card-see-also {:see-also %
                                      :user user
                                      :set-delete-modal-fn set-delete-modal-fn}))
                 see-alsos)
            ($ (-> Grid .-Col) {:span 12}
              ($ Center
                ($ Text "No see alsos"))))))

      ($ (-> Card .-Section) {:inheritPadding true :pb "sm"}
        (if user
          (if show-new-see-also-editor
            ($ editor-see-also {:definition-id (:id definition)
                                :see-alsos see-alsos
                                :on-cancel #(set-new-see-also-show-editor false)
                                :on-save (fn [definition-id-to]
                                           (do (set-new-see-also-show-editor false)
                                               (state.see-alsos/new! {:definition-id (:id definition)
                                                                      :definition-id-to definition-id-to})))})
            ($ Group {:data-testid "add-see-also-logged"
                      :justify "flex-end"}
              ($ Anchor {:data-testid "add-see-also-btn"
                         :onClick #(set-new-see-also-show-editor true)
                         :size "sm"} "Add a see-also")))
          ($ Group {:data-testid "add-see-also-logout"
                    :justify "flex-end"}
            ($ Text {:size "sm"} "Log in to add a see-also")))))))
