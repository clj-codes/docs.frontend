(ns codes.clj.docs.frontend.infra.flex.hook
  (:require ["react" :as react]
            ["use-sync-external-store/shim/with-selector" :as with-selector]
            [town.lilac.flex :as flex]))

(defn use-flex
  "React hook to subscribe to flex sources."
  [container]
  (let [subscribe-fn (fn [callback]
                       (let [listener (flex/listen container callback)]
                         #(flex/dispose! listener)))
        snapshot-fn (fn [] @container)
        [subscribe snapshot] (react/useMemo
                              (fn [] [subscribe-fn snapshot-fn])
                              #js [container])]
    (with-selector/useSyncExternalStoreWithSelector subscribe snapshot nil identity =)))
