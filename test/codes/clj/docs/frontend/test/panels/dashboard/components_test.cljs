(ns codes.clj.docs.frontend.test.panels.dashboard.components-test
  (:require [cljs.test :refer [async deftest is testing use-fixtures]]
            [clojure.string :as str]
            [codes.clj.docs.frontend.panels.dashboards.components :as components]
            [codes.clj.docs.frontend.test.aux.fixtures.dashboards :as fixtures]
            [codes.clj.docs.frontend.test.aux.init :refer [async-cleanup
                                                           async-setup]]
            [codes.clj.docs.frontend.test.aux.testing-library :as tl]
            [helix.core :refer [$]]
            [matcher-combinators.matchers :as m]
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
  {:value fixtures/top-authors :loading? false
   :error nil})

(deftest latest-interactions-component-test
  (testing "latest-interactions should render latest interaction texts depending on interaction type"
    (async done
      (p/catch
        (p/let [latest-interactions-list (tl/wait-for
                                          #(-> (tl/mantine-render ($ components/latest-interactions-list {:& latest-interactions-response}))
                                               (.findByTestId "latest-interactions-list")))]

          (is (match? [(m/pred #(str/starts-with? % "strobelt added a see also on org.clojure/clojure/clojure.core/contains?"))
                       (m/pred #(str/starts-with? % "strobelt authored an example for org.clojure/clojure/clojure.core/contains?"))
                       (m/pred #(str/starts-with? % "kroncatti authored an example for org.clojure/clojure/clojure.core/defmulti"))
                       (m/pred #(str/starts-with? % "rafaeldelboni added a see also on org.clojure/clojure/clojure.core/take"))
                       (m/pred #(str/starts-with? % "rafaeldelboni authored an example for org.clojure/clojure/clojure.core/take"))
                       (m/pred #(str/starts-with? % "rafaeldelboni added a see also on org.clojure/clojure/clojure.core/remove"))
                       (m/pred #(str/starts-with? % "rafaeldelboni added a see also on org.clojure/clojure/clojure.core/keep"))
                       (m/pred #(str/starts-with? % "rafaeldelboni authored an example for org.clojure/clojure/clojure.core/keep"))
                       (m/pred #(str/starts-with? % "dimmyjr-nu authored an example for nubank/matcher-combinators/matcher-combinators.matchers/embeds"))
                       (m/pred #(str/starts-with? % "matheusfrancisco authored a note for org.clojure/clojure/clojure.core/assoc"))]
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

          (is (match? [["rafaeldelboni" "github"]
                       ["vloth" "github"]
                       ["matheusfrancisco" "github"]
                       ["strobelt" "github"]
                       ["kroncatti" "github"]
                       ["dimmyjr-nu" "github"]
                       ["daveliepmann" "github"]]
                      (->> (.querySelectorAll top-authors-list ".author-interaction-anchor")
                           (map (fn [top-author]
                                  (-> top-author
                                      .-href
                                      (str/split "/")
                                      (->> (drop 4))))))))

          (done))
        (fn [err] (is (= nil err))
          (done))))))
