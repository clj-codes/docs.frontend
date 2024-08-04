(ns codes.clj.docs.frontend.panels.dashboards.view
  (:require ["@mantine/core" :refer [Container Space]]
            [codes.clj.docs.frontend.infra.flex.hook :refer [use-flex]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [codes.clj.docs.frontend.panels.dashboards.components :as components]
            [codes.clj.docs.frontend.panels.dashboards.state :as state]
            [helix.core :refer [$]]
            [helix.hooks :refer [use-effect]]))

(defnc all []
  (let [latest-interactions-response (use-flex state/latest-interactions-response)
        top-authors-response (use-flex state/top-authors-response)]

    (use-effect
      :once
      (state/latest-interactions-fetch)
      (state/top-authors-fetch))

    ($ Container {:p "sm"}

      ($ components/top-authors-list {:& top-authors-response})

      ($ Space {:h "xl"})

      ($ components/latest-interactions-list {:& latest-interactions-response}))))
