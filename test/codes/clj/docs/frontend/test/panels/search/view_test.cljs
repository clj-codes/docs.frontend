(ns codes.clj.docs.frontend.test.panels.search.view-test
  (:require ["@testing-library/react" :as tlr]
            [cljs.test :refer [async deftest is testing use-fixtures]]
            [codes.clj.docs.frontend.infra.routes.core :refer [init-routes!]]
            [codes.clj.docs.frontend.panels.search.state :as search.state]
            [codes.clj.docs.frontend.panels.search.view :refer [search-page
                                                                search-spotlight]]
            [codes.clj.docs.frontend.test.aux.fixtures.search-results :as fixtures]
            [codes.clj.docs.frontend.test.aux.init :refer [async-cleanup
                                                           async-setup
                                                           mock-http-with]]
            [codes.clj.docs.frontend.test.aux.testing-library :as tl]
            [helix.core :refer [$]]
            [promesa.core :as p]))

(use-fixtures :each
  {:before async-setup
   :after async-cleanup})

(deftest search-spotlight-test
  (testing "search-spotlight should render search spotlight"
    ; mock http request
    (mock-http-with {"document/search/"
                     {:lag 0
                      :status 200
                      :body fixtures/search-results}})

    (async done
      (p/catch
        (p/let [view (tl/mantine-render ($ search-spotlight))
                button (tl/wait-for #(.findByTestId ^js/Object view "spotlight-search-button"))
                _click (tl/wait-for #(tl/click button))
                input (tl/wait-for #(.findByTestId ^js/Object (tlr/within js/document) "spotlight-modal-search-input"))
                _change (tl/wait-for #(tl/change input "serve"))
                actions (tl/wait-for #(.findByTestId ^js/Object (tlr/within js/document) "spotlight-actions-group:definition"))]

          (is (= ["/org.clojure/clojure/clojure.core.server/servers/0"
                  "/org.clojure/clojure/clojure.core.server/stop-server/0"
                  "/org.clojure/clojure/clojure.core.server/start-server/0"
                  "/org.clojure/clojure/clojure.core.server/stop-servers/0"
                  "/org.clojure/clojure/clojure.core.server/start-servers/0"]
                 (->> (.querySelectorAll actions ".mantine-Spotlight-action")
                      (map #(.-pathname %)))))

          (done))
        (fn [err] (is (= nil err))
          (done))))))

(deftest search-page-test
  (testing "search-page should render search spotlight"
    ; start routes 
    (init-routes!)
    ; mock http request
    (mock-http-with {"document/search/"
                     {:lag 0
                      :status 200
                      :body fixtures/search-results}})

    ; call initial db fetch
    (search.state/search-fetch search.state/page-results "server" 30)

    (async done
      (p/catch
        (p/let [view (tl/mantine-render ($ search-page))
                cards (tl/wait-for #(.findByTestId ^js/Object view "page-result-cards"))]

          (is (= ["card-search-result-org.clojure/clojure/clojure.core.server/servers/0"
                  "card-search-result-org.clojure/clojure/clojure.core.server/stop-server/0"
                  "card-search-result-org.clojure/clojure/clojure.core.server/start-server/0"
                  "card-search-result-org.clojure/clojure/clojure.core.server/stop-servers/0"
                  "card-search-result-org.clojure/clojure/clojure.core.server/start-servers/0"
                  "card-search-result-org.clojure/clojure/clojure.core.server"]
                 (->> (.querySelectorAll cards ".mantine-Card-root")
                      (map #(.-id %)))))

          (done))
        (fn [err] (is (= nil err))
          (done))))))
