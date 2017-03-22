(ns psqf-blackbox.parser.response
  (:require [clojure.spec :as s]
            [psqf-blackbox.parser.common :as c]
            [psqf-blackbox.parser.header :as h]
            [psqf-blackbox.parser.attrs.return-address :as ret-addr]))

(def structure (->> 
                (concat h/structure
                        ret-addr/structure)
                (filter keyword?)))

(defn serialise [r]
  [r (byte-array (mapcat ::c/raw (map r structure)))])
