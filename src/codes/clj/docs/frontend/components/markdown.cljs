(ns codes.clj.docs.frontend.components.markdown
  (:require ["@mantine/code-highlight" :refer [CodeHighlight]]
            ["@mantine/core" :refer [Blockquote Code Paper ScrollArea Tabs
                                     Textarea]]
            ["react-markdown" :default ReactMarkdown]
            ["remark-gfm" :default remarkGfm]
            [clojure.string :as str]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [helix.core :refer [$]]))

(defnc code-highlight [{:keys [children language] :as props}]
  ($ CodeHighlight {:& props
                    :styles (clj->js {:code {:fontSize "var(--mantine-font-size-sm)"}})
                    :withCopyButton true
                    :code (str children)
                    :language (or language "clojure")}))

(defnc markdown [{:keys [children]}]
  ($ ScrollArea.Autosize {:my "auto" :pl "1.5rem" :pr "1.5rem"
                          :style #js {:minHeight "15rem"}}
    ($ ReactMarkdown {:children children
                      :remarkPlugins #js [remarkGfm]
                      :components
                      #js {:blockquote (fn [props]
                                         ($ Blockquote {:py "0.25rem" :px "md"}
                                           (.-children props)))
                           :code (fn [props]
                                   (let [children (.-children props)
                                         className (.-className props)
                                         match (.exec #"language-(\w+)" (or className ""))]
                                     (if match
                                       ($ code-highlight {:& props
                                                          :children children
                                                          :language (last match)})
                                       ($ Code {:& props
                                                :style #js {:fontSize "var(--mantine-font-size-sm)"}
                                                :block (when-not (str/blank? children)
                                                         (str/includes? children "\n"))
                                                :className className
                                                :children children}))))}})))

(defnc markdown-editor [{:keys [placeholder text set-text]}]
  ($ Paper {:withBorder true}
    ($ Tabs {:defaultValue "write"}
      ($ Tabs.List
        ($ Tabs.Tab {:value "write"} "Write")
        ($ Tabs.Tab {:value "preview"} "Preview"))
      ($ Tabs.Panel {:value "write"}
        ($ Textarea {:value text
                     :size "md"
                     :onChange (fn [event] (set-text (-> event .-currentTarget .-value)))
                     :autosize true
                     :minRows 8
                     :placeholder placeholder
                     :p "xs"}))
      ($ Tabs.Panel {:value "preview"}
        ($ markdown (if (str/blank? text) "Nothing to preview" text))))))
