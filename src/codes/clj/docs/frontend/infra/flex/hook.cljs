(ns codes.clj.docs.frontend.infra.flex.hook
  (:require ["react" :as react]
            ["use-sync-external-store/shim/with-selector" :as with-selector]
            [town.lilac.flex :as flex]))

(defn- cljs-deps
  [deps]
  (let [ref (react/useRef deps)]
    (when (not= (.-current ref) deps)
      (set! (.-current ref) deps))
    #js [(.-current ref)]))

(defn use-flex
  "React hook to subscribe to flex sources."
  [container]
  (let [subscribe-fn (fn [callback]
                       (let [signal (flex/signal @container)
                             listener (flex/listen signal callback)]
                         #(flex/dispose! listener)))
        snapshot-fn (fn [] @container)
        [subscribe snapshot] (react/useMemo
                              (fn [] [subscribe-fn snapshot-fn])
                              (cljs-deps @container))]
    (react/useDebugValue @container str)
    (with-selector/useSyncExternalStoreWithSelector subscribe snapshot nil identity =)))
