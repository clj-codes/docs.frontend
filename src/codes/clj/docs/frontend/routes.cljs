(ns codes.clj.docs.frontend.routes
  (:require [codes.clj.docs.frontend.panels.home.view :as home.view]
            [codes.clj.docs.frontend.panels.namespaces.view :as namespaces.view]
            [codes.clj.docs.frontend.panels.namespaces.state :as namespaces.state]
            [codes.clj.docs.frontend.panels.projects.state :as projects.state]
            [codes.clj.docs.frontend.panels.projects.view :as projects.view]))

(def routes
  ["/"
   [""
    {:name        :home
     :view        home.view/home
     :link-text   "Home"}]

   ["projects"
    {:name        :projects
     :view        projects.view/group-by-orgs
     :link-text   "Projects"
     :controllers [{:start (fn [& _params]
                             (projects.state/document-projects-fetch))}]}]

   [":organization/:project"
    {:name        :namespaces
     :view        namespaces.view/org-projects
     :link-text   "Namspaces"
     :parameters  {:path {:organization string?
                          :project string?}}
     :controllers [{:parameters {:path [:organization
                                        :project]}
                    :start (fn [& params]
                             (let [{:keys [organization project]} (-> params first :path)]
                               (namespaces.state/namespaces-fetch organization project)))}]}]])
