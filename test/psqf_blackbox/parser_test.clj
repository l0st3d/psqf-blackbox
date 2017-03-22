(ns psqf-blackbox.parser-test
  (:require [psqf-blackbox.parser :as p]
            [clojure.spec :as s]
            [psqf-blackbox.parser.header :as h]
            [psqf-blackbox.parser.common :as c]
            [psqf-blackbox.parser.response :as resp]
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
    (is (= (c/bytes->long (byte-array [0 255 255])) 65535))
    (is (= (resp/int->bytes 511))) [1 255]))

(deftest should-print-some-data
  (testing "header"
    (let [example {::h/tag-type         {::c/raw [5], ::c/int 5},
                   ::h/transaction-id   {::c/raw [41, -92, 88, -47, 4, -13, 0, 3]
                                         ::c/str "29A458D104F30003"},
                   ::h/attribute-length {::c/raw [0, 84], ::c/int 84},
                   ::h/shop-code        {::c/raw [0, 0, 48, 57], ::c/int 12345},
                   ::h/version          {::c/raw [2], ::c/int 2},
                   ::h/fsc              {::c/raw [0, 80], ::c/int 80}, ::h/spare nil,
                   ::h/service-id       {::c/raw [30, -36], ::c/int 7900},
                   ::h/tag-id           {::c/raw [0, 1], ::c/int 1},
                   ::h/body-length      {::c/raw [0, 21], ::c/int 21},
                   ::h/message-id       {::c/raw [1], ::c/int 1},
                   ::h/licence-code     {::c/raw [0, 0, 48, 57], ::c/int 12345}}
          result (resp/serialise example)]
      (is (map? (first result)))
      (is (c/byte-array? (second result)))
      (is (= example (first result)))
      (is (= (byte-array [2 0 80 0 0 48 57 0 0 48 57 0 1 5 0 21 0 84 41 -92 88 -47 4 -13 0 3 30 -36 1]))))))

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
