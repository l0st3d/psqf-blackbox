(ns psqf-blackbox.worker
  (:require [psqf-blackbox.parser :as bp]
            [psqf-blackbox.parser.common :as c]
            [clojure.core.async :as a]
            [psqf-blackbox.parser :as p]
            [psqf-blackbox.parser.header :as h]
             [psqf-blackbox.parser.body :as b]))

(defmulti do-work (juxt (comp ::c/int ::h/service-id) (comp ::c/int ::h/message-id)))
(defmethod do-work
  [7900 1]
  [parsed-message]
  ;(prn parsed-message)

  (let [header (select-keys parsed-message (filter keyword? h/structure))]
    (merge header {::b/receipt 1234 ::b/responsecode 1024})))
(defmethod do-work [7900 2] [parsed-message] {})
