(ns codes.clj.docs.frontend.panels.license.view
  (:require ["@mantine/core" :refer [Code Container Group Space Text Title]]
            [codes.clj.docs.frontend.infra.helix :refer [defnc]]
            [helix.core :refer [$]]))

(def license-text
  "This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED \" AS IS \", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to <http://unlicense.org/>")

(def contribution-license-text
  "I dedicate any and all copyright interest in this software to the
public domain. I make this dedication for the benefit of the public at
large and to the detriment of my heirs and successors. I intend this
dedication to be an overt act of relinquishment in perpetuity of all
present and future rights to this software under copyright law.")

(defnc license []
  ($ Container {:p "sm"}
    ($ Group
      ($ Title {:id "license" :order 2} "License")
      ($ Text {:size "xl"}
        "To opt out of the copyright industry's game altogether and set your code free, this website and all content generated on it is "
        ($ Text {:component "a" :href "https://stpeter.im/writings/essays/publicdomain.html" :inherit true :fw 700} "public domain")
        " using the following (un)licensing statement:")
      ($ Code {:block true
               :style #js {:fontSize "var(--mantine-font-size-md)"}}
        license-text))

    ($ Space {:h "xl"})

    ($ Group
      ($ Title {:id "contributions" :order 2} "Unlicensing Contributions")
      ($ Text {:size "xl"}
        "In order to ensure this project remains completely free and unencumbered by anyone's copyright monopoly,
        be aware that any contributions, code, examples, notes and see alsos are on the public domain.")
      ($ Code {:block true
               :style #js {:fontSize "var(--mantine-font-size-md)"}}
        contribution-license-text))))
