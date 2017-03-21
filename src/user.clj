(ns user
  (:require [psqf-blackbox.parser :as p]
            [psqf-blackbox.parser.header :as h]
            [psqf-blackbox.parser.common :as c]
            [clojure.spec :as s]))

(defn test-parser []
  ((p/parser [::h/version 1])
   (.getBytes "testingsomethingsilly")))
