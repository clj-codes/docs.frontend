(ns codes.clj.docs.frontend.test.aux.fixtures.dashboards)

(def latest-interactions
  [{:see-also-id #uuid "6e7aa601-1ce6-4801-83a3-5bbad76084d0", :definition-id "org.clojure/clojure/clojure.core/contains?/0", :definition-id-to "org.clojure/clojure/clojure.string/includes?/0", :created-at #inst "2024-05-03T21:59:49.844695000-00:00", :author {:author-id #uuid "b58e81a2-3647-4c71-aa4f-1499cff15c59", :login "strobelt", :account-source "github", :avatar-url "https://avatars.githubusercontent.com/u/669994?v=4", :created-at #inst "2024-05-03T21:43:23.766762000-00:00"}}
   {:example-id #uuid "14f2c72d-e0b3-466d-bd69-8dfdf7bb4bff", :definition-id "org.clojure/clojure/clojure.core/contains?/0", :body ";; contains? looks for keys in a map and not values in a sequence\n;; so this works as expected\n(contains? {:a 1} :a) ;=> true\n\n;; but this doesn't\n(contains? [:a :b :c] :b) ;=> false\n(contains? \"Clojure is awesome\" \"awesome\") ;=> false\n\n;; For strings you can use clojure.string/includes?\n(clojure.string/includes? \"Clojure is awesome\" \"awesome\") ;=> true\n\n;; And for vectors you can use java's .indexOf\n(.indexOf [:a :b :c] :b) ;=> 1", :created-at #inst "2024-05-03T21:59:38.985240000-00:00", :author {:author-id #uuid "b58e81a2-3647-4c71-aa4f-1499cff15c59", :login "strobelt", :account-source "github", :avatar-url "https://avatars.githubusercontent.com/u/669994?v=4", :created-at #inst "2024-05-03T21:43:23.766762000-00:00"}, :editors [{:author-id #uuid "b58e81a2-3647-4c71-aa4f-1499cff15c59", :login "strobelt", :account-source "github", :avatar-url "https://avatars.githubusercontent.com/u/669994?v=4", :created-at #inst "2024-05-03T21:43:23.766762000-00:00", :edited-at #inst "2024-05-03T21:59:38.985240000-00:00"}]}
   {:example-id #uuid "c9e76ca3-f41c-4846-9d7b-e563fd79dc45", :definition-id "org.clojure/clojure/clojure.core/defmulti/0", :body ";; Define the multimethod\n(defmulti my-test\n          (fn [param1 param2] {:param1 param1 :param2 param2}))\n\n;; Defining handlers/implementation for resulting dispatch values  \n(s/defmethod my-test {:param1 :something\n                      :param2 :something-else}\n  [param1 :- s/Keyword\n   param2 :- s/Keyword]\n  (+ 3 4))\n\n(s/defmethod my-test {:param1 :this\n                      :param2 :that}\n  [param1 :- s/Keyword\n   param2 :- s/Keyword]\n  (+ 4 5))\n\n(s/defmethod my-test :default\n  [param1 :- s/Keyword\n   param2 :- s/Keyword]\n  \"ERROR\")\n\n;; Invoking it\n(my-test :something :something-else)\n;; => 7\n\n(my-test :something :undefined)\n;; Should go do the default because no other implementation was found, return \n;; => \"ERROR\"\n", :created-at #inst "2024-04-03T20:17:11.131558000-00:00", :author {:author-id #uuid "05c01718-00b0-4ebe-a28b-280cd1be9a31", :login "kroncatti", :account-source "github", :avatar-url "https://avatars.githubusercontent.com/u/56690659?v=4", :created-at #inst "2024-03-25T16:07:54.244916000-00:00"}, :editors [{:author-id #uuid "05c01718-00b0-4ebe-a28b-280cd1be9a31", :login "kroncatti", :account-source "github", :avatar-url "https://avatars.githubusercontent.com/u/56690659?v=4", :created-at #inst "2024-03-25T16:07:54.244916000-00:00", :edited-at #inst "2024-04-03T20:12:24.936298000-00:00"} {:author-id #uuid "05c01718-00b0-4ebe-a28b-280cd1be9a31", :login "kroncatti", :account-source "github", :avatar-url "https://avatars.githubusercontent.com/u/56690659?v=4", :created-at #inst "2024-03-25T16:07:54.244916000-00:00", :edited-at #inst "2024-04-03T20:17:11.131558000-00:00"}]}
   {:see-also-id #uuid "8d18bf94-195e-4459-81ad-f3a0fcb2b85d", :definition-id "org.clojure/clojure/clojure.core/take/0", :definition-id-to "org.clojure/clojure/clojure.core/repeatedly/0", :created-at #inst "2024-03-28T15:37:09.314558000-00:00", :author {:author-id #uuid "36a91f13-2cf9-4b80-a9f0-619b5dbe6ec5", :login "rafaeldelboni", :account-source "github", :avatar-url "https://avatars.githubusercontent.com/u/1683898?v=4", :created-at #inst "2024-03-11T14:16:54.404609000-00:00"}}
   {:example-id #uuid "dcaccd23-50fe-4a10-a66b-611fc4e776d5", :definition-id "org.clojure/clojure/clojure.core/take/0", :body "; 6 random integers (from 0 to 60)\n(take 6 (repeatedly #(rand-int 60)))", :created-at #inst "2024-03-28T15:36:49.571386000-00:00", :author {:author-id #uuid "36a91f13-2cf9-4b80-a9f0-619b5dbe6ec5", :login "rafaeldelboni", :account-source "github", :avatar-url "https://avatars.githubusercontent.com/u/1683898?v=4", :created-at #inst "2024-03-11T14:16:54.404609000-00:00"}, :editors [{:author-id #uuid "36a91f13-2cf9-4b80-a9f0-619b5dbe6ec5", :login "rafaeldelboni", :account-source "github", :avatar-url "https://avatars.githubusercontent.com/u/1683898?v=4", :created-at #inst "2024-03-11T14:16:54.404609000-00:00", :edited-at #inst "2024-03-28T15:36:49.571386000-00:00"}]}
   {:see-also-id #uuid "3daba272-d969-4e55-92f4-052c7ce64a64", :definition-id "org.clojure/clojure/clojure.core/remove/0", :definition-id-to "org.clojure/clojure/clojure.core/keep/0", :created-at #inst "2024-03-27T11:43:50.674432000-00:00", :author {:author-id #uuid "36a91f13-2cf9-4b80-a9f0-619b5dbe6ec5", :login "rafaeldelboni", :account-source "github", :avatar-url "https://avatars.githubusercontent.com/u/1683898?v=4", :created-at #inst "2024-03-11T14:16:54.404609000-00:00"}}
   {:see-also-id #uuid "2657682d-fb87-4b71-807d-9ba86c9160ba", :definition-id "org.clojure/clojure/clojure.core/keep/0", :definition-id-to "org.clojure/clojure/clojure.core/remove/0", :created-at #inst "2024-03-27T11:43:32.582037000-00:00", :author {:author-id #uuid "36a91f13-2cf9-4b80-a9f0-619b5dbe6ec5", :login "rafaeldelboni", :account-source "github", :avatar-url "https://avatars.githubusercontent.com/u/1683898?v=4", :created-at #inst "2024-03-11T14:16:54.404609000-00:00"}}
   {:example-id #uuid "2a0253a8-6edf-4f94-9b63-c9f558064c55", :definition-id "org.clojure/clojure/clojure.core/keep/0", :body "; removes nil from list or vector\n(keep identity [:a :b nil :d nil :f])\n; => (:a :b :d :f)", :created-at #inst "2024-03-27T11:41:57.364949000-00:00", :author {:author-id #uuid "36a91f13-2cf9-4b80-a9f0-619b5dbe6ec5", :login "rafaeldelboni", :account-source "github", :avatar-url "https://avatars.githubusercontent.com/u/1683898?v=4", :created-at #inst "2024-03-11T14:16:54.404609000-00:00"}, :editors [{:author-id #uuid "36a91f13-2cf9-4b80-a9f0-619b5dbe6ec5", :login "rafaeldelboni", :account-source "github", :avatar-url "https://avatars.githubusercontent.com/u/1683898?v=4", :created-at #inst "2024-03-11T14:16:54.404609000-00:00", :edited-at #inst "2024-03-27T11:41:57.364949000-00:00"}]}
   {:example-id #uuid "a00a79c8-d3a2-44c8-8688-b61b4a7672b9", :definition-id "nubank/matcher-combinators/matcher-combinators.matchers/embeds/0", :body "(flow \"Return transactions by category\"\n        (match? {:status 200 :body {:transactions (m/embeds [{:category \"Food\"}])}}\n                (servlet/request {:method :get :uri \"/transaction/:category\" \n                                  :replace {:category \"Food\"}})", :created-at #inst "2024-03-26T20:43:52.489997000-00:00", :author {:author-id #uuid "750b1ede-fcb5-4989-bb56-f6bbad2b9e66", :login "dimmyjr-nu", :account-source "github", :avatar-url "https://avatars.githubusercontent.com/u/87130196?v=4", :created-at #inst "2024-03-25T19:20:38.580572000-00:00"}, :editors [{:author-id #uuid "750b1ede-fcb5-4989-bb56-f6bbad2b9e66", :login "dimmyjr-nu", :account-source "github", :avatar-url "https://avatars.githubusercontent.com/u/87130196?v=4", :created-at #inst "2024-03-25T19:20:38.580572000-00:00", :edited-at #inst "2024-03-26T20:43:52.489997000-00:00"}]}
   {:note-id #uuid "1fcfb904-306c-4aa7-94e1-755dcaab45ab", :definition-id "org.clojure/clojure/clojure.core/assoc/0", :body "just be aware that assoc can do this:\n\n```clojure\n(assoc {} :a nil) ;; {:a nil}\n```", :created-at #inst "2024-03-21T10:21:18.790781000-00:00", :author {:author-id #uuid "86ebb308-6839-46d0-b3da-b3832c94ad9e", :login "matheusfrancisco", :account-source "github", :avatar-url "https://avatars.githubusercontent.com/u/6428732?v=4", :created-at #inst "2024-03-16T08:24:15.745224000-00:00"}}])

(def top-authors
  [{:author-id #uuid "36a91f13-2cf9-4b80-a9f0-619b5dbe6ec5", :login "rafaeldelboni", :account-source "github", :avatar-url "https://avatars.githubusercontent.com/u/1683898?v=4", :created-at #inst "2024-03-11T14:16:54.404609000-00:00", :interactions 10}
   {:author-id #uuid "3b5b49a2-a731-4c12-81c0-88b1d74415a2", :login "vloth", :account-source "github", :avatar-url "https://avatars.githubusercontent.com/u/49727703?v=4", :created-at #inst "2024-03-16T10:43:31.732700000-00:00", :interactions 3}
   {:author-id #uuid "86ebb308-6839-46d0-b3da-b3832c94ad9e", :login "matheusfrancisco", :account-source "github", :avatar-url "https://avatars.githubusercontent.com/u/6428732?v=4", :created-at #inst "2024-03-16T08:24:15.745224000-00:00", :interactions 2}
   {:author-id #uuid "b58e81a2-3647-4c71-aa4f-1499cff15c59", :login "strobelt", :account-source "github", :avatar-url "https://avatars.githubusercontent.com/u/669994?v=4", :created-at #inst "2024-05-03T21:43:23.766762000-00:00", :interactions 2}
   {:author-id #uuid "05c01718-00b0-4ebe-a28b-280cd1be9a31", :login "kroncatti", :account-source "github", :avatar-url "https://avatars.githubusercontent.com/u/56690659?v=4", :created-at #inst "2024-03-25T16:07:54.244916000-00:00", :interactions 2}
   {:author-id #uuid "750b1ede-fcb5-4989-bb56-f6bbad2b9e66", :login "dimmyjr-nu", :account-source "github", :avatar-url "https://avatars.githubusercontent.com/u/87130196?v=4", :created-at #inst "2024-03-25T19:20:38.580572000-00:00", :interactions 1}
   {:author-id #uuid "d6e7b8d2-590d-4e53-9691-c8a900703d16", :login "daveliepmann", :account-source "github", :avatar-url "https://avatars.githubusercontent.com/u/974443?v=4", :created-at #inst "2024-03-25T15:30:44.171709000-00:00", :interactions 1}])
