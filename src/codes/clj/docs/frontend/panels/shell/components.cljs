(ns codes.clj.docs.frontend.panels.shell.components
  (:require ["@mantine/core" :refer [ActionIcon Anchor AppShell Burger Button
                                     Center Container Drawer Group NavLink
                                     ScrollArea Text useComputedColorScheme
                                     useMantineColorScheme]]
            ["@mantine/hooks" :refer [useDisclosure]]
            ["@tabler/icons-react" :refer [IconBrandGithub IconChevronRight
                                           IconMoon IconSocial IconSun]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [helix.core :refer [$]]
            [helix.dom :as dom]))

(defnc DarkModeButton []
  (let [set-scheme (.-setColorScheme (useMantineColorScheme))
        current-scheme (useComputedColorScheme
                        "light"
                        #js {:getInitialValueInEffect true})]
    ($ ActionIcon {:onClick #(set-scheme
                              (if (= current-scheme "light")
                                "dark"
                                "light"))
                   :variant "default"
                   :size "1.9rem"
                   :aria-label "Toggle color scheme"}
       (if (= current-scheme "dark")
         ($ IconSun {:stroke 1.5})
         ($ IconMoon {:stroke 1.5})))))

(defnc Logo []
  ($ Anchor {:href "./" :underline "never"
             :color "var(--mantine-color-text)"}
     ($ Center {:inline true}
        ($ IconSocial {:color "var(--mantine-color-text)"
                       :size "1.8rem" :stroke "1.5"})
        ($ Text {:ml "0.2rem" :size "xl"}
           "docs.clj.codes"))))

(defnc HeaderDrawer [{:keys [links opened close]}]
  ($ Drawer {:data-testid "header-drawer"
             :position "right"
             :size "xs"
             :opened opened
             :onClose close
             :padding "md"
             :title "Navigation"
             :hiddenFrom "sm"
             :zIndex 1000000}
     ($ ScrollArea {:data-testid "header-drawer-scrollarea"}
        (->> links
             (map-indexed
              (fn [index {:keys [href label]}]
                ($ NavLink
                   {:key index
                    :href href
                    :leftSection ($ Text {:size "lg"} label)
                    :rightSection ($ IconChevronRight)})))
             doall)
        ($ NavLink
           {:href "/login"
            :variant "filled"
            :active true
            :label "Log In"
            :leftSection ($ IconBrandGithub {:size "1.5rem" :stroke 1.5})
            :rightSection ($ IconChevronRight)}))))

(defnc Header [{:keys [links]}]
  (let [[opened updater] (useDisclosure false)
        close (.-toggle updater)]
    ($ AppShell.Header {:className "header"}
       ($ Container {:size "md"}
          ($ Group {:className "innerHeader"}
             ($ Logo)
             ($ Group
                ($ Group {:data-testid "header-root-links"
                          :gap "xs"
                          :visibleFrom "sm"}
                   (->> links
                        (map-indexed
                         (fn [index {:keys [href label]}]
                           ($ Button {:key index
                                      :component "a"
                                      :href href
                                      :size "compact-md"
                                      :variant "transparent"
                                      :color "var(--mantine-color-text)"}
                              label)))
                        doall)
                   ($ Button {:component "a"
                              :href "/login"
                              :size "compact-md"
                              :variant "primary"
                              :leftSection ($ IconBrandGithub {:size "1.5rem" :stroke 1.5})}
                      "Log in"))
                ($ Group {:gap "xs"}
                   ($ DarkModeButton)
                   ($ Burger {:opened opened :onClick close
                              :size "sm" :hiddenFrom "sm"})))
             ($ HeaderDrawer {:links links :opened opened :close close}))))))

(defnc Footer []
  (dom/footer {:className "footer"}
    ($ Container {:className "afterFooter" :size "md"}
       ($ Group {:gap 3}
          ($ Text {:c "dimmed" :size "sm"}
             "© 2024 docs.clj.codes by")
          ($ Anchor {:className "linkFooter"
                     :color "var(--mantine-color-text)"
                     :href "https://rafael.delboni.cc"
                     :target "_blank"}
             "delboni"))
       ($ Group {:className "socialFooter" :gap 0 :justify "flex-end" :wrap "nowrap"}
          ($ ActionIcon {:size "lg" :color "gray" :variant "subtle"
                         :component "a"
                         :href "https://www.github.com/clj-codes"
                         :target "_blank"}
             ($ IconBrandGithub {:size "1.5rem" :stroke 1.5}))))))
