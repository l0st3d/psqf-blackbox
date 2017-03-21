(ns psqf-blackbox.parser-test
  (:require [psqf-blackbox.parser :as p]
            [psqf-blackbox.parser.header :as h]
            [clojure.test :refer [deftest testing is are use-fixtures]]
            [clojure.java.io :as io]))

(deftest should-parse-some-data-files
  (testing "header"
    (let [parser (p/make-parser p/header)]
      (is (= nil (parser "test-resources/bet_request.bin"))))))
