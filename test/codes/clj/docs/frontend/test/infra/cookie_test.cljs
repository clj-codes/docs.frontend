(ns codes.clj.docs.frontend.test.infra.cookie-test
  (:require [cljs.test :refer [deftest is testing use-fixtures]]
            [codes.clj.docs.frontend.infra.cookie :as infra.cookie]
            [codes.clj.docs.frontend.test.aux.init :refer [sync-setup]]))

(def cookie-name "test.infra.http-test.cookie")

(use-fixtures :each #(do (sync-setup %)
                         (infra.cookie/remove-cookie cookie-name)))

(deftest cookies-test
  (testing "empty cookie should return nil"
    (is (nil? (infra.cookie/get-edn-cookie cookie-name))))
  (testing "cookie should return an edn"
    (infra.cookie/set-cookie cookie-name {:my "cookie"})
    (is (= {:my "cookie"}
           (infra.cookie/get-edn-cookie cookie-name))))
  (testing "malformed cookie should return nil"
    (infra.cookie/set-cookie cookie-name "cookie:")
    (is (= "cookie:"
           (infra.cookie/get-cookie cookie-name)))
    (is (= nil
           (infra.cookie/get-edn-cookie cookie-name)))))
