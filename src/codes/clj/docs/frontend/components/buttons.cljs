(ns codes.clj.docs.frontend.components.buttons
  (:require ["@mantine/core" :refer [ActionIcon useComputedColorScheme
                                     useMantineColorScheme]]
            ["@tabler/icons-react" :refer [IconMoon IconSun]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [helix.core :refer [$]]))

(defnc DarkModeButton []
  (let [setColorScheme (.-setColorScheme (useMantineColorScheme))
        computedColorScheme (useComputedColorScheme "light" #js {:getInitialValueInEffect true})]
    ($ ActionIcon {:onClick #(setColorScheme (if (= computedColorScheme "light") "dark" "light"))
                   :variant "default"
                   :size "lg"
                   :aria-label "Toggle color scheme"}
       (if (= computedColorScheme "dark")
         ($ IconSun {:stroke 1.5})
         ($ IconMoon {:stroke 1.5})))))
