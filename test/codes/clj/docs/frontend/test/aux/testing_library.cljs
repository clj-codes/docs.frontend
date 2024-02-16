(ns codes.clj.docs.frontend.test.aux.testing-library
  (:require ["@mantine/core" :refer [MantineProvider]]
            ["@testing-library/react" :as tlr]
            [codes.clj.docs.frontend.config :refer [theme]]
            [helix.core :refer [$]]))

(def wait-for tlr/waitFor)

(defn document []
  (tlr/getQueriesForElement (.-body js/document) tlr/queries))

(defn click
  [^js/Element el]
  (.click tlr/fireEvent el))

(defn change
  [^js/Element el value]
  (.change tlr/fireEvent el (clj->js {:target {:value value}})))

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
