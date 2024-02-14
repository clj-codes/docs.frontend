(ns codes.clj.docs.frontend.panels.projects.adapters
  (:require [clojure.string :as str]))

(defn- grouped-projects->urls
  [grouped-projects]
  (->> grouped-projects
       (map (fn [{:keys [url artifact]}]
              (str/replace-first url (re-pattern (str "/" artifact "$")) "")))
       distinct
       (into #{})))

(defn- urls->org-image "Only works with github for now."
  [urls]
  (when-let [github (first (filter #(re-matches #".*github\.com.*" %) urls))]
    (str github ".png?size=200")))

(defn projects->groups
  [projects]
  (->> projects
       (group-by :group)
       (mapv (fn [[group projects]]
               (let [urls (grouped-projects->urls projects)
                     image (urls->org-image urls)]
                 {:id group
                  :image image
                  :count-projects (count projects)
                  :urls urls
                  :projects projects})))))

