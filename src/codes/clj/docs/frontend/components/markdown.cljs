(ns codes.clj.docs.frontend.components.markdown
  (:require ["@mantine/core" :refer [Blockquote Code Paper ScrollArea Tabs
                                     Textarea useComputedColorScheme]]
            ["react-markdown$default" :as ReactMarkdown]
            ["react-syntax-highlighter/dist/esm/languages/prism/clojure$default" :as clojure-prism]
            ["react-syntax-highlighter/dist/esm/prism-light.js$default" :as SyntaxHighlighter]
            ["react-syntax-highlighter/dist/esm/styles/prism" :refer [materialDark
                                                                      materialLight]]
            ["remark-gfm$default" :as remarkGfm]
            [applied-science.js-interop :as j]
            [clojure.string :as str]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [goog.object]
            [helix.core :refer [$]]))

;; Register your languages here
(.registerLanguage SyntaxHighlighter "clojure" clojure-prism)

(defnc code-highlighter [{:keys [children language] :as props}]
  (let [current-scheme (useComputedColorScheme "light" #js {:getInitialValueInEffect true})]
    ($ SyntaxHighlighter {:& props
                          :PreTag "div"
                          :children children
                          :style (-> (if (= current-scheme "light")
                                       materialLight
                                       materialDark)
                                     (j/assoc-in! ["code[class*=\"language-\"]" :fontSize]
                                                  "var(--mantine-font-size-sm)")
                                     (j/assoc-in! ["pre[class*=\"language-\"]" :fontSize]
                                                  "var(--mantine-font-size-sm)"))
                          :language language})))

(defnc markdown [{:keys [children style padding]}]
  ($ ScrollArea.Autosize {:className "markdown-viewer"
                          :style style
                          :my "auto" :pl (:pl padding) :pr (:pr padding)}
    ($ ReactMarkdown {:children children
                      :remarkPlugins #js [remarkGfm]
                      :components
                      #js {:blockquote (fn [props]
                                         ($ Blockquote {:py "0.25rem" :px "md"}
                                           (.-children props)))
                           :code (fn [props]
                                   (j/let [^js {:keys [children className]} props
                                           match (.exec #"language-(\w+)" (or className ""))]
                                     (if match
                                       ($ code-highlighter {:& props
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
        ($ Textarea {:data-testid "markdown-editor-textarea"
                     :value text
                     :size "md"
                     :onChange (fn [event]
                                 (set-text (-> event .-currentTarget .-value)))
                     :autosize true
                     :minRows 8
                     :placeholder placeholder
                     :p "xs"}))
      ($ Tabs.Panel {:value "preview"}
        ($ markdown {:style #js {:minHeight "15rem"}
                     :padding {:pl "1.5rem" :pr "1.5rem"}}
          (if (str/blank? text) "Nothing to preview" text))))))
