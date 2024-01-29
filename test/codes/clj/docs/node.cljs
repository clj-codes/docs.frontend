(ns codes.clj.docs.node
  (:require ["global-jsdom" :as global-jsdom]
            [clojure.test :as test]))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn main []
  (global-jsdom)
  (test/run-all-tests #".*-test$"))
