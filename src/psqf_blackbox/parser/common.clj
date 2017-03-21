(ns psqf-blackbox.parser.common
  (:require [clojure.spec :as s]))

(defn length-is [n]
  #(= n (count %)))

(def var-length ::var-length)

(def array-of-bytes-type (Class/forName "[B"))

(defn uchar->int [bytes]
  (if (= array-of-bytes-type (type bytes))
    (String. bytes)
    bytes))

(defn bytes->long [bytes]
  (if (= array-of-bytes-type (type bytes))
    (long (+ (* (first bytes) 256) (second bytes)))
    bytes))

(s/def ::uchar (s/and (s/conformer uchar->int) string?))
(s/def ::ushort (s/and (s/conformer bytes->long) integer?))
(s/def ::uint (s/and (s/conformer bytes->long) integer?))

(s/def ::vbinary (s/* integer?))
