(ns user
  (:require [psqf-blackbox.parser :as p]
            [psqf-blackbox.parser.header :as h]
            [psqf-blackbox.parser.common :as c]
            [clojure.spec :as s]
            [clojure.java.io :as io]))

(s/def ::my-int integer?)

