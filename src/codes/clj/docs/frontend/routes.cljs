(ns codes.clj.docs.frontend.routes
  (:require [codes.clj.docs.frontend.panels.definition.state :as definition.state]
            [codes.clj.docs.frontend.panels.definition.view :as definition.view]
            [codes.clj.docs.frontend.panels.definitions.state :as definitions.state]
            [codes.clj.docs.frontend.panels.definitions.view :as definitions.view]
            [codes.clj.docs.frontend.panels.home.view :as home.view]
            [codes.clj.docs.frontend.panels.namespaces.state :as namespaces.state]
            [codes.clj.docs.frontend.panels.namespaces.view :as namespaces.view]
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
     :controllers [{:parameters {:path [:organization :project]}
                    :start (fn [& params]
                             (let [{:keys [organization project]} (-> params first :path)]
                               (namespaces.state/namespaces-fetch organization project)))}]}]

   [":organization/:project/:namespace"
    {:name        :definitions
     :view        definitions.view/namespace-definitions
     :link-text   "Definitions"
     :parameters  {:path {:organization string?
                          :project string?
                          :namespace string?}}
     :controllers [{:parameters {:path [:organization :project :namespace]}
                    :start (fn [& params]
                             (let [{:keys [organization project namespace]} (-> params first :path)]
                               (definitions.state/definitions-fetch organization project namespace)))}]}]

   [":organization/:project/:namespace/:definition"
    {:name        :definition
     :view        definition.view/definition-detail
     :link-text   "Definition"
     :parameters  {:path {:organization string?
                          :project string?
                          :namespace string?
                          :definition string?}}
     :controllers [{:parameters {:path [:organization :project :namespace :definition]}
                    :start (fn [& params]
                             (let [{:keys [organization project namespace definition]} (-> params first :path)]
                               (definition.state/definition-fetch organization project namespace definition 0)))}]}]

   [":organization/:project/:namespace/:definition/:index"
    {:name        :definition-indexed
     :view        definition.view/definition-detail
     :link-text   "Definition"
     :parameters  {:path {:organization string?
                          :project string?
                          :namespace string?
                          :definition string?
                          :index integer?}}
     :controllers [{:parameters {:path [:organization :project :namespace :definition :index]}
                    :start (fn [& params]
                             (let [{:keys [organization project namespace definition index]} (-> params first :path)]
                               (definition.state/definition-fetch organization project namespace definition index)))}]}]])
