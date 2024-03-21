(ns codes.clj.docs.frontend.test.panels.definition.view.see-alsos-test
  (:require ["@testing-library/react" :as tlr]
            [cljs.test :refer [async deftest is testing use-fixtures]]
            [codes.clj.docs.frontend.infra.auth.state :as auth.state]
            [codes.clj.docs.frontend.panels.definition.state :as definition.state]
            [codes.clj.docs.frontend.panels.definition.view :refer [definition-detail]]
            [codes.clj.docs.frontend.test.aux.fixtures.definition :as fixtures]
            [codes.clj.docs.frontend.test.aux.fixtures.search-results :as fixtures.search-results]
            [codes.clj.docs.frontend.test.aux.fixtures.user :as fixtures.user]
            [codes.clj.docs.frontend.test.aux.init :refer [async-cleanup
                                                           async-setup
                                                           get-mock-http-requests
                                                           mock-http-with]]
            [codes.clj.docs.frontend.test.aux.testing-library :as tl]
            [helix.core :refer [$]]
            [matcher-combinators.test :refer [match?]]
            [promesa.core :as p]))

(use-fixtures :each
  {:before async-setup
   :after async-cleanup})

(def base-http-mocks
  {"document/definition/org.clojure/clojure/clojure.core.server/prepl/0"
   {:lag 0
    :status 200
    :body fixtures/definition-docs}
   "social/definition/org.clojure/clojure/clojure.core.server/prepl/0"
   {:lag 0
    :status 200
    :body fixtures/definition-social}})

(deftest social-see-alsos-list-view-test
  (testing "definition-social should render definition social see-alsos card"
    ; mock http request
    (mock-http-with base-http-mocks)

    ; call initial db fetch
    (definition.state/definition-docs-fetch "org.clojure" "clojure" "clojure.core.server" "prepl" 0)
    (definition.state/definition-social-fetch "org.clojure" "clojure" "clojure.core.server" "prepl" 0)
    (auth.state/user {:error nil
                      :loading? false
                      :value fixtures.user/user})

    (async done
      (p/catch
        (p/let [view (tl/mantine-render ($ definition-detail))
                card (tl/wait-for #(.findByTestId ^js/Object view "card-see-alsos"))]

          (is (= ["card-see-also-750941da-15c5-4fbe-8ac2-1a3bc890bb07"
                  "card-see-also-49b9f1a2-84e9-45f7-9310-10aaa396b87f"
                  "card-see-also-8b3fa426-9bfa-4868-ab0d-929c8bd5605c"]
                 (->> (.querySelectorAll card ".card-see-also")
                      (map #(.-id %)))))

          (is (= []
                 (->> (.querySelectorAll card ".author-edit-delete-see-also")
                      (map #(.-textContent %)))))

          (auth.state/user {:error nil
                            :loading? false
                            :value fixtures.user/user-2})

          (is (= ["delete"
                  "delete"
                  "delete"]
                 (->> (.querySelectorAll card ".author-edit-delete-see-also")
                      (map #(.-textContent %)))))

          (done))
        (fn [err] (is (= nil err))
          (done))))))

(deftest see-alsos-create-view-test
  (testing "definition-social should render and create see-also"
    ; mock http request
    (mock-http-with (merge base-http-mocks
                           {"document/search/"
                            {:lag 0
                             :status 200
                             :body fixtures.search-results/search-results}
                            "social/see-also/"
                            {:lag 0
                             :status 200
                             :body {:definition-id-to "org.clojure/clojure/clojure.core.server/stop-server/0",
                                    :created-at #inst "2024-03-15T13:17:45.414-00:00"
                                    :definition-id "org.clojure/clojure/clojure.core.server/prepl/0"
                                    :see-also-id #uuid "d5c4870e-5d92-4d4b-8614-f54995a259d3"}}}))

    ; call initial db fetch
    (definition.state/definition-docs-fetch "org.clojure" "clojure" "clojure.core.server" "prepl" 0)
    (definition.state/definition-social-fetch "org.clojure" "clojure" "clojure.core.server" "prepl" 0)
    (auth.state/user {:error nil
                      :loading? false
                      :value fixtures.user/user})

    (async done
      (p/catch
        (p/let [view (tl/mantine-render ($ definition-detail))
                add-button (tl/wait-for #(.findByTestId ^js/Object (tlr/within js/document) "add-see-also-btn"))
                _click (tl/wait-for #(tl/click add-button))
                input (tl/wait-for #(.findByTestId ^js/Object (tlr/within js/document) "editor-see-also-textarea"))
                _change (tl/wait-for #(tl/change input "server"))
                option (tl/wait-for #(.findByText ^js/Object (tlr/within js/document) "stop-server"))
                _click-option (tl/wait-for #(tl/click option))
                save-button (tl/wait-for #(.findByTestId ^js/Object (tlr/within js/document) "editor-see-also-save-btn"))
                _save-click (tl/wait-for #(tl/click save-button))
                card (tl/wait-for #(.findByTestId ^js/Object view "card-see-alsos"))]

          (is (= ["card-see-also-d5c4870e-5d92-4d4b-8614-f54995a259d3"
                  "card-see-also-750941da-15c5-4fbe-8ac2-1a3bc890bb07"
                  "card-see-also-49b9f1a2-84e9-45f7-9310-10aaa396b87f"
                  "card-see-also-8b3fa426-9bfa-4868-ab0d-929c8bd5605c"]
                 (->> (.querySelectorAll card ".card-see-also")
                      (map #(.-id %)))))

          (is (match? [{:path "document/definition/org.clojure/clojure/clojure.core.server/prepl/0"
                        :method :get}
                       {:path "social/definition/org.clojure/clojure/clojure.core.server/prepl/0"
                        :method :get}
                       {:path "document/search/"
                        :method :get
                        :query-params {:q "" :top 30}}
                       {:path "social/see-also/"
                        :headers {"authorization" "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"}
                        :method :post
                        :body {:definition-id "org.clojure/clojure/clojure.core.server/prepl/0"
                               :definition-id-to "org.clojure/clojure/clojure.core.server/stop-server/0"}}]
                      (get-mock-http-requests)))

          (done))
        (fn [err] (is (= nil err))
          (done))))))

(deftest social-see-alsos-delete-view-test
  (testing "definition-social should render and delete see-also"
    ; mock http request
    (mock-http-with (merge base-http-mocks
                           {"social/see-also/750941da-15c5-4fbe-8ac2-1a3bc890bb07"
                            {:lag 0
                             :status 200
                             :body {:see-also-id #uuid "750941da-15c5-4fbe-8ac2-1a3bc890bb07"}}}))

    ; call initial db fetch
    (definition.state/definition-docs-fetch "org.clojure" "clojure" "clojure.core.server" "prepl" 0)
    (definition.state/definition-social-fetch "org.clojure" "clojure" "clojure.core.server" "prepl" 0)
    (auth.state/user {:error nil
                      :loading? false
                      :value fixtures.user/user-2})

    (async done
      (p/catch
        (p/let [view (tl/mantine-render ($ definition-detail))
                cards (tl/wait-for #(.findByTestId ^js/Object view "card-see-alsos"))
                delete-button (tl/wait-for #(.findByTestId ^js/Object view "see-also-author-delete-button-750941da-15c5-4fbe-8ac2-1a3bc890bb07"))
                _click (tl/wait-for #(tl/click delete-button))
                yes-button (tl/wait-for #(.findByTestId ^js/Object (tlr/within js/document) "definition-delete-alert-yes-btn"))
                _yes-click (tl/wait-for #(tl/click yes-button))
                card-after (tl/wait-for #(.findByTestId ^js/Object view "card-see-alsos"))]

          (is (= ["see-also-author-delete-button-750941da-15c5-4fbe-8ac2-1a3bc890bb07"
                  "see-also-author-delete-button-49b9f1a2-84e9-45f7-9310-10aaa396b87f"
                  "see-also-author-delete-button-8b3fa426-9bfa-4868-ab0d-929c8bd5605c"]
                 (->> (.querySelectorAll cards ".see-also-author-delete-button")
                      (map #(.-id %)))))

          (is (match? ["see-also-author-delete-button-49b9f1a2-84e9-45f7-9310-10aaa396b87f"
                       "see-also-author-delete-button-8b3fa426-9bfa-4868-ab0d-929c8bd5605c"]
                      (->> (.querySelectorAll card-after ".see-also-author-delete-button")
                           (map #(.-id %)))))

          (is (match? [{:path "document/definition/org.clojure/clojure/clojure.core.server/prepl/0"
                        :method :get}
                       {:path "social/definition/org.clojure/clojure/clojure.core.server/prepl/0"
                        :method :get}
                       {:path "social/see-also/750941da-15c5-4fbe-8ac2-1a3bc890bb07"
                        :headers {"authorization" "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"}
                        :method :delete}]
                      (get-mock-http-requests)))

          (done))
        (fn [err] (is (= nil err))
          (done))))))
