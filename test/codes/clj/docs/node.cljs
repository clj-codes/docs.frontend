(ns codes.clj.docs.node
  (:require ["global-jsdom" :as global-jsdom]
            [cljs.test :as test]
            [codes.clj.docs.frontend.core-test]
            [codes.clj.docs.frontend.infra.http-test]
            [codes.clj.docs.frontend.panels.shell.components-test]
            [codes.clj.docs.frontend.panels.projects.adapters-test]
            [codes.clj.docs.frontend.panels.projects.view-test]))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn main []
  (global-jsdom)
  (test/run-all-tests #".*-test$"))
