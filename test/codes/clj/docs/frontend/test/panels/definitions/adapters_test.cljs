(ns codes.clj.docs.frontend.test.panels.definitions.adapters-test
  (:require [cljs.test :refer [deftest is use-fixtures]]
            [codes.clj.docs.frontend.panels.definitions.adapters :as adapters]
            [codes.clj.docs.frontend.test.aux.fixtures.definitions :as fixtures]
            [codes.clj.docs.frontend.test.aux.init :refer [sync-setup]]
            [matcher-combinators.test :refer [match?]]))

(use-fixtures :each sync-setup)

(def expected-output
  [["*" [{:name "!stop-servers"}
         {:name "*session*"}]]
   ["a" [{:name "accept-connection"}]]
   ["e" [{:name "ex->data"}]]
   ["i" [{:name "io-prepl"}]]
   ["l" [{:name "lock"}]]
   ["p" [{:name "parse-props"} {:name "prepl"}]]
   ["r" [{:name "remote-prepl"}
         {:name "repl"}
         {:name "repl-init"}
         {:name "repl-read"}
         {:name "required"}
         {:name "resolve-fn"}]]
   ["s" [{:name "servers"}
         {:name "start-server"}
         {:name "start-servers"}
         {:name "stop-server"}
         {:name "stop-servers"}]]
   ["t" [{:name "thread"}]]
   ["v" [{:name "validate-opts"}]]
   ["w" [{:name "with-lock"}]]])

(deftest projects->groups-test
  (is (match? expected-output
              (-> fixtures/definitions
                  :definitions
                  adapters/definitions->alphabetic-grouped-list))))
