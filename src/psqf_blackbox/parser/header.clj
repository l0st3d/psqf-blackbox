(ns psqf-blackbox.parser.header
  (:require [clojure.spec :as s]
            [psqf-blackbox.parser.common :as c]))

(s/def ::version (s/and ::c/uchar #{(String. (byte-array 1 [(int 2)]))}))
(s/def ::fsc ::c/ushort)
(s/def ::licence-code ::c/uint)
(s/def ::tag-id ::c/ushort)
(s/def ::tag-type ::c/uchar)
(s/def ::body-length ::c/ushort)
(s/def ::attribute ::c/ushort)
(s/def ::transaction-id ::c/uchar)
(s/def ::service-id ::c/uchar)
(s/def ::message-id ::c/uchar)
(s/def ::spare (s/and ::c/uchar (s/conformer (constantly nil))))

#_ (comment
     Version uchar (1 byte) Protocol version. Set to 2.
     FSC ushort (2 bytes) Provider code
     Licence code uint (4 bytes) Set to 0.
     Shop code uint (4 bytes) Set to 0.
     Tag ID ushort (2 bytes)
     Tag type uchar (1 byte)
     Body length ushort (2 bytes) Length of message body in bytes
     Attribute length ushort (2 bytes) Length of attributes section in bytes
     Transaction id uchar[8] (8 bytes) Transaction id (to align request w response)
     )
