(ns psqf-blackbox.parser.attrs.account
  (:require [clojure.spec :as s]
            [psqf-blackbox.parser.common :as c]))

(s/def ::length ::c/uint)
(s/def ::networkcode ::c/uint)
(s/def ::licencecode ::c/uint)
(s/def ::accounttype ::c/uint)
(s/def ::accountnum ::c/uint)

(def structure [::length 1
                ::networkcode 1
                ::licencecode 4
                ::accounttype 1
                ::accountnum 8])

(defmethod c/parse-attrs 1125 [ios & rest]
  (c/parse-structure ios structure))
