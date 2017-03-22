(ns psqf-blackbox.parser.attrs.bets-placed
  (:require [clojure.spec :as s]
            [clojure.string :as st]
            [psqf-blackbox.parser.common :as c]))

(s/def ::length ::c/uint)
(s/def ::number-of-events ::c/uint)
(s/def ::schedule-code ::c/uint)
(s/def ::event-code ::c/uint)

;Variable (uchar + ushort + multiple (ushort + ushort))
;Length (1 byte)
;Number of events (2 byte)
;For each event included:
;Schedule code (2 byte)
;Event code (2 byte)

;TODO deal with more than one event!
(def structure [::length 1
                ::number-of-events 2
                ::schedule-code 2
                ::event-code 2])

(defmethod c/parse-attrs 1152 [ios & rest]
  (c/parse-structure ios structure))
