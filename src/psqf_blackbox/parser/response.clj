(ns psqf-blackbox.parser.response
  (:require [clojure.spec :as s]
            [psqf-blackbox.parser.common :as c]
            [psqf-blackbox.parser.header :as h]))

(def structure (into h/structure))

