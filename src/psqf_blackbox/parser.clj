(ns psqf-blackbox.parser
  (:require [clojure.spec :as s]
            [psqf-blackbox.parser.header :as h]
            [psqf-blackbox.parser.common :as c]
            [clojure.java.io :as io])
  (:import [java.io ByteArrayOutputStream]))

;; TODO rename fns to have ! at the end!
(defn parse-header [ios]
  (c/parse-structure ios h/structure))

(defn parse-body [ios header]
  (c/consume-bytes! ios (::h/body-length header))
  nil)

;; TODO don't copy byte array again if we're too slow - can add up the
;; lengths from the structures to check length
(defn parse-attrs [ios header]          
  (let [attr-ios (io/input-stream (c/consume-bytes! ios (::h/attribute-length header)))]
    (->> #(c/parse-attrs attr-ios header)
         repeatedly
         (take-while (complement #{::c/done}))
         (reduce merge))))

(defn parser [input]
  (let [ios    (io/input-stream input)
        header (parse-header ios)
        body   (parse-body ios header)
        attrs  (parse-attrs ios header)]
    (merge header body attrs)))

