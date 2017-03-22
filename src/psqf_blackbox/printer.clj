(ns psqf-blackbox.printer
  (:require [psqf-blackbox.parser :as bp]
            [clojure.core.async :as a]
            [psqf-blackbox.parser :as p])
  (:import (java.net InetAddress)))

(def ->response
  {:ip    (InetAddress/getByName "127.0.0.1")
   :port  5201
   :bytes (.getBytes "hello")})