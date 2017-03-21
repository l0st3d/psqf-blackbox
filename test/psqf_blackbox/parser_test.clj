(ns psqf-blackbox.parser-test
  (:require [psqf-blackbox.parser :as p]
            [psqf-blackbox.parser.header :as h]
            [psqf-blackbox.parser.common :as c]
            [clojure.test :refer [deftest testing is are use-fixtures]]
            [clojure.java.io :as io]))

(deftest should-parse-some-data-files
  (testing "header"
    (let [parser (p/make-parser p/header)]
      (is (map? (parser "test-resources/bet_request.bin"))))))


(deftest should-parse-bytes
  (testing "maths"
    (is (= (c/bytes->long (byte-array [1 0 0])) 65536))
    (is (= (c/bytes->long (byte-array [0 255 255])) 65535))))
