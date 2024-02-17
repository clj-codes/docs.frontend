(ns codes.clj.docs.frontend.test.panels.definitions.view-test
  (:require [cljs.test :refer [async deftest is testing use-fixtures]]
            [codes.clj.docs.frontend.panels.definitions.state :as projects.state]
            [codes.clj.docs.frontend.panels.definitions.view :refer [namespace-definitions]]
            [codes.clj.docs.frontend.test.aux.fixtures.definitions :as fixtures]
            [codes.clj.docs.frontend.test.aux.init :refer [async-cleanup
                                                           async-setup
                                                           mock-http-with]]
            [codes.clj.docs.frontend.test.aux.testing-library :as tl]
            [helix.core :refer [$]]
            [promesa.core :as p]))

(use-fixtures :each
  {:before async-setup
   :after async-cleanup})

(deftest namespace-definitions-view-test
  ; mock http request
  (mock-http-with {"document/definitions/org.clojure/clojure/clojure.core.server"
                   {:lag 500
                    :status 200
                    :body fixtures/definitions}})

  ; call initial db fetch
  (projects.state/definitions-fetch "org.clojure" "clojure" "clojure.core.server")

  (testing "org-projects should render project namespace cards"
    (async done
      (p/catch
        (p/let [view (tl/mantine-render ($ namespace-definitions))
                items (tl/wait-for #(.findByTestId ^js/Object view "definition-lines-grid"))]

          (is (= ["*" "!stop-servers" "*session*"
                  "a" "accept-connection"
                  "e" "ex->data"
                  "i" "io-prepl"
                  "l" "lock"
                  "p" "parse-props" "prepl"
                  "r" "remote-prepl" "repl" "repl-init" "repl-read" "required" "resolve-fn"
                  "s" "servers" "start-server" "start-servers" "stop-server" "stop-servers"
                  "t" "thread"
                  "v" "validate-opts"
                  "w" "with-lock"]
                 (->> (.querySelectorAll items ".mantine-Anchor-root")
                      (mapv #(-> % .-textContent)))))

          (done))
        (fn [err] (is (= nil err))
          (done))))))
