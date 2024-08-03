(ns codes.clj.docs.frontend.routes
  (:require [codes.clj.docs.frontend.infra.auth.github.view :as auth.github.view]
            [codes.clj.docs.frontend.panels.author.state :as author.state]
            [codes.clj.docs.frontend.panels.author.view :as author.view]
            [codes.clj.docs.frontend.panels.dashboards.view :as dashboards.view]
            [codes.clj.docs.frontend.panels.definition.state :as definition.state]
            [codes.clj.docs.frontend.panels.definition.view :as definition.view]
            [codes.clj.docs.frontend.panels.definitions.state :as definitions.state]
            [codes.clj.docs.frontend.panels.definitions.view :as definitions.view]
            [codes.clj.docs.frontend.panels.home.view :as home.view]
            [codes.clj.docs.frontend.panels.license.view :as license.view]
            [codes.clj.docs.frontend.panels.namespaces.state :as namespaces.state]
            [codes.clj.docs.frontend.panels.namespaces.view :as namespaces.view]
            [codes.clj.docs.frontend.panels.projects.state :as projects.state]
            [codes.clj.docs.frontend.panels.projects.view :as projects.view]
            [codes.clj.docs.frontend.panels.search.state :as search.state :refer [page-results]]
            [codes.clj.docs.frontend.panels.search.view :as search.view]))

(defn- set-title! [title]
  (set! (.-title js/document) title))

(def routes
  ["/"
   [""
    {:name        :home
     :view        home.view/home
     :link-text   "Home"
     :controllers [{:start (fn [& _params]
                             (set-title! "docs.clj.codes"))}]}]

   ["license"
    {:name        :license
     :view        license.view/license
     :link-text   "license"
     :controllers [{:start (fn [& _params]
                             (set-title! "License - docs.clj.codes"))}]}]

   ["github-callback"
    {:name        :github-callback
     :parameters  {:query [:map
                           [:code {:optional true} :string]
                           [:page {:optional true} :string]
                           [:error {:optional true} :string]
                           [:error_description {:optional true} :string]
                           [:error_uri {:optional true} :string]]}
     :view        auth.github.view/page
     :link-text   "github-callback"
     :controllers [{:parameters {:query [:code :page :error :error_description :error_uri]}
                    :start (fn [& _params]
                             (set-title! "docs.clj.codes"))}]}]

   ["projects"
    {:name        :projects
     :view        projects.view/group-by-orgs
     :link-text   "Projects"
     :controllers [{:start (fn [& _params]
                             (set-title! "Projects - docs.clj.codes")
                             (projects.state/document-projects-fetch))}]}]

   ["dashboards"
    {:name        :dashboards
     :view        dashboards.view/all
     :link-text   "dashboards"
     :controllers [{:start (fn [& _params]
                             (set-title! "Dashboards - docs.clj.codes")
                             ; fetch todo
                             )}]}]

   ["search"
    {:name        :search
     :view        search.view/search-page
     :link-text   "Search"
     :parameters  {:query [:map
                           [:q {:optional true} :string]]}
     :controllers [{:parameters {:query [:q]}
                    :start (fn [& params]
                             (let [{:keys [q]} (-> params first :query)]
                               (set-title! "Search - docs.clj.codes")
                               (search.state/search-fetch page-results (or q "") 100)))}]}]

   ["author/:login/:source"
    {:name        :author
     :view        author.view/author-detail-page
     :link-text   "Author Details"
     :conflicting true
     :parameters  {:path {:login string?
                          :source string?}}
     :controllers [{:parameters {:path [:login :source]}
                    :start (fn [& params]
                             (let [{:keys [login source]} (-> params first :path)]
                               (set-title! "Author Details - docs.clj.codes")
                               (author.state/author-fetch login source)))}]}]

   [":organization/:project"
    {:name        :namespaces
     :view        namespaces.view/org-projects
     :link-text   "Namspaces"
     :parameters  {:path {:organization string?
                          :project string?}}
     :controllers [{:parameters {:path [:organization :project]}
                    :start (fn [& params]
                             (let [{:keys [organization project]} (-> params first :path)]
                               (set-title! (str organization " - " project " - docs.clj.codes"))
                               (namespaces.state/namespaces-fetch organization project)))}]}]

   [":organization/:project/:namespace"
    {:name        :definitions
     :view        definitions.view/namespace-definitions
     :link-text   "Definitions"
     :conflicting true
     :parameters  {:path {:organization string?
                          :project string?
                          :namespace string?}}
     :controllers [{:parameters {:path [:organization :project :namespace]}
                    :start (fn [& params]
                             (let [{:keys [organization project namespace]} (-> params first :path)]
                               (set-title! (str namespace " - " organization " - " project " - docs.clj.codes"))
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
                               (set-title! (str definition " - " namespace " - " organization " - " project " - docs.clj.codes"))
                               (definition.state/definition-docs-fetch organization project namespace definition 0)
                               (definition.state/definition-social-fetch organization project namespace definition 0)))}]}]

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
                               (set-title! (str definition " - " namespace " - " organization " - " project " - docs.clj.codes"))
                               (definition.state/definition-docs-fetch organization project namespace definition index)
                               (definition.state/definition-social-fetch organization project namespace definition index)))}]}]])
