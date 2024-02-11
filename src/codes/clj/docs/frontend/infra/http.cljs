(ns codes.clj.docs.frontend.infra.http
  (:require [codes.clj.docs.frontend.infra.http.component :as http.component]
            [codes.clj.docs.frontend.infra.system.state :as system.state]))

(defn request! [request-input]
  (http.component/request (:http @system.state/components) request-input))
