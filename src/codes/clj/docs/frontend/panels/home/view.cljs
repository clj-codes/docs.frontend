(ns codes.clj.docs.frontend.panels.home.view
  (:require ["@mantine/core" :refer [Anchor Card Container Grid Text Title]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [helix.core :refer [$]]
            [helix.dom :as dom]))

(defnc home []
  ($ Container {:size "md"}
     ($ Grid {:id "why"}
        ($ Grid.Col {:span 12}
           (dom/section
             ($ Title {:order 1}
                ($ Anchor {:component "a"
                           :variant "gradient"
                           :gradient #js {:from "blue" :to "cyan"}
                           :inherit true
                           :underline "never"
                           :href "https://docs.clj.codes"}
                   "Docs.clj.codes")
                " is a modern community-powered documentation and examples repository for the "
                ($ Anchor {:component "a"
                           :variant "gradient"
                           :gradient #js {:from "cyan" :to "green"}
                           :inherit true
                           :underline "never"
                           :href "https://clojure.org"}
                   "Clojure programming language"))))
        ($ Grid.Col {:span 12}
           ($ Text {:size "xl" :style #js {:paddingTop "1rem"}}
              "Built on a tech stack featuring "
              ($ Text {:component "a" :href "https://github.com/lilactown/helix" :inherit true :fw 700} "helix")
              " and "
              ($ Text {:component "a" :href "https://github.com/lilactown/flex" :inherit true :fw 700} "flex")
              " on the frontend and powered by postgres and "
              ($ Text {:component "a" :href "https://github.com/juji-io/datalevin" :inherit true :fw 700} "datalevin")
              " on the backend.")))

     ($ Grid {:id "features"}
        ($ Grid.Col {:span 12}
           (dom/section
             (dom/h2 "Key Features")
             ($ Grid  {:grow true :gutter "lg"}
                ($ Grid.Col {:span 4}
                   ($ Card {:shadow "sm" :padding "lg" :radius "lg" :style #js {:minHeight "14rem"}}
                      (dom/h3 "Comprehensive Documentation")
                      (dom/p "Explore in-depth documentation for a vast array of Clojure libraries.")))
                ($ Grid.Col {:span 4}
                   ($ Card {:shadow "sm" :padding "lg" :radius "lg" :style #js {:minHeight "14rem"}}
                      (dom/h3 "Seamless Git Integration")
                      (dom/p "Documentation is directly parsed from their Git repositories.")))
                ($ Grid.Col {:span 4}
                   ($ Card {:shadow "sm" :padding "lg" :radius "lg" :style #js {:minHeight "14rem"}}
                      (dom/h3 "Social Interaction")
                      (dom/p "Join a vibrant community of Clojure enthusiasts.")))
                ($ Grid.Col {:span 4}
                   ($ Card {:shadow "sm" :padding "lg" :radius "lg" :style #js {:minHeight "14rem"}}
                      (dom/h3 "Extensive Search Capabilities")
                      (dom/p "Harness the power of Datalevin for lightning-fast full-text search.")))
                ($ Grid.Col {:span 4}
                   ($ Card {:shadow "sm" :padding "lg" :radius "lg" :style #js {:minHeight "14rem"}}
                      (dom/h3 "Easy Contribution")
                      (dom/p "Become a part of the documentation ecosystem.")))))))))
