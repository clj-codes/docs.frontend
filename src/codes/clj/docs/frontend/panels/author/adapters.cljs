(ns codes.clj.docs.frontend.panels.author.adapters)

(defn ^:private author-socials->summary [socials]
  (reduce (fn [{notes-a :notes examples-a :examples see-alsos-a :see-alsos
                :as accum}
               {notes-c :notes examples-c :examples see-alsos-c :see-alsos}]
            (assoc accum
                   :notes (+ notes-a (count notes-c))
                   :examples (+ examples-a (count examples-c))
                   :see-alsos (+ see-alsos-a (count see-alsos-c))))
          {:notes 0 :examples 0 :see-alsos 0}
          socials))

(defn ^:private filter-socials [socials]
  (reduce-kv
   (fn [a k v]
     (if (not (zero? v))
       (assoc a k v)
       a))
   {}
   socials))

(defn ->string-summary [author]
  (let [socials (-> author :socials author-socials->summary filter-socials sort)]
    (case (count socials)
      0 "This user hasn't authored any content."
      1 (let [[k1 v1] (first socials)]
          (str "This has user has authored " v1 " " (name k1) "."))
      2 (let [[k1 v1] (first socials)
              [k2 v2] (last socials)]
          (str "This has user has authored "
               v1 " " (name k1) " and " v2 " " (name k2) "."))
      3 (let [[k1 v1] (first socials)
              [k2 v2] (second socials)
              [k3 v3] (last socials)]
          (str "This has user has authored "
               v1 " " (name k1) ", "
               v2 " " (name k2) " and "
               v3 " " (name k3) ".")))))
