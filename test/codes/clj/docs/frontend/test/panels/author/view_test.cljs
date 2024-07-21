(ns codes.clj.docs.frontend.test.panels.author.view-test
  (:require [cljs.test :refer [async deftest is testing use-fixtures]]
            [codes.clj.docs.frontend.panels.author.state :as author.state]
            [codes.clj.docs.frontend.panels.author.view :refer [author-detail-page]]
            [codes.clj.docs.frontend.test.aux.fixtures.author :as fixtures]
            [codes.clj.docs.frontend.test.aux.init :refer [async-cleanup
                                                           async-setup
                                                           mock-http-with]]
            [codes.clj.docs.frontend.test.aux.testing-library :as tl]
            [helix.core :refer [$]]
            [promesa.core :as p]))

(use-fixtures :each
  {:before async-setup
   :after async-cleanup})

(deftest author-view-test
  ; mock http request
  (mock-http-with {"social/author/rafaeldelboni/github"
                   {:lag 500
                    :status 200
                    :body fixtures/author}})

  ; call initial db fetch
  (author.state/author-fetch "rafaeldelboni" "github")

  (testing "author-detail-page should render author social"
    (async done
      (p/catch
        (p/let [view (tl/mantine-render ($ author-detail-page))
                items (tl/wait-for #(.findByTestId ^js/Object view "author-grid"))]

          (is (= [";;assoc applied to a vector\n\n(def my-vec [1 2 5 6 8 9])\n\n(assoc my-vec 0 77)\n;;[77 2 5 6 8 9]"
                  "org.clojure/clojure/clojure.core/dissoc/0"
                  "org.clojure/clojure/clojure.core/update/0"
                  "org.clojure/clojure/clojure.core/assoc-in/0"
                  "Here is a version that will create a vector when the key is numerical. This may be useful instead of throwing an IndexOutOfBoundsException.\n```clojure\n(defn assoc-in-idx [m [k & ks] v]\n  (let [value (get m k (when (number? (first ks)) []))\n    m (if (and (vector? m) (number? k) (-> m count (< k)))\n        (reduce (fn [m _] (conj m nil)) m (range (count m) k))\n        m)\n    v (if ks\n        (assoc-in-idx value ks v)\n        v)]\n    (assoc m k v)))\n```\n> copied from  [clojuredocs.org](https://clojuredocs.org/clojure.core/assoc) for test"
                  "; removes nil from list or vector\n(keep identity [:a :b nil :d nil :f])\n; => (:a :b :d :f)"
                  "org.clojure/clojure/clojure.core/remove/0"
                  "; 6 random integers (from 0 to 60)\n(take 6 (repeatedly #(rand-int 60)))"
                  "org.clojure/clojure/clojure.core/repeatedly/0"
                  "org.clojure/clojure/clojure.core/keep/0"]
                 (->> (.querySelectorAll items ".social-preview-item")
                      (map #(-> % .-textContent)))))

          (done))
        (fn [err] (is (= nil err))

          (js/console.log "eee" err)
          (done))))))
