(ns codes.clj.docs.frontend.infra.analytics
  (:require [codes.clj.docs.frontend.infra.system.state :as system.state]))

(defn google-tag-manager
  [ga-tag-id]
  (let [script (js/document.createElement "script")]
    (set! (.-src script) (str "https://www.googletagmanager.com/gtag/js?id="
                              ga-tag-id))
    (set! (.-async script) true)
    (js/document.head.prepend script)
    script))

(defn google-tag
  [ga-tag-id gtm-script]
  (let [script (js/document.createElement "script")]
    (set! (.-src script) (str "window.dataLayer = window.dataLayer || [];
                               function gtag(){dataLayer.push(arguments);}
                               gtag('js', new Date());

                               gtag('config', '" ga-tag-id "');"))
    (js/document.head.insertBefore script (.-nextSibling gtm-script))))

(defn google-analytics []
  (let [ga-tag-id (-> @system.state/components :config :ga-tag-id)]
    (when (seq ga-tag-id)
      (let [gtm-script (google-tag-manager ga-tag-id)]
        (google-tag ga-tag-id gtm-script)))))
