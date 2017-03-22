(ns psqf-blackbox.parser.response
  (:require [clojure.spec :as s]
            [psqf-blackbox.parser.common :as c]
            [psqf-blackbox.parser.header :as h]
            [psqf-blackbox.parser.attrs.return-address :as ret-addr]
            [psqf-blackbox.parser.body.place-bet :as place-bet]))

(def structure (->> 
                (concat h/structure
                        ret-addr/structure
                        place-bet/structure)
                (filter keyword?)))

(def header-structure (->> structure
                           (filter #(-> % namespace (.startsWith "psqf-blackbox.parser.header")))))

(def body-structure (->> structure
                         (filter #(-> % namespace (.startsWith "psqf-blackbox.parser.body")))))

(def attrs-structure (->> structure
                          (filter #(-> % namespace (.startsWith "psqf-blackbox.parser.attrs")))))

(defn int->bytes [i]
  (loop [acc ()
         i i]
    (if (-> i (> 0))
      (recur (cons (mod i 256) acc) (int (/ i 256)))
      acc)))

(defn- get-raw [v]
  (cond (::c/raw v)  (::c/raw v)
        (integer? v) (int->bytes v)
        (string? v)  (.getBytes v)
        :else        (throw (ex-info "Cannot get raw value" {:data v}))))

(defn serialise [r]
  (let [body   (mapcat get-raw (map r body-structure))
        attrs  (mapcat get-raw (map r attrs-structure))
        header (mapcat get-raw (map (assoc r ::h/body-length (count body) ::h/attribute-length (count attrs)) header-structure))]
    [r (byte-array (concat header body attrs))]))
