(ns psqf-blackbox.parser.attrs.return-address
  (:require [clojure.spec :as s]
            [clojure.string :as st]
            [psqf-blackbox.parser.common :as c]))

(s/def ::length ::c/uint)
(s/def ::port ::c/uint)
(s/def ::ip-address (s/and ::c/uchar (s/conformer (c/include ::c/str #(st/join "." (map c/un-sign (seq %)))))))

(def structure [::length     1
                ::port       2
                ::ip-address 4])

(defmethod c/parse-attrs 1036 [ios & rest]
  (c/parse-structure ios structure))
