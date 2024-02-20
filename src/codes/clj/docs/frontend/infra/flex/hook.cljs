(ns codes.clj.docs.frontend.infra.flex.hook
  (:require ["react" :as react]
            ["use-sync-external-store/shim/with-selector" :as with-selector]
            [town.lilac.flex :as flex]))

(defn use-flex
  "React hook to subscribe to flex sources."
  [container]
  (let [subscribe (react/useCallback
                   (fn [callback]
                     (let [listener (flex/listen container callback)]
                       #(flex/dispose! listener)))
                   #js [container])
        snapshot (react/useCallback
                  (fn [] (binding [flex/*warn-nonreactive-deref* false]
                           @container))
                  #js [container])]
    (with-selector/useSyncExternalStoreWithSelector subscribe snapshot nil identity =)))
