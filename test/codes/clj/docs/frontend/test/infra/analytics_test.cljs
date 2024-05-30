(ns codes.clj.docs.frontend.test.infra.analytics-test
  (:require [cljs.test :refer-macros [deftest is testing]]
            [clojure.string :as string]
            [codes.clj.docs.frontend.infra.analytics :as analytics]))

(deftest ga-scripts-test
  (testing "not-blank GA_TAG_ID should return a script containing the tag id"
    (let [ga-tag-id "G-0123456789"
          ga-scripts (analytics/ga-scripts ga-tag-id)]
      (is (instance? js/HTMLScriptElement ga-scripts))
      (is (string/includes? (.-text ga-scripts) ga-tag-id))))
  (testing "blank GA_TAG_ID should not return anything"
    (is (nil? (analytics/ga-scripts "")))))
