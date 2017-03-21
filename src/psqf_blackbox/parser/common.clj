(ns psqf-blackbox.parser.common
  (:require [clojure.spec :as s]
            [clojure.string :as st]))

(defn length-is [n]
  #(= n (count %)))

(def var-length ::var-length)

(def array-of-bytes-type (Class/forName "[B"))

(defn byte-array? [bytes]
  (= array-of-bytes-type (type bytes)))

(defn uchar->string [bytes]
  (if (= array-of-bytes-type (type bytes))
    (String. bytes)
    bytes))

(defn bytes->long [bytes]
  (if (byte-array? bytes)
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

(defn bytes->hex-str [bytes]
  (st/join (map #(format "%02X" %) (seq bytes))))

(defn debug-bytes [bytes]
  (apply prn '->> (map #(format "%X" %) (seq bytes)))
  bytes)

(s/def ::uchar byte-array?)
(s/def ::ushort (s/and (s/conformer bytes->long) integer?))
(s/def ::uint (s/and (s/conformer bytes->long) integer?))

(s/def ::vbinary (s/* integer?))
