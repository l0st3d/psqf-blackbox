(ns psqf-blackbox.parser
  (:require [clojure.spec :as s]
            [psqf-blackbox.parser.header :as h]))

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
          (throw (ex-info "Invalid data" {:input input
                                          :errors (s/explain-data cat-spec result)})))))))
