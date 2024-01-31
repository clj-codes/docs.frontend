(ns codes.clj.docs.frontend.aux.testing-library
  (:require ["@mantine/core" :refer [MantineProvider]]
            ["@testing-library/dom" :as tld]
            ["@testing-library/react" :as tlr]
            [codes.clj.docs.frontend.config :refer [theme]]
            [helix.core :refer [$]]))

(def wait-for tlr/waitFor)

(def screen tld/screen)

(defn document []
  (tlr/getQueriesForElement (.-body js/document) tlr/queries))

(defn text [el]
  (.-textContent el))

(defn length [el]
  (.-length el))

(defn find-by-text
  [el text]
  (.findByText el text))

(defn get-by-testid
  [el testid]
  (.getByTestId el testid))

(defn get-all-by-testid
  [el testid]
  (.getAllByTestId el testid))

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

(defn query-all
  [element query]
  (.querySelectorAll element query))

(defn testing-container []
  (let [div (js/document.createElement "div")]
    (.setAttribute div "data-testid" "tlr-test-root")
    (js/document.body.appendChild div)))

(defn render
  [component]
  (tlr/render component
              #js {:container (testing-container)}))

(defn mantine-render
  [component]
  (tlr/render ($ MantineProvider {:theme theme}
                 component)
              #js {:container (testing-container)}))

(defn cleanup
  ([] (tlr/cleanup))
  ([after-fn] (tlr/cleanup)
              (after-fn)))
