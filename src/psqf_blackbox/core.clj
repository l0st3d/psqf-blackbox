(ns psqf-blackbox.core
  (:require [aleph.udp :as udp]
            [byte-streams :as bs]
            [clojure.spec :as s]
            [clojure.core.async :as a]))

(defn -main [& args]
  (udp/socket {:socket-address "localhost"
               :port 12345
               :broadcast? false}))
