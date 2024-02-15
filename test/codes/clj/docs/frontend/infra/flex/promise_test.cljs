(ns codes.clj.docs.frontend.infra.flex.promise-test
  (:require [cljs.test :refer [async deftest is use-fixtures]]
            [codes.clj.docs.frontend.aux.init :refer [async-cleanup
                                                      async-setup]]
            [codes.clj.docs.frontend.infra.flex.promise :as flex.promise]
            [promesa.core :as p]
            [town.lilac.flex :as flex]))

(use-fixtures :each
  {:before async-setup
   :after async-cleanup})

(defn sleep
  [ms]
  (js/Promise. (fn [res _rej]
                 (js/setTimeout
                  (fn [] (res))
                  ms))))

(deftest custom-flex-resource-test
  (async done
    (p/catch
      (p/do (let [r (flex.promise/resource (fn []
                                             (-> (sleep 100)
                                                 (.then (constantly 42)))))
                  _call-r (r) ; this is now needed
                  *calls (atom [])
                  s (flex/signal (inc @(:value r)))
                  _fx (flex/effect [] (swap! *calls conj @s))]
              (is (= :pending @(:state r)))
              ;; (is (= :unresolved @(:state r)))
              (is (= nil @(:value r)))
              (is (= nil @(:error r)))
              (is (= [1] @*calls))
              ;; (r)
              ;; (is (= :pending @(:state r)))
              (-> (sleep 101)
                  (.then (fn []
                           (is (= :ready @(:state r)))
                           (is (= 42 @(:value r)))
                           (is (= [1 43] @*calls))))
                  (.then done done))))
      (fn [err] (is (= nil err))
        (done)))))

(deftest custom-flex-resource-with-args-test
  (async done
    (p/catch
      (p/do (let [r (flex.promise/resource (fn [ms]
                                             (-> (sleep ms)
                                                 (.then (constantly 42)))))
                  _call-r (r 200) ; this is now needed
                  *calls (atom [])
                  s (flex/signal (inc @(:value r)))
                  _fx (flex/effect [] (swap! *calls conj @s))]
              (is (= :pending @(:state r)))
              ;; (is (= :unresolved @(:state r)))
              (is (= nil @(:value r)))
              (is (= nil @(:error r)))
              (is (= [1] @*calls))
              ;; (r)
              ;; (is (= :pending @(:state r)))
              (-> (sleep 101)
                  (.then (fn []
                           (is (= :pending @(:state r)))
                           (is (= nil @(:value r)))
                           (is (= [1] @*calls))))
                  (.then done done))
              (-> (sleep 201)
                  (.then (fn []
                           (is (= :ready @(:state r)))
                           (is (= 42 @(:value r)))
                           (is (= [1 43] @*calls))))
                  (.then done done))))
      (fn [err] (is (= nil err))
        (done)))))
