(ns psqf-blackbox.worker-test
  (:require [psqf-blackbox.worker :as w]
            [psqf-blackbox.parser :as p]
            [clojure.test :refer [deftest testing is are use-fixtures]]))

(deftest should-return-response-map
  (let [r (w/do-work(p/parser "test-resources/bet_request.bin"))]
    (is (map? r))
    (println r)
    ))

