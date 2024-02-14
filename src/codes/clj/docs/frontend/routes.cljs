(ns codes.clj.docs.frontend.routes
  (:require [codes.clj.docs.frontend.panels.home.view :as home.view]
            [codes.clj.docs.frontend.panels.projects.state :as projects.state]
            [codes.clj.docs.frontend.panels.projects.view :as projects.view]))

(def routes
  ["/"
   [""
    {:name      :home
     :view      home.view/home
     :link-text "Home"}]

   ["projects"
    {:name      :projects
     :view      projects.view/group-by-orgs
     :link-text "Projects"
     :controllers
     [{:start (fn [& _params]
                (projects.state/document-projects-fetch))}]}]])
