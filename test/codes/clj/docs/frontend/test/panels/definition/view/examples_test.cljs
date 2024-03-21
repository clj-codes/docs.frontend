(ns codes.clj.docs.frontend.test.panels.definition.view.examples-test
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

(deftest social-examples-list-view-test
  (testing "definition-social should render definition social examples card"
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
                card (tl/wait-for #(.findByTestId ^js/Object view "card-examples"))]

          (is (= ["card-example-f6867425-d71b-478e-ac83-6da49646482b"
                  "card-example-9b809144-48ea-4df6-975e-a4d67df0828f"]
                 (->> (.querySelectorAll card ".card-example")
                      (map #(.-id %)))))

          (is (= ["edit" "edit"]
                 (->> (.querySelectorAll card ".author-edit-delete-example")
                      (map #(.-textContent %)))))

          (auth.state/user {:error nil
                            :loading? false
                            :value fixtures.user/user-2})

          (is (= ["editdelete" "edit"]
                 (->> (.querySelectorAll card ".author-edit-delete-example")
                      (map #(.-textContent %)))))

          (done))
        (fn [err] (is (= nil err))
          (done))))))

(deftest examples-create-view-test
  (testing "definition-social should render and create example"
    ; mock http request
    (mock-http-with (merge base-http-mocks
                           {"social/example/"
                            {:lag 0
                             :status 200
                             :body {:body "my example 1",
                                    :created-at #inst "2024-03-15T13:17:45.414-00:00"
                                    :definition-id "org.clojure/clojure/clojure.core.server/prepl/0"
                                    :example-id #uuid "d5c4870e-5d92-4d4b-8614-f54995a259d3"}}}))

    ; call initial db fetch
    (definition.state/definition-docs-fetch "org.clojure" "clojure" "clojure.core.server" "prepl" 0)
    (definition.state/definition-social-fetch "org.clojure" "clojure" "clojure.core.server" "prepl" 0)
    (auth.state/user {:error nil
                      :loading? false
                      :value fixtures.user/user})

    (async done
      (p/catch
        (p/let [view (tl/mantine-render ($ definition-detail))
                add-button (tl/wait-for #(.findByTestId ^js/Object (tlr/within js/document) "add-example-btn"))
                _click (tl/wait-for #(tl/click add-button))
                input (tl/wait-for #(.findByTestId ^js/Object (tlr/within js/document) "markdown-editor-textarea"))
                _change (tl/wait-for #(tl/change input "my example 1"))
                save-button (tl/wait-for #(.findByTestId ^js/Object (tlr/within js/document) "editor-base-save-btn"))
                _save-click (tl/wait-for #(tl/click save-button))
                card (tl/wait-for #(.findByTestId ^js/Object view "card-examples"))]

          (is (= ["card-example-d5c4870e-5d92-4d4b-8614-f54995a259d3"
                  "card-example-f6867425-d71b-478e-ac83-6da49646482b"
                  "card-example-9b809144-48ea-4df6-975e-a4d67df0828f"]
                 (->> (.querySelectorAll card ".card-example")
                      (map #(.-id %)))))

          (is (match? [{:path "document/definition/org.clojure/clojure/clojure.core.server/prepl/0"
                        :method :get}
                       {:path "social/definition/org.clojure/clojure/clojure.core.server/prepl/0"
                        :method :get}
                       {:path "social/example/"
                        :headers {"authorization" "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"}
                        :method :post
                        :body {:definition-id "org.clojure/clojure/clojure.core.server/prepl/0"
                               :body "my example 1"}}]
                      (get-mock-http-requests)))

          (done))
        (fn [err] (is (= nil err))
          (done))))))

(deftest social-examples-edit-view-test
  (testing "definition-social should render and edit example"
    ; mock http request
    (mock-http-with (merge base-http-mocks
                           {"social/example/"
                            {:lag 0
                             :status 200
                             :body {:body "edited example",
                                    :created-at #inst "2024-03-15T13:17:45.414-00:00"
                                    :definition-id "org.clojure/clojure/clojure.core.server/prepl/0"
                                    :example-id #uuid "8b81ce0d-20fd-43e7-a79a-a9edbb0f162a"}}}))

    ; call initial db fetch
    (definition.state/definition-docs-fetch "org.clojure" "clojure" "clojure.core.server" "prepl" 0)
    (definition.state/definition-social-fetch "org.clojure" "clojure" "clojure.core.server" "prepl" 0)
    (auth.state/user {:error nil
                      :loading? false
                      :value fixtures.user/user-2})

    (async done
      (p/catch
        (p/let [view (tl/mantine-render ($ definition-detail))
                cards (tl/wait-for #(.findByTestId ^js/Object view "card-examples"))
                edit-button (tl/wait-for #(.findByTestId ^js/Object view "example-author-edit-button-f6867425-d71b-478e-ac83-6da49646482b"))
                _click (tl/wait-for #(tl/click edit-button))
                input (tl/wait-for #(.findByTestId ^js/Object (tlr/within js/document) "markdown-editor-textarea"))
                _change (tl/wait-for #(tl/change input "edited example"))
                save-button (tl/wait-for #(.findByTestId ^js/Object (tlr/within js/document) "editor-base-save-btn"))
                _save-click (tl/wait-for #(tl/click save-button))
                card-after (tl/wait-for #(.findByTestId ^js/Object view "card-examples"))]

          (is (= ["example-author-edit-button-f6867425-d71b-478e-ac83-6da49646482b"
                  "example-author-edit-button-9b809144-48ea-4df6-975e-a4d67df0828f"]
                 (->> (.querySelectorAll cards ".example-author-edit-button")
                      (map #(.-id %)))))

          (is (match? [#"(assoc \{\} :key1 \"value\" :key2 \"another value\")"
                       #"(please :stop)"]
                      (->> (.querySelectorAll card-after ".code-viewer")
                           (map #(.-textContent %)))))

          (is (match? [{:path "document/definition/org.clojure/clojure/clojure.core.server/prepl/0"
                        :method :get}
                       {:path "social/definition/org.clojure/clojure/clojure.core.server/prepl/0"
                        :method :get}
                       {:path "social/example/"
                        :headers {"authorization" "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"}
                        :method :put
                        :body {:example-id #uuid "f6867425-d71b-478e-ac83-6da49646482b"
                               :body "edited example"}}]
                      (get-mock-http-requests)))

          (done))
        (fn [err] (is (= nil err))
          (done))))))

(deftest social-examples-delete-view-test
  (testing "definition-social should render and delete example"
    ; mock http request
    (mock-http-with (merge base-http-mocks
                           {"social/example/f6867425-d71b-478e-ac83-6da49646482b"
                            {:lag 0
                             :status 200
                             :body {:example-id #uuid "f6867425-d71b-478e-ac83-6da49646482b"}}}))

    ; call initial db fetch
    (definition.state/definition-docs-fetch "org.clojure" "clojure" "clojure.core.server" "prepl" 0)
    (definition.state/definition-social-fetch "org.clojure" "clojure" "clojure.core.server" "prepl" 0)
    (auth.state/user {:error nil
                      :loading? false
                      :value fixtures.user/user-2})

    (async done
      (p/catch
        (p/let [view (tl/mantine-render ($ definition-detail))
                cards (tl/wait-for #(.findByTestId ^js/Object view "card-examples"))
                delete-button (tl/wait-for #(.findByTestId ^js/Object view "example-author-delete-button-f6867425-d71b-478e-ac83-6da49646482b"))
                _click (tl/wait-for #(tl/click delete-button))
                yes-button (tl/wait-for #(.findByTestId ^js/Object (tlr/within js/document) "definition-delete-alert-yes-btn"))
                _yes-click (tl/wait-for #(tl/click yes-button))
                card-after (tl/wait-for #(.findByTestId ^js/Object view "card-examples"))]

          (is (= ["example-author-delete-button-f6867425-d71b-478e-ac83-6da49646482b"]
                 (->> (.querySelectorAll cards ".example-author-delete-button")
                      (map #(.-id %)))))

          (is (match? [#"(please :stop)"]
                      (->> (.querySelectorAll card-after ".code-viewer")
                           (map #(.-textContent %)))))

          (is (match? [{:path "document/definition/org.clojure/clojure/clojure.core.server/prepl/0"
                        :method :get}
                       {:path "social/definition/org.clojure/clojure/clojure.core.server/prepl/0"
                        :method :get}
                       {:path "social/example/f6867425-d71b-478e-ac83-6da49646482b"
                        :headers {"authorization" "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"}
                        :method :delete}]
                      (get-mock-http-requests)))

          (done))
        (fn [err] (is (= nil err))
          (done))))))
