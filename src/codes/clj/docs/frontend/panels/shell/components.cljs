(ns codes.clj.docs.frontend.panels.shell.components
  (:require ["@mantine/core" :refer [ActionIcon Anchor Burger Button Center
                                     Container Divider Drawer Group NavLink
                                     ScrollArea Text useComputedColorScheme
                                     useMantineColorScheme]]
            ["@mantine/hooks" :refer [useDisclosure]]
            ["@tabler/icons-react" :refer [IconBrandGithub IconChevronRight
                                           IconMoon IconSocial IconSun]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.panels.search.view :refer [search-spotlight]]
            [helix.core :refer [$]]
            [helix.dom :as dom]))

(defnc dark-mode-button []
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

(defnc logo []
  ($ Anchor {:href "/" :underline "never"
             :color "var(--mantine-color-text)"}
    ($ Center {:inline true}
      ($ IconSocial {:color "var(--mantine-color-text)"
                     :size "1.8rem" :stroke "1.5"})
      ($ Text {:ml "0.2rem" :size "xl"}
        "docs.clj.codes"))))

(defnc header-drawer [{:keys [links login-link opened close]}]
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
              :onClick close
              :leftSection ($ Text {:size "lg"} label)
              :rightSection ($ IconChevronRight)})))
        doall)
      ($ NavLink
        {:href login-link
         :onClick close
         :variant "filled"
         :active true
         :label "Log In"
         :leftSection ($ IconBrandGithub {:size "1.5rem" :stroke 1.5})
         :rightSection ($ IconChevronRight)}))))

(defnc header [{:keys [links login-link]}]
  (let [[opened updater] (useDisclosure false)
        close (.-toggle updater)]
    (dom/div
      ($ Container {:size "md"}
        ($ Group {:className "shell-inner-header"}
          ($ logo)
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
                         :href login-link
                         :size "compact-md"
                         :variant "primary"
                         :leftSection ($ IconBrandGithub
                                        {:size "1.5rem" :stroke 1.5})}
                "Log in"))
            ($ Group {:gap "xs"}
              ($ search-spotlight)
              ($ dark-mode-button)
              ($ Burger {:opened opened :onClick close
                         :size "sm" :hiddenFrom "sm"})))
          ($ header-drawer {:links links :login-link login-link
                            :opened opened :close close})))
      ($ Divider))))

(defnc footer []
  (dom/footer {:className "shell-footer"}
    ($ Container {:className "shell-after-footer" :size "md"}
      ($ Group {:gap 3}
        ($ Text {:c "dimmed" :size "sm"}
          "© 2024 docs.clj.codes by")
        ($ Anchor {:className "shell-link-footer"
                   :color "var(--mantine-color-text)"
                   :href "https://rafael.delboni.cc"
                   :target "_blank"}
          "delboni"))
      ($ Group {:className "shell-social-footer"
                :gap 0 :justify "flex-end" :wrap "nowrap"}
        ($ ActionIcon {:size "lg" :color "gray" :variant "subtle"
                       :component "a"
                       :href "https://www.github.com/clj-codes"
                       :target "_blank"}
          ($ IconBrandGithub {:size "1.5rem" :stroke 1.5}))))))
