(ns codes.clj.docs.frontend.aux.testing-library
  (:require ["@testing-library/react" :as tlr]
            [helix.core :refer [$]]))

(def wait-for tlr/waitFor)

(defn text [el]
  (.-textContent el))

(defn length [el]
  (.-length el))

(defn find-by-text
  [el text]
  (.findByText el text))

(defn click
  [^js/Element el]
  (.click tlr/fireEvent el))

(defn change
  [^js/Element el value]
  (.change tlr/fireEvent el (clj->js {:target {:value value}})))

(defn tag
  [element tag-name]
  (.getElementsByTagName element tag-name))

(defn query
  [element query]
  (.querySelector element query))

(defn testing-container []
  (as-> (js/document.createElement "div") div
    (js/document.body.appendChild div)))

(defn render
  [component]
  (tlr/render ($ component)
              #js {:container (testing-container)}))

(defn cleanup
  ([] (tlr/cleanup))
  ([after-fn] (tlr/cleanup)
              (after-fn)))
