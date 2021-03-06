(ns psqf-blackbox.worker
  (:require [psqf-blackbox.parser :as bp]
            [psqf-blackbox.parser.common :as c]
            [clojure.core.async :as a]
            [psqf-blackbox.parser :as p]
            [psqf-blackbox.parser.header :as h]
            [psqf-blackbox.parser.body.place-bet :as b]
            [psqf-blackbox.parser.attrs.bets-placed :as bets]
            [psqf-blackbox.parser.attrs.date :as da]
            [psqf-blackbox.parser.attrs.return-address :as ra]
            )
  (:import (java.util Date)))

(defmulti do-work (juxt (comp ::c/int ::h/service-id) (comp ::c/int ::h/message-id)))
(defmethod do-work
  [7900 1]
  [parsed-message]
  ;(prn parsed-message)

  (let [header (select-keys parsed-message (filter keyword? h/structure))]
    (let [currentDate (Date.)
          {ip-address ::ra/ip-address port ::ra/port} parsed-message]
      (prn ip-address " and " port)
    (merge header {::b/receipt 1234
                   ::b/responsecode 1024
                   ::bets/length 6
                   ::bets/event-code 5678
                   ::bets/number-of-events 1
                   ::bets/schedule-code 56
                   ::da/year (+ 1900 (.getYear currentDate))
                   ::da/month (+ 1 (.getMonth currentDate))
                   ::da/day (.getDate currentDate)
                   ::da/hour (.getHours currentDate)
                   ::da/mins (.getMinutes currentDate)
                   ::da/seconds (.getSeconds currentDate)
                   ::ra/ip-address ip-address
                   ::ra/port port
                   }))))
(defmethod do-work [7900 2] [parsed-message] {})
