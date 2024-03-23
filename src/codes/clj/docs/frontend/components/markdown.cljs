(ns codes.clj.docs.frontend.components.markdown
  (:require ["@mantine/core" :refer [Blockquote Code Container Paper
                                     ScrollArea Tabs Text Textarea
                                     useComputedColorScheme]]
            ["react-markdown$default" :as ReactMarkdown]
            ["react-syntax-highlighter/dist/esm/languages/prism/clojure$default" :as clojure-prism]
            ["react-syntax-highlighter/dist/esm/prism-light$default" :as SyntaxHighlighter]
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

(defnc syntax-highlighter [{:keys [children language min-height] :as props}]
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
                                                  "var(--mantine-font-size-sm)")
                                     (j/assoc-in! ["code[class*=\"language-\"]" :borderRadius]
                                                  "var(--mantine-radius-md)")
                                     (j/assoc-in! ["pre[class*=\"language-\"]" :borderRadius]
                                                  "var(--mantine-radius-md)")
                                     (j/assoc-in! ["code[class*=\"language-\"]" :minHeight]
                                                  min-height)
                                     (j/assoc-in! ["pre[class*=\"language-\"]" :minHeight]
                                                  min-height))
                          :language language})))

(defnc code-viewer [{:keys [children language min-height] :as props}]
  ($ (-> ScrollArea .-Autosize) {:className "code-viewer"}
    ($ syntax-highlighter {:& props
                           :children children
                           :language language
                           :min-height min-height})))

(defnc markdown-viewer [{:keys [children]}]
  ($ (-> ScrollArea .-Autosize) {:className "markdown-viewer"}
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
                                       ($ syntax-highlighter {:& props
                                                              :children children
                                                              :language (last match)})
                                       ($ Code {:& props
                                                :style #js {:fontSize "var(--mantine-font-size-sm)"}
                                                :block (when-not (str/blank? children)
                                                         (str/includes? children "\n"))
                                                :className className
                                                :children children}))))}})))

(defnc previewer-base [{:keys [children placeholder text set-text]}]
  ($ Paper {:style #js {:minHeight "17rem"}
            :withBorder true}
    ($ Tabs {:defaultValue "write"}
      ($ (-> Tabs .-List)
        ($ (-> Tabs .-Tab) {:value "write"} "Write")
        ($ (-> Tabs .-Tab) {:value "preview"} "Preview"))
      ($ (-> Tabs .-Panel) {:value "write"}
        ($ Textarea {:data-testid "markdown-editor-textarea"
                     :value text
                     :size "md"
                     :onChange (fn [event]
                                 (set-text (-> event .-currentTarget .-value)))
                     :autosize true
                     :minRows 8
                     :placeholder placeholder
                     :p "xs"}))
      ($ (-> Tabs .-Panel) {:value "preview"}
        children))))

(defnc previewer-markdown [{:keys [placeholder text set-text]}]
  ($ previewer-base {:placeholder placeholder :text text :set-text set-text}
    ($ Container {:fluid true :my "auto" :px "1.5rem"}
      (if (str/blank? text)
        ($ Text {:py "1rem"} "Nothing to preview")
        ($ markdown-viewer text)))))

(defnc previewer-code [{:keys [placeholder text set-text]}]
  ($ previewer-base {:placeholder placeholder :text text :set-text set-text}
    ($ Container {:fluid true :my "auto" :px "0.6rem" :py "0.35rem"}
      (if (str/blank? text)
        ($ Text {:px "0.9rem" :py "0.5rem"} "Nothing to preview")
        ($ code-viewer {:language "clojure" :min-height "13rem"} text)))))
