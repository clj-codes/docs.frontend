(ns codes.clj.docs.node
  (:require ["global-jsdom" :as global-jsdom]
            [cljs.test :as test]
            [codes.clj.docs.frontend.core-test]
            [codes.clj.docs.frontend.infra.http-test]
            [codes.clj.docs.frontend.panels.projects.adapters-test]
            [codes.clj.docs.frontend.panels.projects.view-test]
            [codes.clj.docs.frontend.panels.shell.components-test]))

(defmethod test/report [::test/default :end-run-tests] [m]
  (if (test/successful? m)
    (js/process.exit 0)
    (js/process.exit 1)))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn main []
  (global-jsdom)
  (test/run-all-tests #".*-test$"))
