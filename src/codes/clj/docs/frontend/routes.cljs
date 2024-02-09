(ns codes.clj.docs.frontend.routes
  (:require [codes.clj.docs.frontend.panels.home.view :as home.view]
            [codes.clj.docs.frontend.panels.libraries.view :as libraries.view]))

(def routes
  ["/"
   [""
    {:name      :home
     :view      home.view/home
     :link-text "Home"}]

   ["libraries"
    {:name      :libraries
     :view      libraries.view/libraries
     :link-text "Libraries"}]])
