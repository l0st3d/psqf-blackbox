(ns psqf-blackbox.receiver
  (:import [java.net DatagramSocket DatagramPacket InetAddress])
  (:require [psqf-blackbox.parser :as bp]
            [psqf-blackbox.controller :as bc]
            [clojure.core.async :as a]
            [psqf-blackbox.parser :as p]))

(def socket_in (DatagramSocket. 5200))

(def running (atom true))
(def buffer (make-array Byte/TYPE 1024))

(defn control []
  (let [in (a/chan 10)
        out (a/chan 10)]
    (a/go (while true
            (let [input (a/<! in)
                  output (p/parser input)]
              (prn output)
              (a/>! out output)
              )))
    [in out]))

(defn start-receiver []
  (while (true? @running)
    (let [packet (DatagramPacket. buffer 1024)
          [in out] (control)]
      (prn "starting receiver")
      (.receive socket_in packet)
      (a/>!! in (.getData packet))
      (a/go
        (let [{:keys [ip port bytes]} (a/<! out)]
          (.send (DatagramSocket.) (DatagramPacket. bytes (count bytes) (InetAddress/getByName "127.0.0.1") 5201))
          )
        ;)
        ))))

(defn stop-receiver []
  (prn "stopping receiver")
  (reset! @running false)
  (.close socket_in))


