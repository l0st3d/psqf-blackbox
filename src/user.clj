(ns user
  (:require [psqf-blackbox.parser :as p]
            [psqf-blackbox.parser.header :as h]
            [psqf-blackbox.parser.common :as c]
            [clojure.spec :as s]
            [byte-streams :as bs]))

(defn test-parser []
  ((p/parser [::h/version 1])
   (.getBytes "testingsomethingsilly")))


(defn test-parser2 []
  ((p/parser [::h/version 1])
    (bs/to-byte-array (java.io.File. "test-resources/bet_request.bin"))))