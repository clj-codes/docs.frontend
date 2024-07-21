(ns codes.clj.docs.frontend.panels.author.view
  (:require ["@mantine/core" :refer [Alert Avatar Box Center Container Grid
                                     Group LoadingOverlay Space Text Title List]]
            ["@tabler/icons-react" :refer [IconInfoCircle]]
            [clojure.string :as str]
            [codes.clj.docs.frontend.components.navigation :refer [back-to-top
                                                                   safe-anchor]]
            [codes.clj.docs.frontend.infra.flex.hook :refer [use-flex]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.panels.author.adapters :as author.adapters]
            [codes.clj.docs.frontend.panels.author.state :refer [author-response]]
            [helix.core :refer [$]]
            [helix.dom :as dom]))

(defnc social-preview-list [{:keys [title items id-key data-key]}]
  (dom/div
    ($ Title {:style #js {:paddingTop 10} :order 4}
      title)
    ($ List {:listStyleType "square"}
      (map (fn [item]
             ($ (-> List .-Item) {:key (id-key item)}
               ($ Box {:w #js {:base 350 :xs 400 :sm 600 :md 800 :lg 900 :xl 1000}}
                 ($ Text {:id (id-key item) :className "social-preview-item"
                          :truncate "end"}
                   (data-key item)))))
        items))))

(defnc author-socials-preview-list [{:keys [socials]}]
  (dom/div
    ($ Title {:order 2} "Interactions")
    ($ Space {:h "md"})
    ($ Group
      ($ Grid {:data-testid "author-grid"}
        (map (fn [{:keys [definition-id examples notes see-alsos]}]
               ($ (-> Grid .-Col) {:key definition-id}
                 (dom/div
                   ($ safe-anchor {:fz "xl" :fw 500
                                   :href (str "/" definition-id)}
                     (str/replace definition-id #"/0$" ""))

                   (when (seq examples)
                     ($ social-preview-list {:title "Examples"
                                             :items examples
                                             :id-key :example-id
                                             :data-key :body}))

                   (when (seq see-alsos)
                     ($ social-preview-list {:title "See Alsos"
                                             :items see-alsos
                                             :id-key :see-also-id
                                             :data-key :definition-id-to}))

                   (when (seq notes)
                     ($ social-preview-list {:title "Notes"
                                             :items notes
                                             :id-key :note-id
                                             :data-key :body})))))

          socials)))))

(defnc author-detail-page []
  (let [{:keys [loading? error value]} (use-flex author-response)
        {:keys [login account-source avatar-url socials]} value]

    ($ Container {:p "md"}
      (if loading?

        ($ LoadingOverlay {:visible loading? :zIndex 1000
                           :overlayProps #js {:radius "sm" :blur 2}})

        (if error
          ($ Alert {:variant "light" :color "red"
                    :radius "md" :title "Error"
                    :icon ($ IconInfoCircle)}
            (str error))

          (dom/div

            ($ Center
              ($ Group {:wrap "nowrap"}
                ($ Avatar {:src avatar-url
                           :size 200
                           :radius 200})
                (dom/div
                  ($ Title {:order 3}
                    login)

                  ($ Text {:fz "xs" :tt "uppercase" :fw 700 :c "dimmed"}
                    (name account-source))

                  ($ Space {:h "sm"})

                  ($ Text {:data-testid "author-social-summary"
                           :fz "lg" :fw 500}
                    (author.adapters/->string-summary value)))))

            ($ Space {:h "lg"})

            (when socials
              ($ author-socials-preview-list {:socials socials}))

            ($ back-to-top)))))))
