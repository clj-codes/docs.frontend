(ns codes.clj.docs.frontend.panels.definition.state.see-alsos
  (:require [codes.clj.docs.frontend.infra.auth.state :as auth.state]
            [codes.clj.docs.frontend.infra.http :as http]
            [codes.clj.docs.frontend.panels.definition.state :refer [definition-social-results]]
            [town.lilac.flex :as flex]
            [town.lilac.flex.promise :as flex.promise]))

(def search-results (flex/source {:error nil
                                  :loading? false
                                  :value nil}))

(defn ^:private create-local-state-see-alsos [response author]
  (definition-social-results
    update-in [:value :see-alsos] merge
    (assoc (:body response) :author author)))

(defn ^:private remove-local-state-see-alsos [response author]
  (definition-social-results
    update-in [:value :see-alsos]
    (fn [current-see-alsos deleted]
      (remove #(= (:see-also-id deleted) (:see-also-id %))
              current-see-alsos))
    (assoc (:body response) :author author)))

(def new!
  (flex.promise/resource
   (fn [see-also]
     (let [{:keys [author access-token]} @auth.state/user-signal]
       (definition-social-results assoc :loading? true)
       (-> (http/request! {:path "social/see-also/"
                           :headers {"authorization" (str "Bearer " access-token)}
                           :method :post
                           :body see-also})
           (.then (fn [response]
                    (definition-social-results assoc :error nil :loading? false)
                    (create-local-state-see-alsos response author)))
           (.catch (fn [error]
                     (js/console.error error)
                     (definition-social-results assoc :error error :loading? false)
                     (throw error))))))))

(def delete!
  (flex.promise/resource
   (fn [see-also]
     (let [{:keys [author access-token]} @auth.state/user-signal]
       (definition-social-results assoc :loading? true)
       (-> (http/request! {:path (str "social/see-also/" (:see-also-id see-also))
                           :headers {"authorization" (str "Bearer " access-token)}
                           :method :delete})
           (.then (fn [response]
                    (definition-social-results assoc :error nil :loading? false)
                    (remove-local-state-see-alsos response author)))
           (.catch (fn [error]
                     (js/console.error error)
                     (definition-social-results assoc :error error :loading? false)
                     (throw error))))))))
