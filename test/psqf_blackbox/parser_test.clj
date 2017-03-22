(ns psqf-blackbox.parser-test
  (:require [psqf-blackbox.parser :as p]
            [clojure.spec :as s]
            [psqf-blackbox.parser.header :as h]
            [psqf-blackbox.parser.common :as c]
            [clojure.test :refer [deftest testing is are use-fixtures]]
            [clojure.java.io :as io]))

(deftest should-parse-some-data-files
  (testing "header"
    (let [v (p/parser "test-resources/bet_request.bin")]
      (is (map? v))
      (is (nil? v))
      #_ (is (= (into #{} (filter keyword? h/structure))
                (into #{} (keys v))))))
  (testing "idempotent"
    (let [v (p/parser "test-resources/bet_request.bin")]
      (is (= v (s/conform (s/spec map?) v))))))

(deftest should-parse-bytes
  (testing "maths"
    (is (= (c/bytes->long (byte-array [1 0 0])) 65536))
    (is (= (c/bytes->long (byte-array [0 255 255])) 65535))))

(deftest should-print-some-data
  (testing "header"
    ))

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
