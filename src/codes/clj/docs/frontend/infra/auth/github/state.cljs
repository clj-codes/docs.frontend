(ns codes.clj.docs.frontend.infra.auth.github.state
  (:require [codes.clj.docs.frontend.infra.auth.state :as auth.state]
            [codes.clj.docs.frontend.infra.http :as http]
            [town.lilac.flex.promise :as flex.promise]))

(def login!
  (flex.promise/resource
   (fn [code]
     (auth.state/user assoc :loading? true)
     (-> (http/request! {:method :post
                         :path "login/github"
                         :body {:code code}})
         (.then (fn [response]
                  (auth.state/user {:error nil
                                    :loading? false
                                    :value (:body response)})))
         (.catch (fn [error]
                   (js/console.error error)
                   (auth.state/user {:error error
                                     :loading? false
                                     :value nil})
                   (throw error)))))))
