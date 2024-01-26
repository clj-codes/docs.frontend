(ns codes.clj.docs.frontend.dev.core
  (:require [codes.clj.docs.frontend.core :as app]
            [promesa.core :as p]))

(def debug? ^boolean goog.DEBUG)

(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode")))

(defn ^:export init []
  (p/do (dev-setup)
        (app/init)))
