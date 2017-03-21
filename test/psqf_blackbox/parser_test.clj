(ns psqf-blackbox.parser-test
  (:require [psqf-blackbox.parser :as p]
            [psqf-blackbox.parser.header :as h]
            [psqf-blackbox.parser.common :as c]
            [clojure.test :refer [deftest testing is are use-fixtures]]
            [clojure.java.io :as io]))

(deftest should-parse-some-data-files
  (testing "header"
    (let [parser (p/make-parser p/header)
          v (parser "test-resources/bet_request.bin")]
      (is (map? v))
      (is (= {} v))
      (is (= (into #{} (filter keyword? p/header))
             (into #{} (keys v)))))))


(deftest should-parse-bytes
  (testing "maths"
    (is (= (c/bytes->long (byte-array [1 0 0])) 65536))
    (is (= (c/bytes->long (byte-array [0 255 255])) 65535))))

#_ (defn parser [input-defs]
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
