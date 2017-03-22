(ns psqf-blackbox.receiver
  (:import [java.net DatagramSocket DatagramPacket InetAddress])
  (:require [psqf-blackbox.parser :as bp]
            [clojure.core.async :as a]
            [psqf-blackbox.parser :as p]
            [psqf-blackbox.worker :as w]
            [psqf-blackbox.printer :as pr]
            ))

(defonce socket_in (DatagramSocket. 5200))

(defonce running (atom true))
(def buffer (make-array Byte/TYPE 1024))

(def tx (comp (map p/parser)
              (map w/do-work)
              (map pr/->response)))

(defn control [tx]
  (let [in (a/chan 10)
        out (a/chan 10)]
    (a/pipeline 4 in tx out)
    [in out]))

(defn start-receiver []
  (while (true? @running)
    (let [packet (DatagramPacket. buffer 1024)
          [in out] (control tx)]
      (prn "starting receiver")
      (.receive socket_in packet)
      (a/>!! in (.getData packet))
      (a/go
        (let [{:keys [ip port bytes]} (a/<! out)]
          (.send (DatagramSocket.) (DatagramPacket. bytes (count bytes) ip port))
          )
        ;)
        ))))

(defn stop-receiver []
  (prn "stopping receiver")
  (reset! @running false)
  (.close socket_in))


