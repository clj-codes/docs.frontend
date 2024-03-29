(ns codes.clj.docs.frontend.components.documents
  (:require ["@mantine/core" :refer [ActionIcon Anchor Avatar Badge Card Code
                                     Grid Group Text Title]]
            ["@tabler/icons-react" :refer [IconArrowRight]]
            [codes.clj.docs.frontend.components.navigation :refer [safe-anchor]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [helix.core :refer [$]]))

(defnc head-card-section [{:keys [id name header]}]
  (if header
    ($ (-> Card .-Section) {:withBorder true
                            :inheritPadding true
                            :py "sm"}
      ($ Title {:order 5} name))
    ($ (-> Card .-Section) {:component safe-anchor
                            :href (str "/" id)
                            :withBorder true
                            :inheritPadding true
                            :py "sm"}
      ($ Group {:justify "space-between"}
        ($ Title {:order 4} name)
        ($ ActionIcon {:variant "light"}
          ($ IconArrowRight))))))

(defnc card-project [{:keys [id name manifest paths sha tag url header]}]
  ($ Card {:id (str "card-project-" id)
           :key (str "card-project-" id)
           :data-testid (str "card-project-" id)
           :className (when header "components-documents-cards")
           :withBorder true
           :shadow "sm"}

    ($ head-card-section {:id id :name name :header header})

    ($ (-> Card .-Section) {:withBorder true :inheritPadding true :py "sm"}
      ($ Grid {:c "dimmed"}
        ($ (-> Grid .-Col)
          ($ Grid
            ($ (-> Grid .-Col) {:span #js {:base 12 :md 5}}
              ($ Title {:order 6} "Git")
              ($ Anchor {:size "sm" :href url} url))
            ($ (-> Grid .-Col) {:span #js {:base 12 :md 5}}
              ($ Title {:order 6} "Sha")
              ($ Code sha))
            (when tag
              ($ (-> Grid .-Col) {:span #js {:base 12 :md 2}}
                ($ Title {:order 6} "Tag")
                ($ Badge {:variant "primary"} tag)))))
        ($ (-> Grid .-Col) {:span 24}
          ($ Group
            ($ Group
              ($ Title {:order 6} "Manifest")
              ($ Text {:size "sm" :c "bright" :fw 500} (str manifest)))
            ($ Group
              ($ Title {:order 6} "Paths")
              ($ Group
                (map (fn [path] ($ Code {:key path :size "sm"} path)) paths)))))))))

(defnc card-namespace [{:keys [id name author doc filename git-source col row header]}]
  ($ Card {:id (str "card-namespace-" id)
           :key (str "card-namespace-" id)
           :data-testid (str "card-namespace-" id)
           :className (when header "components-documents-cards")
           :withBorder true
           :shadow "sm"}

    ($ head-card-section {:id id :name name :header header})

    ($ (-> Card .-Section) {:withBorder true :inheritPadding true :py "sm"}
      ($ Grid
        ($ (-> Grid .-Col)
          ($ Grid {:justify "space-between"}
            ($ (-> Grid .-Col)
              ($ Group
                ($ Title {:order 6} "Source")
                ($ Anchor {:size "md" :href git-source :fw 500}
                  ($ Text (rest (str filename ":" row ":" col))))))))
        (when doc
          ($ (-> Grid .-Col)
            ($ Title {:order 6} "Doc")
            ($ Code {:block true} doc)))))

    (when author
      ($ (-> Card .-Section) {:withBorder true :inheritPadding true :py "sm"}
        ($ Group {:justify "flex-start"}
          ($ Avatar {:alt "Author"})
          ($ Text author))))))
