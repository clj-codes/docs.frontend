(ns codes.clj.docs.frontend.infra.analytics)

(goog-define GA_TAG_ID "G-0123456789")

(defn google-tag-manager
  [ga-tag-id]
  (let [script (js/document.createElement "script")]
    (set! (.-src script) (str "https://www.googletagmanager.com/gtag/js?id="
                              ga-tag-id))
    (set! (.-async script) true)
    (js/document.head.appendChild script)))

(defn google-tag
  [ga-tag-id]
  (let [script (js/document.createElement "script")]
    (set! (.-src script) (str "window.dataLayer = window.dataLayer || [];
                               function gtag(){dataLayer.push(arguments);}
                               gtag('js', new Date());

                               gtag('config', '" ga-tag-id "');"))
    (js/document.head.appendChild script)))

(defn google-analytics []
  (google-tag-manager GA_TAG_ID)
  (google-tag GA_TAG_ID))
