(ns psqf-blackbox.parser.header
  (:require [clojure.spec :as s]
            [psqf-blackbox.parser.common :as c]))

(s/def ::version (s/and ::c/uint (comp #{2} ::c/int)))
(s/def ::fsc ::c/uint)
(s/def ::licence-code ::c/uint)
(s/def ::shop-code ::c/uint)
(s/def ::tag-id ::c/uint)
(s/def ::tag-type ::c/uint)
(s/def ::body-length ::c/uint)
(s/def ::attribute-length ::c/uint)
(s/def ::transaction-id (s/and ::c/uchar (s/conformer (c/include ::c/str c/bytes->hex-str))))
(s/def ::service-id ::c/uint)
(s/def ::message-id ::c/uint)
(s/def ::spare (s/and ::c/uchar (s/conformer (constantly nil))))

(def structure [::version          1
                ::fsc              2
                ::licence-code     4
                ::shop-code        4
                ::tag-id           2
                ::tag-type         1
                ::body-length      2
                ::attribute-length 2
                ::transaction-id   8
                ::service-id       2
                ::message-id       1
                ::spare            3])
