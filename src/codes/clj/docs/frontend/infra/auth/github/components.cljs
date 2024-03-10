(ns codes.clj.docs.frontend.infra.auth.github.components
  (:require ["@mantine/core" :refer [Avatar Button NavLink]]
            ["@tabler/icons-react" :refer [IconBrandGithub IconChevronRight]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [helix.core :refer [$]]))

(defnc login-navlink [{:keys [close login-link]}]
  ($ NavLink
    {:data-testid "github-login-navlink"
     :href login-link
     :onClick close
     :variant "filled"
     :active true
     :label "Log In"
     :leftSection ($ IconBrandGithub {:size "1.5rem" :stroke 1.5})
     :rightSection ($ IconChevronRight)}))

(defnc logoff-navlink [{:keys [logoff close user]}]
  (let [{:keys [avatar-url]} (:author user)]
    ($ NavLink
      {:data-testid "github-logoff-navlink"
       :onClick (fn [_] (logoff) (close))
       :variant "subtle"
       :active true
       :label "Logoff"
       :leftSection ($ Avatar {:src avatar-url :radius "xl" :size "sm"})
       :rightSection ($ IconChevronRight)})))

(defnc auth-navlink [{:keys [logoff close user login-link]}]
  (if user
    ($ logoff-navlink {:logoff logoff :close close :user user})
    ($ login-navlink {:close close :login-link login-link})))

(defnc login-button [{:keys [login-link]}]
  ($ Button {:data-testid "github-login-button"
             :component "a"
             :href login-link
             :size "compact-md"
             :variant "primary"
             :leftSection ($ IconBrandGithub
                            {:size "1.5rem" :stroke 1.5})}
    "Log in"))

(defnc logoff-button [{:keys [logoff user]}]
  (let [{:keys [avatar-url]} (:author user)]
    ($ Button {:data-testid "github-logoff-button"
               :onClick logoff
               :size "compact-md"
               :variant "subtle"
               :leftSection ($ Avatar
                              {:src avatar-url :radius "md" :size "sm"})}
      "Logoff")))

(defnc auth-button [{:keys [logoff user login-link]}]
  (if user
    ($ logoff-button {:logoff logoff :user user})
    ($ login-button {:login-link login-link})))
