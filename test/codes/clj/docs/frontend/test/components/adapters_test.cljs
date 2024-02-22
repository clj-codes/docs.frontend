(ns codes.clj.docs.frontend.test.components.adapters-test
  (:require [cljs.test :refer [deftest is use-fixtures]]
            [codes.clj.docs.frontend.components.adapters :as adapters]
            [codes.clj.docs.frontend.test.aux.init :refer [sync-setup]]
            [matcher-combinators.test :refer [match?]]))

(use-fixtures :each sync-setup)

(deftest href->safe-href-test
  (is (match? "myurl/" (adapters/href->safe-href "myurl/")))
  (is (match? "myurl/_." (adapters/href->safe-href "myurl/.")))
  (is (match? "myurl/_./" (adapters/href->safe-href "myurl/./")))
  (is (match? "myurl/_.." (adapters/href->safe-href "myurl/..")))
  (is (match? "myurl/_../" (adapters/href->safe-href "myurl/../")))
  (is (match? "myurl/_fs" (adapters/href->safe-href "myurl//")))
  (is (match? "myurl/_fs/" (adapters/href->safe-href "myurl///")))
  (is (match? "myurl/_q" (adapters/href->safe-href "myurl/?")))
  (is (match? "myurl/q_q" (adapters/href->safe-href "myurl/q?")))
  (is (match? "myurl/_bs" (adapters/href->safe-href "myurl/\\")))
  (is (match? "myurl/q_bs" (adapters/href->safe-href "myurl/q\\"))))

(deftest safe-href->href-test
  (is (match? "myurl/" (adapters/safe-href->href "myurl/")))
  (is (match? "myurl/." (adapters/safe-href->href "myurl/_.")))
  (is (match? "myurl/./" (adapters/safe-href->href "myurl/_./")))
  (is (match? "myurl/.." (adapters/safe-href->href "myurl/_..")))
  (is (match? "myurl/../" (adapters/safe-href->href "myurl/_../")))
  (is (match? "myurl//" (adapters/safe-href->href "myurl/_fs")))
  (is (match? "myurl///" (adapters/safe-href->href "myurl/_fs/")))
  (is (match? "myurl/?" (adapters/safe-href->href "myurl/_q")))
  (is (match? "myurl/q?" (adapters/safe-href->href "myurl/q_q")))
  (is (match? "myurl/\\" (adapters/safe-href->href "myurl/_bs")))
  (is (match? "myurl/q\\" (adapters/safe-href->href "myurl/q_bs"))))

(deftest safe-href->url-encoded-test
  (is (match? "myurl/" (adapters/safe-href->url-encoded "myurl/")))
  (is (match? "myurl/%2F" (adapters/safe-href->url-encoded "myurl/_fs")))
  (is (match? "myurl/%2F/" (adapters/safe-href->url-encoded "myurl/_fs/")))
  (is (match? "myurl/%3F" (adapters/safe-href->url-encoded "myurl/_q")))
  (is (match? "myurl/q%3F" (adapters/safe-href->url-encoded "myurl/q_q")))
  (is (match? "myurl/%5C" (adapters/safe-href->url-encoded "myurl/_bs")))
  (is (match? "myurl/q%5C" (adapters/safe-href->url-encoded "myurl/q_bs")))
  ; Only dots have different behavior
  (is (match? "myurl%2F." (adapters/safe-href->url-encoded "myurl/_.")))
  (is (match? "myurl%2F.%2F" (adapters/safe-href->url-encoded "myurl/_./")))
  (is (match? "myurl%2F.." (adapters/safe-href->url-encoded "myurl/_..")))
  (is (match? "myurl%2F..%2F" (adapters/safe-href->url-encoded "myurl/_../"))))
