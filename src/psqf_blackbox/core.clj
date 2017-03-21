(ns psqf-blackbox.core
  (:require [aleph.udp :as udp]
            [byte-streams :as bs]
            [manifold.stream :as ms]
            [clojure.spec :as s]
            [clojure.core.async :as a]
            [clojure.tools.logging :as log]
            [psqf-blackbox.parser :as p]))

(def socket-server (atom nil))

(defn create-server
  [host port]
  (let [c (a/chan 10)
        s (udp/socket {:port port
             :address host
             :broadcast? false})]
    (ms/connect s c)
  {:server s
   :chan c}))

(defn -main [& args]
  (let [{:keys [chan]} (swap! socket-server #(or % (create-server "localhost" 12345)))]
    (log/info "statsd udp server: started")
    chan))

(defn stop
  []
  (log/info "statsd udp server: stopping")
  (log/info "statsd udp server stopped"))