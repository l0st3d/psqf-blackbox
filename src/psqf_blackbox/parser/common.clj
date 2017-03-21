(ns psqf-blackbox.parser.common
  (:require [clojure.spec :as s]))

(defn length-is [n]
  #(= n (count %)))

(def var-length ::var-length)

(def array-of-bytes-type (Class/forName "[B"))

(defn uchar->string [bytes]
  (if (= array-of-bytes-type (type bytes))
    (String. bytes)
    bytes))

(s/def ::uchar (s/and (s/conformer uchar->string) string?))
(s/def ::ushort (s/and string?))
(s/def ::uint (s/and string?))
(s/def ::vbinary (s/* int?))



