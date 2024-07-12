(ns codes.clj.docs.frontend.panels.author.view
  (:require ["@mantine/core" :refer [Alert Avatar Box Center Container Grid Group
                                     LoadingOverlay Space Text Title]]
            ["@tabler/icons-react" :refer [IconInfoCircle]]
            [clojure.string :as str]
            [codes.clj.docs.frontend.components.navigation :refer [back-to-top
                                                                   safe-anchor]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [helix.core :refer [$]]
            [helix.dom :as dom]))

(def author-value {:author-id #uuid "36a91f13-2cf9-4b80-a9f0-619b5dbe6ec5"
                   :login "rafaeldelboni"
                   :account-source "github"
                   :avatar-url "https://avatars.githubusercontent.com/u/1683898?v=4"
                   :created-at #inst "2024-03-11T14:16:54.404609000-00:00"
                   :socials [{:definition-id "org.clojure/clojure/clojure.core/assoc/0"
                              :notes [{:note-id #uuid "77f27f87-0c60-49cd-bb26-d1eef3ed2453"
                                       :definition-id "org.clojure/clojure/clojure.core/assoc/0"
                                       :body "Here is a version that will create a vector when the key is numerical. This may be useful instead of throwing an IndexOutOfBoundsException.\n```clojure\n(defn assoc-in-idx [m [k & ks] v]\n  (let [value (get m k (when (number? (first ks)) []))\n    m (if (and (vector? m) (number? k) (-> m count (< k)))\n        (reduce (fn [m _] (conj m nil)) m (range (count m) k))\n        m)\n    v (if ks\n        (assoc-in-idx value ks v)\n        v)]\n    (assoc m k v)))\n```\n> copied from  [clojuredocs.org](https://clojuredocs.org/clojure.core/assoc) for test"
                                       :created-at #inst "2024-03-20T14:09:03.779336000-00:00"
                                       :author {:author-id #uuid "36a91f13-2cf9-4b80-a9f0-619b5dbe6ec5"
                                                :login "rafaeldelboni"
                                                :account-source "github"
                                                :avatar-url "https://avatars.githubusercontent.com/u/1683898?v=4"
                                                :created-at #inst "2024-03-11T14:16:54.404609000-00:00"}}]
                              :examples [{:example-id #uuid "20292593-cad2-45e9-9c7f-6873863f8a8b"
                                          :definition-id "org.clojure/clojure/clojure.core/assoc/0"
                                          :body ";;assoc applied to a vector\n\n(def my-vec [1 2 5 6 8 9])\n\n(assoc my-vec 0 77)\n;;[77 2 5 6 8 9]"
                                          :created-at #inst "2024-03-20T14:09:23.011242000-00:00"
                                          :author {:author-id #uuid "36a91f13-2cf9-4b80-a9f0-619b5dbe6ec5"
                                                   :login "rafaeldelboni"
                                                   :account-source "github"
                                                   :avatar-url "https://avatars.githubusercontent.com/u/1683898?v=4"
                                                   :created-at #inst "2024-03-11T14:16:54.404609000-00:00"}
                                          :editors [{:author-id #uuid "36a91f13-2cf9-4b80-a9f0-619b5dbe6ec5"
                                                     :login "rafaeldelboni"
                                                     :account-source "github"
                                                     :avatar-url "https://avatars.githubusercontent.com/u/1683898?v=4"
                                                     :created-at #inst "2024-03-11T14:16:54.404609000-00:00"
                                                     :edited-at #inst "2024-03-20T14:09:23.011242000-00:00"}]}]
                              :see-alsos [{:see-also-id #uuid "393ab547-3bb6-406f-9fce-aa1098fdd7c8"
                                           :definition-id "org.clojure/clojure/clojure.core/assoc/0"
                                           :definition-id-to "org.clojure/clojure/clojure.core/dissoc/0"
                                           :created-at #inst "2024-03-21T10:07:35.853221000-00:00"
                                           :author {:author-id #uuid "36a91f13-2cf9-4b80-a9f0-619b5dbe6ec5"
                                                    :login "rafaeldelboni"
                                                    :account-source "github"
                                                    :avatar-url "https://avatars.githubusercontent.com/u/1683898?v=4"
                                                    :created-at #inst "2024-03-11T14:16:54.404609000-00:00"}}
                                          {:see-also-id #uuid "c71ce7d0-d739-43ad-9360-23f28d196af4"
                                           :definition-id "org.clojure/clojure/clojure.core/assoc/0"
                                           :definition-id-to "org.clojure/clojure/clojure.core/update/0"
                                           :created-at #inst "2024-03-21T10:08:04.647016000-00:00"
                                           :author {:author-id #uuid "36a91f13-2cf9-4b80-a9f0-619b5dbe6ec5"
                                                    :login "rafaeldelboni"
                                                    :account-source "github"
                                                    :avatar-url "https://avatars.githubusercontent.com/u/1683898?v=4"
                                                    :created-at #inst "2024-03-11T14:16:54.404609000-00:00"}}
                                          {:see-also-id #uuid "e64be101-17c8-454c-935c-5296a1588f58"
                                           :definition-id "org.clojure/clojure/clojure.core/assoc/0"
                                           :definition-id-to "org.clojure/clojure/clojure.core/assoc-in/0"
                                           :created-at #inst "2024-03-21T11:01:42.085837000-00:00"
                                           :author {:author-id #uuid "36a91f13-2cf9-4b80-a9f0-619b5dbe6ec5"
                                                    :login "rafaeldelboni"
                                                    :account-source "github"
                                                    :avatar-url "https://avatars.githubusercontent.com/u/1683898?v=4"
                                                    :created-at #inst "2024-03-11T14:16:54.404609000-00:00"}}]}
                             {:definition-id "org.clojure/clojure/clojure.core/keep/0"
                              :notes []
                              :examples [{:example-id #uuid "2a0253a8-6edf-4f94-9b63-c9f558064c55"
                                          :definition-id "org.clojure/clojure/clojure.core/keep/0"
                                          :body "; removes nil from list or vector\n(keep identity [:a :b nil :d nil :f])\n; => (:a :b :d :f)"
                                          :created-at #inst "2024-03-27T11:41:57.364949000-00:00"
                                          :author {:author-id #uuid "36a91f13-2cf9-4b80-a9f0-619b5dbe6ec5"
                                                   :login "rafaeldelboni"
                                                   :account-source "github"
                                                   :avatar-url "https://avatars.githubusercontent.com/u/1683898?v=4"
                                                   :created-at #inst "2024-03-11T14:16:54.404609000-00:00"}
                                          :editors [{:author-id #uuid "36a91f13-2cf9-4b80-a9f0-619b5dbe6ec5"
                                                     :login "rafaeldelboni"
                                                     :account-source "github"
                                                     :avatar-url "https://avatars.githubusercontent.com/u/1683898?v=4"
                                                     :created-at #inst "2024-03-11T14:16:54.404609000-00:00"
                                                     :edited-at #inst "2024-03-27T11:41:57.364949000-00:00"}]}]
                              :see-alsos [{:see-also-id #uuid "2657682d-fb87-4b71-807d-9ba86c9160ba"
                                           :definition-id "org.clojure/clojure/clojure.core/keep/0"
                                           :definition-id-to "org.clojure/clojure/clojure.core/remove/0"
                                           :created-at #inst "2024-03-27T11:43:32.582037000-00:00"
                                           :author {:author-id #uuid "36a91f13-2cf9-4b80-a9f0-619b5dbe6ec5"
                                                    :login "rafaeldelboni"
                                                    :account-source "github"
                                                    :avatar-url "https://avatars.githubusercontent.com/u/1683898?v=4"
                                                    :created-at #inst "2024-03-11T14:16:54.404609000-00:00"}}]}
                             {:definition-id "org.clojure/clojure/clojure.core/take/0"
                              :notes []
                              :examples [{:example-id #uuid "dcaccd23-50fe-4a10-a66b-611fc4e776d5"
                                          :definition-id "org.clojure/clojure/clojure.core/take/0"
                                          :body "; 6 random integers (from 0 to 60)\n(take 6 (repeatedly #(rand-int 60)))"
                                          :created-at #inst "2024-03-28T15:36:49.571386000-00:00"
                                          :author {:author-id #uuid "36a91f13-2cf9-4b80-a9f0-619b5dbe6ec5"
                                                   :login "rafaeldelboni"
                                                   :account-source "github"
                                                   :avatar-url "https://avatars.githubusercontent.com/u/1683898?v=4"
                                                   :created-at #inst "2024-03-11T14:16:54.404609000-00:00"}
                                          :editors [{:author-id #uuid "36a91f13-2cf9-4b80-a9f0-619b5dbe6ec5"
                                                     :login "rafaeldelboni"
                                                     :account-source "github"
                                                     :avatar-url "https://avatars.githubusercontent.com/u/1683898?v=4"
                                                     :created-at #inst "2024-03-11T14:16:54.404609000-00:00"
                                                     :edited-at #inst "2024-03-28T15:36:49.571386000-00:00"}]}]
                              :see-alsos [{:see-also-id #uuid "8d18bf94-195e-4459-81ad-f3a0fcb2b85d"
                                           :definition-id "org.clojure/clojure/clojure.core/take/0"
                                           :definition-id-to "org.clojure/clojure/clojure.core/repeatedly/0"
                                           :created-at #inst "2024-03-28T15:37:09.314558000-00:00"
                                           :author {:author-id #uuid "36a91f13-2cf9-4b80-a9f0-619b5dbe6ec5"
                                                    :login "rafaeldelboni"
                                                    :account-source "github"
                                                    :avatar-url "https://avatars.githubusercontent.com/u/1683898?v=4"
                                                    :created-at #inst "2024-03-11T14:16:54.404609000-00:00"}}]}
                             {:definition-id "org.clojure/clojure/clojure.core/remove/0"
                              :notes []
                              :examples []
                              :see-alsos [{:see-also-id #uuid "3daba272-d969-4e55-92f4-052c7ce64a64"
                                           :definition-id "org.clojure/clojure/clojure.core/remove/0"
                                           :definition-id-to "org.clojure/clojure/clojure.core/keep/0"
                                           :created-at #inst "2024-03-27T11:43:50.674432000-00:00"
                                           :author {:author-id #uuid "36a91f13-2cf9-4b80-a9f0-619b5dbe6ec5"
                                                    :login "rafaeldelboni"
                                                    :account-source "github"
                                                    :avatar-url "https://avatars.githubusercontent.com/u/1683898?v=4"
                                                    :created-at #inst "2024-03-11T14:16:54.404609000-00:00"}}]}]})

; TODO move to adapters & unit test
(defn author-socials->summary [socials]
  (reduce (fn [{notes-a :notes examples-a :examples see-alsos-a :see-alsos
                :as accum}
               {notes-c :notes examples-c :examples see-alsos-c :see-alsos}]
            (assoc accum
                   :notes (+ notes-a (count notes-c))
                   :examples (+ examples-a (count examples-c))
                   :see-alsos (+ see-alsos-a (count see-alsos-c))))
          {:notes 0 :examples 0 :see-alsos 0}
          socials))

; TODO move to adapters & unit test
(defn author->string-summary
  [{:keys [socials]}]
  ; TODO add cases when only authored examples, notes or see alsos
  ; case when not authored nothing
  (let [{:keys [examples notes see-alsos]} (author-socials->summary socials)]
    (str "This has user has authored "
         examples " examples, "
         notes " notes and "
         see-alsos " see alsos.")))

(defnc author-detail-page []
  (let [{:keys [loading? error value]} {:loading? false :error nil :value author-value}
        {:keys [login account-source avatar-url socials]} value]

    ($ Container {:p "md"}
      (if loading?

        ($ LoadingOverlay {:visible loading? :zIndex 1000
                           :overlayProps #js {:radius "sm" :blur 2}})

        (if error
          ($ Alert {:variant "light" :color "red"
                    :radius "md" :title "Error"
                    :icon ($ IconInfoCircle)}
            (str error))

          (dom/div

            ($ Center
              ($ Group {:wrap "nowrap"}
                ($ Avatar {:src avatar-url
                           :size 200
                           :radius 200})
                (dom/div
                  ($ Title {:order 3}
                    login)

                  ($ Text {:fz "xs" :tt "uppercase" :fw 700 :c "dimmed"}
                    (name account-source))

                  ($ Space {:h "sm"})

                  ($ Text {:fz "lg" :fw 500}
                    (author->string-summary value)))))

            ($ Space {:h "lg"})

            (when socials
              ;; todo move to own component
              (dom/div
                ($ Title {:order 2} "Interactions")
                ($ Space {:h "md"})
                ($ Group
                  ($ Grid {:data-testid "author-grid"}
                    (map (fn [{:keys [definition-id examples notes see-alsos]}]
                           ($ (-> Grid .-Col) {:key definition-id}
                             (dom/div
                               ($ safe-anchor {:fz "xl" :fw 500
                                               :href (str "/" definition-id)}
                                 (str/replace definition-id #"/0$" ""))

                               (when (seq examples)
                                 ;; todo move to own component
                                 (dom/div
                                   ($ Title {:style #js {:paddingTop 10} :order 4}
                                      "Examples")
                                   (map (fn [{:keys [example-id body]}]
                                          ($ Box {:key example-id
                                                  :w #js {:base 350 :xs 400
                                                          :sm 600 :md 800
                                                          :lg 900 :xl 1000}}
                                            ($ Text {:truncate "end"} body)))
                                     examples)))

                               (when (seq see-alsos)
                                 ;; todo move to own component
                                 (dom/div
                                   ($ Title {:style #js {:paddingTop 10} :order 4}
                                      "See Alsos")
                                   (map (fn [{:keys [see-also-id definition-id-to]}]
                                          ($ Box {:key see-also-id
                                                  :w #js {:base 350 :xs 400
                                                          :sm 600 :md 800
                                                          :lg 900 :xl 1000}}
                                            ($ Text {:truncate "end"}
                                              (str/replace definition-id-to #"/0$" "\n"))))
                                     see-alsos)))

                               (when (seq notes)
                                 ;; todo move to own component
                                 (dom/div
                                   ($ Title {:style #js {:paddingTop 10} :order 4}
                                      "Notes")
                                   (map (fn [{:keys [note-id body]}]
                                          ($ Box {:key note-id
                                                  :w #js {:base 350 :xs 400
                                                          :sm 600 :md 800
                                                          :lg 900 :xl 1000}}
                                            ($ Text {:truncate "end"}
                                              body)))
                                     notes))))))

                      socials)))))

            ($ back-to-top)))))))
