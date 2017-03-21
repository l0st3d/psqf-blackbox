(ns psqf-blackbox.parser
  (:require [clojure.spec :as s]
            [psqf-blackbox.parser.header :as h]
            [psqf-blackbox.parser.common :as c]
            [clojure.java.io :as io])
  (:import [java.io ByteArrayOutputStream]))

(def header [::h/version        1
             ::h/fsc            2
             ;; ::h/licence-code   4
             ;; ::h/tag-id         2
             ;; ::h/tag-type       1
             ;; ::h/body-length    2
             ;; ::h/attribute      2
             ;; ::h/transaction-id 8
             ::c/vbinary        c/var-length
             ])

(defn consume-bytes! [rdr length]
  (prn 'consuming length 'bytes)
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

(defn parser [input-defs]
  (let [input-spec   (s/* (s/cat :name keyword? :size integer?))
        input-defs   (if (s/valid? input-spec input-defs)
                       (s/conform input-spec input-defs)
                       (throw (ex-info "Illegal Arg"
                                       {:input-defs input-defs
                                        :error      (s/explain-data input-spec input-defs)})))
        fixed-widths (mapcat #(repeat (:size %) (:name %)) input-defs)
        cat-spec     (->> input-defs
                          (map :name)
                          (mapcat #(vector (keyword (name %)) %))
                          (cons `s/cat)
                          eval)]
    (fn [input]
      (let [result (->> (seq input)
                        (map vector fixed-widths)
                        (partition-by first)
                        (map #(apply str (map second %))))]
        (if (s/valid? cat-spec result)
          (s/conform cat-spec result)
          (throw (ex-info "Invalid data" {:input input
                                          :errors (s/explain-data cat-spec result)})))))))
