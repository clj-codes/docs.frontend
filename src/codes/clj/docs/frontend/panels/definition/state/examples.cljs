(ns codes.clj.docs.frontend.panels.definition.state.examples
  (:require [codes.clj.docs.frontend.infra.auth.state :as auth.state]
            [codes.clj.docs.frontend.infra.http :as http]
            [codes.clj.docs.frontend.panels.definition.state :refer [definition-social-results]]
            [town.lilac.flex.promise :as flex.promise]))

(defn ^:private update-local-state-examples [response author]
  (definition-social-results
    update-in [:value :examples]
    (fn [current-examples updated]
      (mapv #(if (= (:example-id updated) (:example-id %)) updated %)
            current-examples))
    (assoc (:body response) :author author)))

(defn ^:private remove-local-state-examples [response author]
  (definition-social-results
    update-in [:value :examples]
    (fn [current-examples deleted]
      (remove #(= (:example-id deleted) (:example-id %))
              current-examples))
    (assoc (:body response) :author author)))

(def new!
  (flex.promise/resource
   (fn [example]
     (let [{:keys [author access-token]} @auth.state/user-signal]
       (definition-social-results assoc :loading? true)
       (-> (http/request! {:path "social/example/"
                           :headers {"authorization" (str "Bearer " access-token)}
                           :method :post
                           :body example})
           (.then (fn [response]
                    (definition-social-results assoc :error nil :loading? false)
                    (definition-social-results
                      update-in [:value :examples] merge
                      (assoc (:body response)
                             :author author
                             :editors [(assoc author :edited-at (js/Date.))]))))
           (.catch (fn [error]
                     (js/console.error error)
                     (definition-social-results assoc :error error :loading? false)
                     (throw error))))))))

(def edit!
  (flex.promise/resource
   (fn [example]
     (let [{:keys [author access-token]} @auth.state/user-signal]
       (definition-social-results assoc :loading? true)
       (-> (http/request! {:path "social/example/"
                           :headers {"authorization" (str "Bearer " access-token)}
                           :method :put
                           :body (select-keys example [:example-id :body])})
           (.then (fn [response]
                    (definition-social-results assoc :error nil :loading? false)
                    (update-local-state-examples response author)))
           (.catch (fn [error]
                     (js/console.error error)
                     (definition-social-results assoc :error error :loading? false)
                     (throw error))))))))

(def delete!
  (flex.promise/resource
   (fn [example]
     (let [{:keys [author access-token]} @auth.state/user-signal]
       (definition-social-results assoc :loading? true)
       (-> (http/request! {:path (str "social/example/" (:example-id example))
                           :headers {"authorization" (str "Bearer " access-token)}
                           :method :delete})
           (.then (fn [response]
                    (definition-social-results assoc :error nil :loading? false)
                    (prn :delete response)
                    (if (-> response :body :editors)
                      (update-local-state-examples response author)
                      (remove-local-state-examples response author))))
           (.catch (fn [error]
                     (js/console.error error)
                     (definition-social-results assoc :error error :loading? false)
                     (throw error))))))))
