(ns codes.clj.docs.frontend.test.panels.dashboard.components-test
  (:require [cljs.test :refer [async deftest is testing use-fixtures]]
            [codes.clj.docs.frontend.panels.dashboards.components :as components]
            [codes.clj.docs.frontend.test.aux.init :refer [async-cleanup
                                                           async-setup]]
            [codes.clj.docs.frontend.test.aux.testing-library :as tl]
            [codes.clj.docs.frontend.test.aux.fixtures.dashboards :as fixtures]
            [helix.core :refer [$]]
            [matcher-combinators.test :refer [match?]]
            [promesa.core :as p]))

(use-fixtures :each
  {:before async-setup
   :after async-cleanup})

(def latest-interactions-response
  {:value fixtures/latest-interactions
   :loading? false
   :error nil})

(def top-authors-response
  {:value fixtures/top-authors
   :loading? false
   :error nil})

(deftest latest-interactions-component-test
  (testing "latest-interactions should render latest interaction texts depending on interaction type"
    (async done
      (p/catch
        (p/let [latest-interactions-list (tl/wait-for
                                          #(-> (tl/mantine-render ($ components/latest-interactions-list {:& latest-interactions-response}))
                                               (.findByTestId "latest-interactions-list")))]

          (is (match? ["strobelt added a see also on org.clojure/clojure/clojure.core/contains? 3 months ago."
                       "strobelt authored an example for org.clojure/clojure/clojure.core/contains? 3 months ago."
                       "kroncatti authored an example for org.clojure/clojure/clojure.core/defmulti 4 months ago."
                       "rafaeldelboni added a see also on org.clojure/clojure/clojure.core/take 4 months ago."
                       "rafaeldelboni authored an example for org.clojure/clojure/clojure.core/take 4 months ago."
                       "rafaeldelboni added a see also on org.clojure/clojure/clojure.core/remove 4 months ago."
                       "rafaeldelboni added a see also on org.clojure/clojure/clojure.core/keep 4 months ago."
                       "rafaeldelboni authored an example for org.clojure/clojure/clojure.core/keep 4 months ago."
                       "dimmyjr-nu authored an example for nubank/matcher-combinators/matcher-combinators.matchers/embeds 4 months ago."
                       "matheusfrancisco authored a note for org.clojure/clojure/clojure.core/assoc 4 months ago."]
                      (->> (.querySelectorAll latest-interactions-list ".interaction-text")
                           (map (fn [interaction]
                                  (.-textContent interaction))))))

          (done))
        (fn [err] (is (= nil err))
          (done))))))

(deftest top-authors-component-test
  (testing "top-authors should render top authors links"
    (async done
      (p/catch
        (p/let [top-authors-list (tl/wait-for
                                  #(-> (tl/mantine-render ($ components/top-authors-list {:& top-authors-response}))
                                       (.findByTestId "top-authors-list")))]

          (is (match? ["http://localhost:5002/author/rafaeldelboni/github"
                       "http://localhost:5002/author/vloth/github"
                       "http://localhost:5002/author/matheusfrancisco/github"
                       "http://localhost:5002/author/strobelt/github"
                       "http://localhost:5002/author/kroncatti/github"
                       "http://localhost:5002/author/dimmyjr-nu/github"
                       "http://localhost:5002/author/daveliepmann/github"]
                      (->> (.querySelectorAll top-authors-list ".author-interaction-anchor")
                           (map (fn [top-author]
                                  (js/console.log top-author)
                                  (.-href top-author))))))

          (done))
        (fn [err] (is (= nil err))
          (done))))))
