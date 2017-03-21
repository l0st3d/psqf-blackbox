(ns psqf-blackbox.parser.common
  (:require [clojure.spec :as s]))

(defn length-is [n]
  #(= n (count %)))

(s/def ::uchar (s/and string?))
(s/def ::ushort (s/and string?))
(s/def ::uint (s/and string?))

