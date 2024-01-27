(ns codes.clj.docs.frontend.panels.shell.components
  (:require ["@mantine/core" :refer [ActionIcon Anchor AppShell Burger Button
                                     Center Container Group Text
                                     useComputedColorScheme
                                     useMantineColorScheme]]
            ["@mantine/hooks" :refer [useDisclosure]]
            ["@tabler/icons-react" :refer [IconBrandGithub IconMoon IconSocial IconSun]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [helix.core :refer [$]]
            [helix.dom :as dom]))

(defnc DarkModeButton []
  (let [setColorScheme (.-setColorScheme (useMantineColorScheme))
        computedColorScheme (useComputedColorScheme "light" #js {:getInitialValueInEffect true})]
    ($ ActionIcon {:onClick #(setColorScheme (if (= computedColorScheme "light") "dark" "light"))
                   :variant "default"
                   :size "1.9rem"
                   :aria-label "Toggle color scheme"}
       (if (= computedColorScheme "dark")
         ($ IconSun {:stroke 1.5})
         ($ IconMoon {:stroke 1.5})))))

(defnc Logo []
  ($ Anchor {:href "./" :underline "never"
             :color "var(--mantine-color-text)"}
     ($ Center {:inline true}
        ($ IconSocial {:color "var(--mantine-color-text)"
                       :size "1.8rem" :stroke "1.5"})
        ($ Text {:ml "0.2rem" :size "xl"} "docs.clj.codes"))))

(defnc Header []
  (let [[opened updater] (useDisclosure false)
        toggle (.-toggle updater)]
    ($ AppShell.Header
       {:className "header"}
       ($ Container {:size "md"}
          (dom/div
           {:className "innerHeader"}
           ($ Logo)
           ($ Group
              ($ Group {:gap 5 :visibleFrom "sm"}
                 (dom/a
                  {:size "lg" :href "/libraries"
                   :className "linkHeader"}
                  "Libraries")
                 ($ Button
                    {:size "compact-md" :variant "primary"}
                    "Log in"))
              ($ DarkModeButton))
           ($ Burger
              {:opened opened :onClick toggle
               :size "sm" :hiddenFrom "sm"}))))))

(defnc Footer []
  (dom/footer
   {:className "footer"}
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
