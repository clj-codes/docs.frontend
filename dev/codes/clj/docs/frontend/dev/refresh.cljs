(ns codes.clj.docs.frontend.dev.refresh
  "A place to add preloads for developer tools!"
  (:require [codes.clj.docs.frontend.core :as app]
            [codes.clj.docs.frontend.infra.routes.core :refer [init-routes!]]
            [helix.experimental.refresh :as r]))

(r/inject-hook!)
(init-routes!)

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn ^:dev/after-load refresh []
  (r/refresh!))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn ^:dev/after-load clear-cache-and-render! []
  (app/render))
