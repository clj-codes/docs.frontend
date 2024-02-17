(ns codes.clj.docs.frontend.panels.definitions.adapters
  (:require [clojure.string :as str]))

(defn definitions->alphabetic-grouped-list [definitions]
  (->> definitions
       (sort-by :name)
       (group-by (fn [{:keys [name]}]
                   (if-let [alpha (->> name
                                       str/lower-case
                                       first
                                       (re-matches #"[a-z]"))]
                     alpha
                     "*")))
       (sort-by first)))
