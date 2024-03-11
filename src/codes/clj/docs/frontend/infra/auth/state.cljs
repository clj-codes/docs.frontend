(ns codes.clj.docs.frontend.infra.auth.state
  (:require [codes.clj.docs.frontend.infra.cookie :refer [get-edn-cookie
                                                          remove-cookie
                                                          set-cookie]]
            [town.lilac.flex :as flex]))

(def user-cookie-name "codes.clj.docs.user")

(def user
  (flex/source {:error nil
                :loading? false
                :value (get-edn-cookie user-cookie-name)}))

(def user-signal
  (flex/signal (:value @user)))

; user cookie effect
(flex/listen user-signal
             (fn [user]
               (if user
                 (set-cookie user-cookie-name user)
                 (remove-cookie user-cookie-name))))
