(ns codes.clj.docs.frontend.core
  (:require ["@mantine/core" :refer [MantineProvider ActionIcon SimpleGrid Switch useMantineColorScheme useComputedColorScheme]]
            ["@tabler/icons-react" :refer [IconSun IconMoon]]
            ["react-dom/client" :as rdom]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [helix.core :refer [$]]
            [helix.dom :as dom]))

(defnc DarkModeButton []
  (let [setColorScheme (.-setColorScheme (useMantineColorScheme))
        computedColorScheme (useComputedColorScheme "light" #js {:getInitialValueInEffect true})]
    ($ ActionIcon {:onClick #(setColorScheme (if (= computedColorScheme "light") "dark" "light"))
                   :variant "default"
                   :size "xl"
                   :aria-label "Toggle color scheme"}
       (if (= computedColorScheme "dark")
         ($ IconSun {:stroke 1.5})
         ($ IconMoon {:stroke 1.5})))))

(defnc app []
  ($ MantineProvider
     ($ SimpleGrid {:cols 3}
        (dom/div  "1")
        (dom/div "2")
        (dom/div "3")
        (dom/div "4")
        ($ DarkModeButton)
        ($ Switch {:defaultChecked true :label "I agree to sell my privacy"})
        (dom/div "Helix + Mantine"))))

(defonce root
  (rdom/createRoot (js/document.getElementById "app")))

(defn render []
  (.render root ($ app)))

(defn ^:export init []
  (render))
