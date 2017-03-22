(ns psqf-blackbox.parser.response
  (:require [clojure.spec :as s]
            [psqf-blackbox.parser.common :as c]
            [psqf-blackbox.parser.header :as h]
            [psqf-blackbox.parser.attrs.return-address :as ret-addr]))

(def structure (->> 
                (concat h/structure
                        ret-addr/structure)
                (filter keyword?)))

(def header-structure (->> structure
                           (filter #(-> % namespace (.startsWith "psqf-blackbox.parser.header")))))

(def body-structure (->> structure
                         (filter #(-> % namespace (.startsWith "psqf-blackbox.parser.body")))))

(def attrs-structure (->> structure
                          (filter #(-> % namespace (.startsWith "psqf-blackbox.parser.attrs")))))

(defn serialise [r]
  (let [body   (mapcat ::c/raw (map r body-structure))
        attrs  (mapcat ::c/raw (map r attrs-structure))
        header (mapcat ::c/raw (map (assoc r ::h/body-length (count body) ::h/attribute-length (count attrs)) header-structure))]
    [r (byte-array (concat header body attrs))]))
