(ns psqf-blackbox.parser.common
  (:require [clojure.spec :as s]))

(defn length-is [n]
  #(= n (count %)))

(def var-length ::var-length)

(def array-of-bytes-type (Class/forName "[B"))

(defn byte-array? [bytes]
  (= array-of-bytes-type (type bytes)))

(defn uchar->int [bytes]
  (if (= array-of-bytes-type (type bytes))
    (String. bytes)
    bytes))

(defn bytes->long [bytes]
  (if (and (byte-array? bytes) (-> bytes count (> 0)))
    (loop [acc 0
           bytes bytes]
      (if (seq bytes)
        (let [b (first bytes)
              b (if (< b 0)
                  (+ 256 b)
                  b)]
          (recur (+ (* acc 256) b) (next bytes)))
        acc))
    bytes))

(s/def ::uchar (s/and (s/conformer uchar->int) string?))
(s/def ::ushort (s/and (s/conformer bytes->long) integer?))
(s/def ::uint (s/and (s/conformer bytes->long) integer?))

(s/def ::vbinary (s/* integer?))
