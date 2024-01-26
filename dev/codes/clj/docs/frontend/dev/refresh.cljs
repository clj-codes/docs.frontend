(ns codes.clj.docs.frontend.dev.refresh
  "A place to add preloads for developer tools!"
  (:require [codes.clj.docs.frontend.core :as app]
            [helix.experimental.refresh :as r]
            [refx.alpha :as refx]))

;; inject-hook! needs to run on application start.
;; For ease, we run it at the top level.
;; This function adds the react-refresh runtime to the page
(r/inject-hook!)

;; shadow-cljs allows us to annotate a function name with `:dev/after-load`
;; to signal that it should be run after any code reload. We call the `refresh!`
;; function, which will tell react to refresh any components which have a
;; signature created by turning on the `:fast-refresh` feature flag.
(defn ^:dev/after-load refresh []
  (r/refresh!))

(defn ^:dev/after-load clear-cache-and-render! []
  ;; The `:dev/after-load` metadata causes this function to be called
  ;; after shadow-cljs hot-reloads code. We force a UI update by clearing
  ;; the Refx subscription cache.
  (refx/clear-subscription-cache!)
  (app/render))
