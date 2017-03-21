(ns user
  (:require [psqf-blackbox.core :as bb]
            [psqf-blackbox.parser.header :as h]
            [psqf-blackbox.parser.common :as c]
            [clojure.spec :as s]))

(defn test-parser []
  ((bb/parser [::bb/testing 7 ::bb/something 9 ::bb/silly 5]) (.getBytes "testingsomethingsilly")))
