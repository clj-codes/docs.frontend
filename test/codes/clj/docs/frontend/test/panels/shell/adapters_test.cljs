(ns codes.clj.docs.frontend.test.panels.shell.adapters-test
  (:require [cljs.test :refer [deftest is use-fixtures]]
            [codes.clj.docs.frontend.panels.shell.adapters :as shell.adapters]
            [codes.clj.docs.frontend.test.aux.init :refer [sync-setup]]
            [matcher-combinators.test :refer [match?]]))

(use-fixtures :each sync-setup)

(deftest href->safe-href-test
  (let [github-fake-config {:login-url "https://github.io"
                            :client-id "abc123456def"
                            :redirect-uri "https://my.site"}]
    (is (match? "https://github.io?client_id=abc123456def&redirect_uri=https://my.site?page=myurl/"
                (shell.adapters/path->github-link "myurl/" github-fake-config)))))
