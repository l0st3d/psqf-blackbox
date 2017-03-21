(ns psqf-blackbox.parser.common
  (:require [clojure.spec :as s]))

(defn length-is [n]
  #(= n (count %)))

(def var-length ::var-length)

(def array-of-bytes-type (Class/forName "[B"))

(defn uchar->int [bytes]
  (if (= array-of-bytes-type (type bytes))
    (first bytes)
    bytes))

#_ (defn uchar->char [bytes]
     (if (= array-of-bytes-type (type bytes))
       (char (first bytes))
       bytes))

(defn ushort->long [bytes]
  (if (= array-of-bytes-type (type bytes))
    (long (+ (* (first bytes) 256) (second bytes)))
    bytes))

(s/def ::uchar (s/and (s/conformer uchar->int) integer?))
(s/def ::ushort (s/and (s/conformer ushort->long) integer?))
(s/def ::uint (s/and string?))
(s/def ::vbinary (s/* int?))



