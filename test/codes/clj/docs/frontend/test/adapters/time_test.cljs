(ns codes.clj.docs.frontend.test.adapters.time-test
  (:require [cljs.test :refer [deftest is use-fixtures]]
            [codes.clj.docs.frontend.adapters.time :as adapters]
            [codes.clj.docs.frontend.test.aux.init :refer [sync-setup]]
            [matcher-combinators.test :refer [match?]]))

(use-fixtures :each sync-setup)

(deftest time-since-test
  (let [created-at #inst "2024-03-09T00:00:00.000000000-00:00"]
    (is (match? "0 second ago"
                (adapters/time-since created-at #inst "2024-03-09T00:00:00.000000000-00:00")))

    (is (match? "1 second ago"
                (adapters/time-since created-at #inst "2024-03-09T00:00:01.000000000-00:00")))
    (is (match? "2 seconds ago"
                (adapters/time-since created-at #inst "2024-03-09T00:00:02.000000000-00:00")))

    (is (match? "1 minute ago"
                (adapters/time-since created-at #inst "2024-03-09T00:01:00.000000000-00:00")))
    (is (match? "2 minutes ago"
                (adapters/time-since created-at #inst "2024-03-09T00:02:00.000000000-00:00")))

    (is (match? "1 hour ago"
                (adapters/time-since created-at #inst "2024-03-09T01:00:00.000000000-00:00")))
    (is (match? "2 hours ago"
                (adapters/time-since created-at #inst "2024-03-09T02:00:00.000000000-00:00")))

    (is (match? "1 day ago"
                (adapters/time-since created-at #inst "2024-03-10T00:00:00.000000000-00:00")))
    (is (match? "2 days ago"
                (adapters/time-since created-at #inst "2024-03-11T00:00:00.000000000-00:00")))

    (is (match? "1 week ago"
                (adapters/time-since created-at #inst "2024-03-16T00:00:00.000000000-00:00")))
    (is (match? "2 weeks ago"
                (adapters/time-since created-at #inst "2024-03-26T00:00:00.000000000-00:00")))

    (is (match? "1 month ago"
                (adapters/time-since created-at #inst "2024-04-09T00:00:00.000000000-00:00")))
    (is (match? "2 months ago"
                (adapters/time-since created-at #inst "2024-05-09T00:00:00.000000000-00:00")))

    (is (match? "1 year ago"
                (adapters/time-since created-at #inst "2025-03-09T00:00:00.000000000-00:00")))
    (is (match? "2 years ago"
                (adapters/time-since created-at #inst "2026-03-09T00:00:00.000000000-00:00")))))


