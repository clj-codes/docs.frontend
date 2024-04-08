(ns codes.clj.docs.frontend.config
  (:require ["@mantine/core" :refer [createTheme]]))

(def theme
  (createTheme
   (clj->js
    {:colors {:moonstone ["#e3fbff"
                          "#d6f0f7"
                          "#b1dee9"
                          "#88ccdb"
                          "#67bccf"
                          "#51b3c9"
                          "#42aec6"
                          "#2f98b0"
                          "#20889e"
                          "#00768b"]}
     :primaryColor "moonstone"
     :defaultRadius "md"})))

(goog-define BASE_URL "https://docs-backend.fly.dev/api/")
(goog-define CLIENT_ID "46d86692f00ed9c613a1")
(goog-define REDIRECT_URI "https://docs.clj.codes/github-callback")

(def config
  (let [debug? goog.DEBUG]
    {:debug? debug?
     :base-url (if debug?
                 "http://localhost:3001/api/"
                 BASE_URL)
     :github {:login-url "https://github.com/login/oauth/authorize"
              :client-id (if debug?
                           "e2e06123b7ca69a6150a"
                           CLIENT_ID)
              :redirect-uri (if debug?
                              "http://localhost:5000/github-callback"
                              REDIRECT_URI)}}))
