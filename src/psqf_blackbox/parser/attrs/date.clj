(ns psqf-blackbox.parser.attrs.date
  (:require [clojure.spec :as s]
            [clojure.string :as st]
            [psqf-blackbox.parser.common :as c]))

(s/def ::length ::c/uint)
(s/def ::year ::c/uint)
(s/def ::month ::c/uint)
(s/def ::day ::c/uint)
(s/def ::hour ::c/uint)
(s/def ::mins ::c/uint)
(s/def ::seconds ::c/uint)

(def structure [::length 1
                ::year 2
                ::month 1
                ::day 1
                ::hour 1
                ::mins 1
                ::seconds 1])

(defmethod c/parse-attrs 1200 [ios & rest]
  (c/parse-structure ios structure))
