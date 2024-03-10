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

; TODO test?
(comment
  ; create cookie
  (user assoc :value {:author {:author-id #uuid "919e0da0-9d4e-4e95-a9a5-5001ca641405"
                               :login "rafaeldelboni"
                               :account-source "github"
                               :avatar-url "https://avatars.githubusercontent.com/u/1683898?v=4"
                               :created-at #inst "2024-03-09T01:23:35.332-00:00"}
                      :access-token "<token>"})
  ; delete cookie
  (user assoc :value nil))
