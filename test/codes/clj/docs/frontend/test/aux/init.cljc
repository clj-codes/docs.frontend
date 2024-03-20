(ns codes.clj.docs.frontend.test.aux.init
  (:require #?@(:node [["global-jsdom/register"]])
            [cljs.js :as js]
            [codes.clj.docs.frontend.infra.auth.state :as auth.state]
            [codes.clj.docs.frontend.infra.http.component :as http.component]
            [codes.clj.docs.frontend.infra.system.state :as system.state]
            [codes.clj.docs.frontend.test.aux.testing-library :as tlr]
            [shadow.cljs.modern :refer [defclass]]))

#_{:clj-kondo/ignore [:uninitialized-var]}
(def node?
  #?(:node true :cljs false))

#_{:clj-kondo/ignore [:unresolved-var]}
(defn ^:private mock-window-fns []
  (let [mock-fn (fn [& _args] nil)
        get-computed-style (.-getComputedStyle js/window)
        resize-observer (defclass ResizeObserver
                          (constructor [this] nil)
                          Object
                          (observe [this & args] nil)
                          (unobserve [this & args] nil)
                          (disconnect [this & args] nil))]

    (set! (.-getComputedStyle js/window)
          (fn [elt]
            (get-computed-style elt)))

    (.defineProperty js/Object js/window
                     "matchMedia"
                     #js {:writable true,
                          :value (fn [query]
                                   #js {:matches false,
                                        :media query,
                                        :onchange nil,
                                        :addListener mock-fn,
                                        :removeListener mock-fn,
                                        :addEventListener mock-fn,
                                        :removeEventListener mock-fn,
                                        :dispatchEvent mock-fn})})

    (set! (.-ResizeObserver js/window)
          resize-observer)

    (when node?
      (set! (.-ResizeObserver js/global)
            resize-observer))

    (set! (.-scrollTo js/window)
          mock-fn)))

(defn async-setup []
  (mock-window-fns))

#_{:clj-kondo/ignore [:unresolved-var]}
(defn async-cleanup []
  (tlr/cleanup)
  (auth.state/user nil))

#_{:clj-kondo/ignore [:unresolved-var]}
(defn sync-setup [f]
  (f)
  (tlr/cleanup))

#_{:clj-kondo/ignore [:unresolved-var]}
(defn mock-http-with [mocked-responses]
  (system.state/components assoc
                           :http (http.component/new-http-mock mocked-responses)))

#_{:clj-kondo/ignore [:unresolved-var]}
(defn get-mock-http-requests []
  @(-> @system.state/components :http :requests))

