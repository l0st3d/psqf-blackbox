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

(defn un-sign [b]
  (if (< b 0)
    (+ 256 b)
    b))

(defn bytes->long [bytes]
  (if (byte-array? bytes)
    (loop [acc 0
           bytes bytes]
      (if (seq bytes)
        (recur (+ (* acc 256) (un-sign (first bytes))) (next bytes))
        acc))
    bytes))

(defn bytes->hex-str [bytes]
  (st/join (map #(format "%02X" %) (seq bytes))))

(defn debug-bytes [bytes]
  (prn '->> (bytes->hex-str bytes))
  bytes)

(s/def ::uchar byte-array?)
(s/def ::uint (s/and (s/conformer bytes->long) integer?))

(s/def ::vbinary (s/* integer?))

(defn consume-bytes! [ios length]
  (when (-> length (> 0))
    (let [ba (byte-array length)]
      (.read ios ba)
      ba)))

(defn parse-structure [ios structure]
  (loop [acc        {}
         input-defs structure]
    (if-let [[tag length] (seq input-defs)]
      (let [length (if (= var-length length)
                     (.read ios)
                     length)
            bytes  (consume-bytes! ios length)
            val    (s/conform tag bytes)]
        (when (= ::s/invalid val)
          (throw (ex-info "Invalid data" {:input-def (first input-defs)
                                          :length    length
                                          :errors    (s/explain-data tag bytes)})))
        (recur (assoc acc tag val) (next (next input-defs))))
      acc)))

(defn- read-attr-code! [^java.io.InputStream ios & rest]
  (bytes->long (consume-bytes! ios 2)))

(defmulti parse-attrs read-attr-code!)

(defmethod parse-attrs -1 [ios & rest] nil)
