(ns codes.clj.docs.frontend.adapters.url
  (:require [clojure.string :as str]))

(defn safe-href->href [safe-href]
  (when safe-href
    (-> safe-href
        (str/replace #"/_\.\./" "/../")
        (str/replace #"/_\.\." "/..")
        (str/replace #"/_\." "/.")
        (str/replace #"/_\./" "/./")
        (str/replace #"/_fs" "//")
        (str/replace #"/_fs/" "///")
        (str/replace #"_bs" "\\")
        (str/replace #"_q" "?"))))

(defn href->safe-href [href]
  (when href
    (-> href
        (str/replace #"/\.\./" "/_../")
        (str/replace #"/\.\./" "/_..")
        (str/replace #"/\." "/_.")
        (str/replace #"/\./" "/_./")
        (str/replace #"//" "/_fs")
        (str/replace #"///" "/_fs/")
        (str/replace #"\\" "_bs")
        (str/replace #"\?" "_q"))))

(defn safe-href->url-encoded [safe-href]
  (when safe-href
    (-> safe-href
        (str/replace #"/_\.\./" (js/encodeURIComponent "/../"))
        (str/replace #"/_\.\." (js/encodeURIComponent "/.."))
        (str/replace #"/_\./" (js/encodeURIComponent "/./"))
        (str/replace #"/_\." (js/encodeURIComponent "/."))
        (str/replace #"/_fs" (str "/" (js/encodeURIComponent "/")))
        (str/replace #"/_fs/" (str "/" (js/encodeURIComponent "/") "/"))
        (str/replace #"_bs" (js/encodeURIComponent "\\"))
        (str/replace #"_q" (js/encodeURIComponent "?")))))
