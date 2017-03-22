(ns psqf-blackbox.parser.body.place-bet
  (:require [clojure.spec :as s]
            [psqf-blackbox.parser.common :as c]))

(s/def ::responsecode ::c/uchar)
(s/def ::receipt ::c/uchar)

(def structure [::responsecode 2
                ::receipt 10])
