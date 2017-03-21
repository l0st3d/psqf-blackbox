(ns psqf-blackbox.receiver
  (:import [java.net DatagramSocket DatagramPacket InetAddress])
  (:require [psqf-blackbox.parser :as p]))

(def socket_in (DatagramSocket. 5200))

(def running (atom true))
(def buffer (make-array Byte/TYPE 1024))
(defn parse [packet]
  {:ip    (InetAddress/getByName "127.0.0.1")
   :port  5201
   :bytes (.getBytes (String. (.getData packet) 0 (.getLength packet)))})

(defn start-receiver []
  (while (true? @running)
    (let [packet (DatagramPacket. buffer 1024)]
      (println "running here")
      (.receive socket_in packet)
      (let [{:keys [ip port bytes]} (parse packet)]
        (.send (DatagramSocket.) (DatagramPacket. bytes (count bytes) ip port))
        )
      )
    ))