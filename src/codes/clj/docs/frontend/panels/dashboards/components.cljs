(ns codes.clj.docs.frontend.panels.dashboards.components
  (:require ["@mantine/core" :refer [Alert Anchor Avatar Box Grid Group
                                     Indicator LoadingOverlay SimpleGrid Space
                                     Text Title Tooltip]]
            ["@tabler/icons-react" :refer [IconInfoCircle]]
            [clojure.string :as str]
            [codes.clj.docs.frontend.adapters.time :as adapters.time]
            [codes.clj.docs.frontend.components.navigation :refer [safe-anchor]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [helix.core :refer [$]]))

(defnc latest-interactions-list [{:keys [value loading? error]}]
  ($ SimpleGrid {:cols 1 :data-testid "latest-interactions-list"}
    ($ Title {:order 1} "Recently Updated")

    (if error
      ($ Alert {:variant "light" :color "red" :radius "md" :title "Error" :icon ($ IconInfoCircle)}
        (str error))

      ($ Box {:pos "relative"}
        ($ LoadingOverlay {:visible loading? :zIndex 1000
                           :loaderProps #js {:type "dots"}
                           :overlayProps #js {:radius "sm" :blur 2}})
        (when loading? ($ Space {:h "xl"}))

        ($ SimpleGrid {:cols #js {:base 1 :sm 2}}
          (map
            (fn [{:keys [note-id example-id see-also-id definition-id author created-at]}]
              (let [id (str "latest" (or note-id example-id see-also-id))
                    action (cond
                             note-id "authored a note for"
                             example-id "authored an example for"
                             see-also-id "added a see also on")
                    definition (str/replace definition-id #"/0$" "")
                    ago (adapters.time/time-since created-at (.now js/Date))
                    {:keys [login account-source avatar-url]} author]
                ($ Group {:key id :id id
                          :wrap "nowrap"
                          :className "interaction-text"}
                  ($ Anchor {:href (str "/author/" login "/" account-source)}
                    ($ Avatar {:src avatar-url}))
                  ($ Text {:size "sm"} login " "
                    ($ Text {:component "span"}
                      action " "
                      ($ safe-anchor {:href definition-id} definition)
                      " " ago ".")))))
            value))))))

(defnc top-authors-list [{:keys [value loading? error]}]
  ($ SimpleGrid {:cols 1 :data-testid "top-authors-list"}
    ($ Title {:order 1} "Top Authors")

    (if error
      ($ Alert {:variant "light" :color "red" :radius "md" :title "Error" :icon ($ IconInfoCircle)}
        (str error))

      ($ Group {:pos "relative"}
        ($ LoadingOverlay {:visible loading? :zIndex 1000
                           :loaderProps #js {:type "dots"}
                           :overlayProps #js {:radius "sm" :blur 2}})
        (when loading? ($ Space {:h "xl"}))

        ($ Grid {:grow false :gutter "lg"}
          (map
            (fn [{:keys [author-id login account-source interactions avatar-url]}]
              ($ (.-Col Grid) {:key (str "top" author-id)
                               :span "lg"}
                ($ Indicator {:withBorder true :inline true :label interactions :size 16 :position "bottom-end"}
                  ($ Anchor {:href (str "/author/" login "/" account-source)
                             :className "author-interaction-anchor"}
                    ($ Tooltip {:label (str login " with " interactions " interactions")
                                :withArrow true}
                      ($ Avatar {:size "md" :src avatar-url}))))))
            value))))))
