(ns codes.clj.docs.frontend.test.node
  (:require [cljs.test :as test]
            [codes.clj.docs.frontend.test.aux.init]
            [codes.clj.docs.frontend.test.components.documents-test]
            [codes.clj.docs.frontend.test.components.navigation-test]
            [codes.clj.docs.frontend.test.core-test]
            [codes.clj.docs.frontend.test.infra.http-test]
            [codes.clj.docs.frontend.test.panels.definition.view-test]
            [codes.clj.docs.frontend.test.panels.definitions.adapters-test]
            [codes.clj.docs.frontend.test.panels.definitions.view-test]
            [codes.clj.docs.frontend.test.panels.namespaces.view-test]
            [codes.clj.docs.frontend.test.panels.projects.adapters-test]
            [codes.clj.docs.frontend.test.panels.projects.view-test]
            [codes.clj.docs.frontend.test.panels.shell.components-test]))

(defmethod test/report [::test/default :end-run-tests] [m]
  (if (test/successful? m)
    (js/process.exit 0)
    (js/process.exit 1)))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn main []
  (test/run-all-tests #".*-test$"))
