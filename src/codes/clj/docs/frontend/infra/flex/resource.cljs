(ns codes.clj.docs.frontend.infra.flex.resource
  (:require [town.lilac.flex :as flex]
            [town.lilac.flex.promise :as flex.promise]))

(defn resource
  "Same as flex.promise/resource, but don't execute the promise on declare.

  Returns a flex source that updates its state based on a promise-returning
  function. Calling the source like a function will execute the `fetcher`,
  a function that returns a promise, updating the state of the resource as it
  proceeds. Derefing will return the last value retrieved by `fetcher`.

  The return value is also an associative containing the following keys:
  `:state` - a source containing one of :unresolved, :pending, :ready,
             :refreshing, :error
  `:error` - a source containing last error from `fetcher`
  `:value` - a source containing last value retrieved by `fetcher`
  `:loading?` - a signal containing true/false whether currently waiting for a
                promise returned by `fetcher` to fulfill
  `:fetcher` - the original `fetcher` function
  `:p` - the last promise returned by `fetcher`"
  [fetcher]
  (let [state (flex/source :unresolved)
        error (flex/source nil)
        value (flex/source nil)
        loading? (flex/source (case @state
                                (:pending :refreshing) true
                                false))]
    (flex.promise/->Resource state error value loading? fetcher nil)))
