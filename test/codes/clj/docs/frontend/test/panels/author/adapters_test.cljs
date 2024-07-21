(ns codes.clj.docs.frontend.test.panels.author.adapters-test
  (:require [cljs.test :refer [deftest is use-fixtures]]
            [codes.clj.docs.frontend.panels.author.adapters :as author.adapters]
            [codes.clj.docs.frontend.test.aux.fixtures.author :as fixtures.author]
            [codes.clj.docs.frontend.test.aux.init :refer [sync-setup]]))

(use-fixtures :each sync-setup)

(deftest author->string-summary-test
  (is (match? "This user hasn't authored any content."
              (author.adapters/->string-summary (dissoc fixtures.author/author :socials))))
  (is (match? "This has user has authored 1 see-alsos."
              (author.adapters/->string-summary (update
                                                 fixtures.author/author
                                                 :socials
                                                 (partial take-last 1)))))
  (is (match? "This has user has authored 2 examples and 3 see-alsos."
              (author.adapters/->string-summary (update
                                                 fixtures.author/author
                                                 :socials
                                                 rest))))
  (is (match? "This has user has authored 3 examples, 1 notes and 6 see-alsos."
              (author.adapters/->string-summary fixtures.author/author))))
