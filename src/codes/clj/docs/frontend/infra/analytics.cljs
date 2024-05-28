(ns codes.clj.docs.frontend.infra.analytics)

(goog-define GA_TAG_ID "G-0123456789")

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
  (let [gtm-script (google-tag-manager GA_TAG_ID)]
    (google-tag GA_TAG_ID gtm-script)))
