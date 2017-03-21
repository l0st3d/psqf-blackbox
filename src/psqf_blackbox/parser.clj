(ns psqf-blackbox.parser
  (:require [clojure.spec :as s]
            [psqf-blackbox.parser.header :as h]
            [psqf-blackbox.parser.common :as c]
            [clojure.java.io :as io])
  (:import [java.io ByteArrayOutputStream]))

(def header h/structure)

(defn consume-bytes! [rdr length]
  (when (-> length (> 0))
    (let [ba (byte-array length)]
      (.read rdr ba)
      ba)))

(defn make-parser [input-defs]
  (fn parser [input]
    (let [rdr (io/input-stream input)]
      (loop [acc        {}
             input-defs input-defs]
        (if-let [[tag length] (seq input-defs)]
          (let [length (if (= c/var-length length)
                         (.read rdr)
                         length)
                bytes  (consume-bytes! rdr length)
                val    (s/conform tag bytes)]
            (when (= ::s/invalid val)
              (throw (ex-info "Invalid data" {:input-def (first input-defs)
                                              :length    length
                                              :errors    (s/explain-data tag bytes)})))
            (recur (assoc acc tag val) (next (next input-defs))))
          acc)))))

