(ns codes.clj.docs.frontend.config
  (:require ["@mantine/core" :refer [createTheme]]))

(def system
  (let [debug? goog.DEBUG]
    {:debug? debug?
     :base-url (if debug?
                 "http://localhost:3001/api"
                 "http://docs.clj.codes/api")}))

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


