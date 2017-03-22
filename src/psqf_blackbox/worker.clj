(ns psqf-blackbox.worker
  (:require [psqf-blackbox.parser :as bp]
            [clojure.core.async :as a]
            [psqf-blackbox.parser :as p]))

(defn do-work [input-defs]
  (.getBytes "hello"))