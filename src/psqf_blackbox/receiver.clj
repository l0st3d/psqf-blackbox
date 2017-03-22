(ns psqf-blackbox.receiver
  (:import [java.net DatagramSocket DatagramPacket InetAddress])
  (:require [psqf-blackbox.parser :as bp]
            [clojure.core.async :as a]
            [psqf-blackbox.parser :as p]
            [psqf-blackbox.worker :as w]
            [psqf-blackbox.parser.response :as pr]
            [psqf-blackbox.parser.attrs.return-address :as r]
            [psqf-blackbox.parser.common :as c]))

(defonce socket_in (DatagramSocket. 5200))
(defonce running (atom true))

(def tx (comp (map p/parser)
              (map w/do-work)
              (map pr/serialise)))

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
        (let [[{{ip-address ::c/str} ::r/ip-address
                {port ::c/int}       ::r/port
                :as                  q} bytes] (a/<! out)
              socket (DatagramSocket.)]
          (.send socket (DatagramPacket. bytes (count bytes) (InetAddress/getByName ip-address) port))
          (.close socket)
          )
        ))))

(defn stop-receiver []
  (prn "stopping receiver")
  (reset! @running false)
  (.close socket_in))


