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

(defn control [work pool-size]
  (let [in (a/chan 1)
        out (a/chan 1)]
    (a/pipeline pool-size out work in)
    [in out]))

(defn start-receiver []
  (while (true? @running)
    (let [packet (DatagramPacket. (byte-array 1024) 1024)
          [in out] (control tx 5)]
      (prn "starting receiver")
      (.receive socket_in packet)
      (a/>!! in (.getData packet))
      (a/go
        (let [{:keys [ip port bytes]} (a/<! out)
              socket (DatagramSocket.)]
          (.send socket (DatagramPacket. bytes (count bytes) ip port))
          (.close socket)
          )
                                        ;)
        ))))

(defn stop-receiver []
  (prn "stopping receiver")
  (reset! @running false)
  (.close socket_in))


