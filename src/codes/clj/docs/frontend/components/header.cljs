(ns codes.clj.docs.frontend.components.header
  (:require ["@mantine/core" :refer [Anchor AppShell Burger Button Center
                                     Container Group Text]]
            ["@mantine/hooks" :refer [useDisclosure]]
            ["@tabler/icons-react" :refer [IconSocial]]
            [codes.clj.docs.frontend.components.buttons :refer [DarkModeButton]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [helix.core :refer [$]]
            [helix.dom :as dom]))

(defnc Logo []
  ($ Anchor {:href "./" :underline "never" :color "var(--mantine-color-text)"}
     ($ Center {:inline true}
        ($ IconSocial {:color "var(--mantine-color-text)" :size "1.8rem" :stroke "1.5"})
        ($ Text {:ml "0.2rem" :size "xl"} "docs.clj.codes"))))

(defnc Header []
  (let [[opened updater] (useDisclosure false)
        toggle (.-toggle updater)]
    ($ AppShell.Header
       {:className "header"}
       ($ Container {:size "md"}
          (dom/div
           {:className "inner"}
           ($ Logo)
           ($ Group
              ($ Group {:gap 5 :visibleFrom "sm"}
                 (dom/a
                  {:href "/libraries" :className "link"}
                  "Libraries")
                 ($ Button
                    {:size "compact-lg" :variant "primary"} "Log in"))
              ($ DarkModeButton))
           ($ Burger
              {:opened opened
               :onClick toggle
               :size "sm"
               :hiddenFrom "sm"}))))))
