(ns codes.clj.docs.frontend.adapters.time)

(def MINUTE 60)
(def HOUR (* MINUTE 60))
(def DAY (* HOUR 24))
(def WEEK (* DAY 7))
(def MONTH (* DAY 30))
(def YEAR (* DAY 365))

(defn time-since [date now]
  (let [seconds-ago (-> (- now date)
                        (/ 1000)
                        Math/round)
        {:keys [divisor unit]} (cond
                                 (< seconds-ago MINUTE) {:divisor 1 :unit "second"}
                                 (< seconds-ago HOUR) {:divisor MINUTE :unit "minute"}
                                 (< seconds-ago DAY) {:divisor HOUR :unit "hour"}
                                 (< seconds-ago WEEK) {:divisor DAY :unit "day"}
                                 (< seconds-ago MONTH) {:divisor WEEK :unit "week"}
                                 (< seconds-ago YEAR) {:divisor MONTH :unit "month"}
                                 :else {:divisor YEAR :unit "year"})
        value (-> (/ seconds-ago divisor)
                  Math/floor)
        plural? (if (> value 1) "s" "")]
    (str value " " unit plural? " ago")))
