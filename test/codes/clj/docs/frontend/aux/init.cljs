(ns codes.clj.docs.frontend.aux.init
  (:require [codes.clj.docs.frontend.aux.testing-library :as tlr]))

(defn setup-root [f]
  (f)
  (tlr/cleanup))
