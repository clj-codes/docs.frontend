(ns codes.clj.docs.frontend.panels.definition.state.notes
  (:require [codes.clj.docs.frontend.infra.auth.state :as auth.state]
            [codes.clj.docs.frontend.infra.http :as http]
            [codes.clj.docs.frontend.panels.definition.state :refer [definition-social-results]]
            [town.lilac.flex.promise :as flex.promise]))

(def new!
  (flex.promise/resource
   (fn [note]
     (let [{:keys [author access-token]} @auth.state/user-signal]
       (definition-social-results assoc :loading? true)
       (-> (http/request! {:path "social/note/"
                           :headers {"authorization" (str "Bearer " access-token)}
                           :method :post
                           :body note})
           (.then (fn [response]
                    (definition-social-results assoc :error nil :loading? false)
                    (definition-social-results
                      update-in [:value :notes] merge
                      (assoc (:body response) :author author))))
           (.catch (fn [error]
                     (js/console.error error)
                     (definition-social-results assoc :error error :loading? false)
                     (throw error))))))))

(def edit!
  (flex.promise/resource
   (fn [note]
     (let [{:keys [author access-token]} @auth.state/user-signal]
       (definition-social-results assoc :loading? true)
       (-> (http/request! {:path "social/note/"
                           :headers {"authorization" (str "Bearer " access-token)}
                           :method :put
                           :body (select-keys note [:note-id :body])})
           (.then (fn [response]
                    (definition-social-results assoc :error nil :loading? false)
                    (definition-social-results
                      update-in [:value :notes]
                      (fn [current-notes updated]
                        (mapv #(if (= (:note-id updated) (:note-id %)) updated %)
                              current-notes))
                      (assoc (:body response) :author author))))
           (.catch (fn [error]
                     (js/console.error error)
                     (definition-social-results assoc :error error :loading? false)
                     (throw error))))))))

(def delete!
  (flex.promise/resource
   (fn [note]
     (let [{:keys [author access-token]} @auth.state/user-signal]
       (definition-social-results assoc :loading? true)
       (-> (http/request! {:path (str "social/note/" (:note-id note))
                           :headers {"authorization" (str "Bearer " access-token)}
                           :method :delete})
           (.then (fn [response]
                    (definition-social-results assoc :error nil :loading? false)
                    (definition-social-results
                      update-in [:value :notes]
                      (fn [current-notes deleted]
                        (remove #(= (:note-id deleted) (:note-id %))
                                current-notes))
                      (assoc (:body response) :author author))))
           (.catch (fn [error]
                     (js/console.error error)
                     (definition-social-results assoc :error error :loading? false)
                     (throw error))))))))
