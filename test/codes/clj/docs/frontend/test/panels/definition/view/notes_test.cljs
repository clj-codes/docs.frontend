(ns codes.clj.docs.frontend.test.panels.definition.view.notes-test
  (:require ["@testing-library/react" :as tlr]
            [cljs.test :refer [async deftest is testing use-fixtures]]
            [codes.clj.docs.frontend.infra.auth.state :as auth.state]
            [codes.clj.docs.frontend.panels.definition.state :as definition.state]
            [codes.clj.docs.frontend.panels.definition.view :refer [definition-detail]]
            [codes.clj.docs.frontend.test.aux.fixtures.definition :as fixtures]
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

(deftest social-notes-list-view-test
  (testing "definition-social should render definition social notes card"
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
                card (tl/wait-for #(.findByTestId ^js/Object view "card-notes"))]

          (is (= ["card-note-8b81ce0d-20fd-43e7-a79a-a9edbb0f162a"
                  "card-note-2a704396-78d2-4153-9a0a-31fa1c87e9c8"]
                 (->> (.querySelectorAll card ".card-note")
                      (map #(.-id %)))))

          (is (= []
                 (->> (.querySelectorAll card ".author-edit-delete-note")
                      (map #(.-textContent %)))))

          (auth.state/user {:error nil
                            :loading? false
                            :value fixtures.user/user-2})

          (is (= ["editdelete"
                  "editdelete"]
                 (->> (.querySelectorAll card ".author-edit-delete-note")
                      (map #(.-textContent %)))))

          (done))
        (fn [err] (is (= nil err))
          (done))))))

(deftest notes-create-view-test
  (testing "definition-social should render and create note"
    ; mock http request
    (mock-http-with (merge base-http-mocks
                           {"social/note/"
                            {:lag 0
                             :status 200
                             :body {:body "my note 1",
                                    :created-at #inst "2024-03-15T13:17:45.414-00:00"
                                    :definition-id "org.clojure/clojure/clojure.core.server/prepl/0"
                                    :note-id #uuid "d5c4870e-5d92-4d4b-8614-f54995a259d3"}}}))

    ; call initial db fetch
    (definition.state/definition-docs-fetch "org.clojure" "clojure" "clojure.core.server" "prepl" 0)
    (definition.state/definition-social-fetch "org.clojure" "clojure" "clojure.core.server" "prepl" 0)
    (auth.state/user {:error nil
                      :loading? false
                      :value fixtures.user/user})

    (async done
      (p/catch
        (p/let [view (tl/mantine-render ($ definition-detail))
                add-button (tl/wait-for #(.findByTestId ^js/Object (tlr/within js/document) "add-note-btn"))
                _click (tl/wait-for #(tl/click add-button))
                input (tl/wait-for #(.findByTestId ^js/Object (tlr/within js/document) "markdown-editor-textarea"))
                _change (tl/wait-for #(tl/change input "my note 1"))
                save-button (tl/wait-for #(.findByTestId ^js/Object (tlr/within js/document) "editor-base-save-btn"))
                _save-click (tl/wait-for #(tl/click save-button))
                card (tl/wait-for #(.findByTestId ^js/Object view "card-notes"))]

          (is (= ["card-note-8b81ce0d-20fd-43e7-a79a-a9edbb0f162a"
                  "card-note-d5c4870e-5d92-4d4b-8614-f54995a259d3"
                  "card-note-2a704396-78d2-4153-9a0a-31fa1c87e9c8"]
                 (->> (.querySelectorAll card ".card-note")
                      (map #(.-id %)))))

          (is (match? [{:path "document/definition/org.clojure/clojure/clojure.core.server/prepl/0"
                        :method :get}
                       {:path "social/definition/org.clojure/clojure/clojure.core.server/prepl/0"
                        :method :get}
                       {:path "social/note/"
                        :headers {"authorization" "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"}
                        :method :post
                        :body {:definition-id "org.clojure/clojure/clojure.core.server/prepl/0"
                               :body "my note 1"}}]
                      (get-mock-http-requests)))

          (done))
        (fn [err] (is (= nil err))
          (done))))))

(deftest social-notes-edit-view-test
  (testing "definition-social should render and edit note"
    ; mock http request
    (mock-http-with (merge base-http-mocks
                           {"social/note/"
                            {:lag 0
                             :status 200
                             :body {:body "edited note",
                                    :created-at #inst "2024-03-15T13:17:45.414-00:00"
                                    :definition-id "org.clojure/clojure/clojure.core.server/prepl/0"
                                    :note-id #uuid "8b81ce0d-20fd-43e7-a79a-a9edbb0f162a"}}}))

    ; call initial db fetch
    (definition.state/definition-docs-fetch "org.clojure" "clojure" "clojure.core.server" "prepl" 0)
    (definition.state/definition-social-fetch "org.clojure" "clojure" "clojure.core.server" "prepl" 0)
    (auth.state/user {:error nil
                      :loading? false
                      :value fixtures.user/user-2})

    (async done
      (p/catch
        (p/let [view (tl/mantine-render ($ definition-detail))
                cards (tl/wait-for #(.findByTestId ^js/Object view "card-notes"))
                edit-button (tl/wait-for #(.findByTestId ^js/Object view "note-author-edit-button-8b81ce0d-20fd-43e7-a79a-a9edbb0f162a"))
                _click (tl/wait-for #(tl/click edit-button))
                input (tl/wait-for #(.findByTestId ^js/Object (tlr/within js/document) "markdown-editor-textarea"))
                _change (tl/wait-for #(tl/change input "edited note"))
                save-button (tl/wait-for #(.findByTestId ^js/Object (tlr/within js/document) "editor-base-save-btn"))
                _save-click (tl/wait-for #(tl/click save-button))
                card-after (tl/wait-for #(.findByTestId ^js/Object view "card-notes"))]

          (is (= ["note-author-edit-button-8b81ce0d-20fd-43e7-a79a-a9edbb0f162a"
                  "note-author-edit-button-2a704396-78d2-4153-9a0a-31fa1c87e9c8"]
                 (->> (.querySelectorAll cards ".note-author-edit-button")
                      (map #(.-id %)))))

          (is (match? [#"edited note"
                       #"the API is blurry When applied to a vector"]
                      (->> (.querySelectorAll card-after ".markdown-viewer")
                           (map #(.-textContent %)))))

          (is (match? [{:path "document/definition/org.clojure/clojure/clojure.core.server/prepl/0"
                        :method :get}
                       {:path "social/definition/org.clojure/clojure/clojure.core.server/prepl/0"
                        :method :get}
                       {:path "social/note/"
                        :headers {"authorization" "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"}
                        :method :put
                        :body {:note-id #uuid "8b81ce0d-20fd-43e7-a79a-a9edbb0f162a"
                               :body "edited note"}}]
                      (get-mock-http-requests)))

          (done))
        (fn [err] (is (= nil err))
          (done))))))

(deftest social-notes-delete-view-test
  (testing "definition-social should render and delete note"
    ; mock http request
    (mock-http-with (merge base-http-mocks
                           {"social/note/8b81ce0d-20fd-43e7-a79a-a9edbb0f162a"
                            {:lag 0
                             :status 200
                             :body {:note-id #uuid "8b81ce0d-20fd-43e7-a79a-a9edbb0f162a"}}}))

    ; call initial db fetch
    (definition.state/definition-docs-fetch "org.clojure" "clojure" "clojure.core.server" "prepl" 0)
    (definition.state/definition-social-fetch "org.clojure" "clojure" "clojure.core.server" "prepl" 0)
    (auth.state/user {:error nil
                      :loading? false
                      :value fixtures.user/user-2})

    (async done
      (p/catch
        (p/let [view (tl/mantine-render ($ definition-detail))
                cards (tl/wait-for #(.findByTestId ^js/Object view "card-notes"))
                delete-button (tl/wait-for #(.findByTestId ^js/Object view "note-author-delete-button-8b81ce0d-20fd-43e7-a79a-a9edbb0f162a"))
                _click (tl/wait-for #(tl/click delete-button))
                yes-button (tl/wait-for #(.findByTestId ^js/Object (tlr/within js/document) "definition-delete-alert-yes-btn"))
                _yes-click (tl/wait-for #(tl/click yes-button))
                card-after (tl/wait-for #(.findByTestId ^js/Object view "card-notes"))]

          (is (= ["note-author-delete-button-8b81ce0d-20fd-43e7-a79a-a9edbb0f162a"
                  "note-author-delete-button-2a704396-78d2-4153-9a0a-31fa1c87e9c8"]
                 (->> (.querySelectorAll cards ".note-author-delete-button")
                      (map #(.-id %)))))

          (is (match? [#"the API is blurry When applied to a vector"]
                      (->> (.querySelectorAll card-after ".markdown-viewer")
                           (map #(.-textContent %)))))

          (is (match? [{:path "document/definition/org.clojure/clojure/clojure.core.server/prepl/0"
                        :method :get}
                       {:path "social/definition/org.clojure/clojure/clojure.core.server/prepl/0"
                        :method :get}
                       {:path "social/note/8b81ce0d-20fd-43e7-a79a-a9edbb0f162a"
                        :headers {"authorization" "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"}
                        :method :delete}]
                      (get-mock-http-requests)))

          (done))
        (fn [err] (is (= nil err))
          (done))))))
