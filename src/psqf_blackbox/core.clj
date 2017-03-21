(ns psqf-blackbox.core
  (:require [aleph.udp :as udp]
            [byte-streams :as bs]
            [clojure.spec :as s]
            [clojure.core.async :as a]))

(s/def ::testing #{"testing"})
(s/def ::something #{"something"})
(s/def ::silly #{"silly"})

(s/def ::test #{"test"})
(s/def ::some #{"some"})
(s/def ::thing #{"thing"})

(defn parser [input-defs]
  (let [input-spec   (s/* (s/cat :name keyword? :size integer?))
        input-defs   (if (s/valid? input-spec input-defs)
                       (s/conform input-spec input-defs)
                       (throw (ex-info "Illegal Arg"
                                       {:input-defs input-defs
                                        :error      (s/explain-data input-spec input-defs)})))
        fixed-widths (mapcat #(repeat (:size %) (:name %)) input-defs)
        cat-spec     (->> input-defs
                          (map :name)
                          (mapcat #(vector (keyword (name %)) %))
                          (cons `s/cat)
                          eval)]
    (fn [input]
      (let [result (->> (seq input)
                        (map vector fixed-widths)
                        (partition-by first)
                        (map #(apply str (map second %))))]
        (if (s/valid? cat-spec result)
          (s/conform cat-spec result)
          (throw (ex-info "Invalid data" {:input input :errors (s/explain-data cat-spec result)})))))))

(defn -main [& args]
  (udp/socket {:socket-address "localhost"
               :port 12345
               :broadcast? false}))
