(ns codes.clj.docs.frontend.components.query
  (:require ["@mantine/core" :refer [Alert Anchor Avatar Grid Group Indicator
                                     LoadingOverlay SimpleGrid Text Title
                                     Tooltip]]
            ["@tabler/icons-react" :refer [IconInfoCircle]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [helix.core :refer [$]]
            [helix.dom :as dom]))

(defnc latest-interactions [{:keys [value loading? error]}]
  ($ SimpleGrid {:cols 1}
    ($ Title {:order 1} "Recently Updated")

    (if error
      ($ Alert {:variant "light" :color "red" :radius "md" :title "Error" :icon ($ IconInfoCircle)}
        (str error))

      ($ Group {:pos "relative" :grow true}
        ($ LoadingOverlay {:visible loading? :zIndex 1000 :overlayProps #js {:radius "sm" :blur 2}})
        ($ SimpleGrid {:cols 2}
          (map
            (fn [{:keys [author]}]
              (let [{:keys [author-id login account-source avatar-url]} author]
                ($ Group {:key (str "latest" author-id) :grow true}
                  ($ Anchor {:href (str "/author/" login "/" account-source)}
                    ($ Avatar {:size "md" :src avatar-url}))

                  (dom/div
                    ($ Text "banana")))))

            value))))))

    ; <UnstyledButton className={classes.user}>
    ;   <Group>
    ;     <Avatar
    ;       src="https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/avatars/avatar-8.png"
    ;       radius="xl"
    ;     />
    ;
    ;     <div style={{ flex: 1 }}>
    ;       <Text size="sm" fw={500}>
    ;         Harriette Spoonlicker
    ;       </Text>
    ;
    ;       <Text c="dimmed" size="xs">
    ;         hspoonlicker@outlook.com
    ;       </Text>
    ;     </div>
    ;
    ;     <IconChevronRight style={{ width: rem(14), height: rem(14) }} stroke={1.5} />
    ;   </Group>
    ; </UnstyledButton>

(defnc top-author [{:keys [value loading? error]}]
  ($ SimpleGrid {:cols 1}
    ($ Title {:order 1} "Top Authors")

    (if error
      ($ Alert {:variant "light" :color "red" :radius "md" :title "Error" :icon ($ IconInfoCircle)}
        (str error))

      ($ Group {:pos "relative"}
        ($ LoadingOverlay {:visible loading? :zIndex 1000 :overlayProps #js {:radius "sm" :blur 2}})
        ($ Grid {:grow false :gutter "lg"}
          (map
            (fn [{:keys [author-id login account-source interactions avatar-url]}]
              ($ (.-Col Grid) {:key (str "top" author-id)
                               :span "lg"}
                ($ Indicator {:withBorder true :inline true :label interactions :size 16 :position "bottom-end"}
                  ($ Anchor {:href (str "/author/" login "/" account-source)}
                    ($ Tooltip {:label (str login " with " interactions " interactions")
                                :withArrow true}
                      ($ Avatar {:size "md" :src avatar-url}))))))
            value))))))
