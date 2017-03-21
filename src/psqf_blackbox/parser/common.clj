(ns psqf-blackbox.parser.common
  (:require [clojure.spec :as s]))

(s/def ::uchar (s/and string? #(= 1 (.length %))))

