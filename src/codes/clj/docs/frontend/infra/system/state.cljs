(ns codes.clj.docs.frontend.infra.system.state
  (:require [codes.clj.docs.frontend.config :as config]
            [codes.clj.docs.frontend.infra.http.component :as http]
            [town.lilac.flex :as flex]))

(def components
  (flex/source {:config config/config
                :http (http/new-http config/config)}))
