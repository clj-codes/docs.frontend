(ns codes.clj.docs.frontend.test.infra.routes.adapters-test
  (:require [cljs.test :refer [deftest is testing use-fixtures]]
            [codes.clj.docs.frontend.infra.routes.adapters :as routes.adapters]
            [codes.clj.docs.frontend.infra.routes.core :refer [init-routes!]]
            [codes.clj.docs.frontend.infra.routes.state :as routes.state]
            [codes.clj.docs.frontend.test.aux.init :refer [sync-setup]]
            [matcher-combinators.test :refer [match?]]))

(use-fixtures :each sync-setup)

(deftest href->route-test
  (init-routes!)
  (testing "empty cookie should return nil"
    (is (match? {:route :search
                 :path-params {}
                 :query-params {:q "banana"}}
                (routes.adapters/href->route "/search?q=banana"
                                             (:router @routes.state/routes-db))))))
