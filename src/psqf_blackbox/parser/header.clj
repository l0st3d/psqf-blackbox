(ns psqf-blackbox.parser.header
  (:require [clojure.spec :as s]
            [psqf-blackbox.parser.common :as c]))

(s/def ::version (s/and ::uchar #{"2"}))
(s/def ::fsc (s/and ::ushort (c/length-is 2)))
(s/def ::licence-code (s/and ::uint #{"0000"}))
(s/def ::tag-id (s/and ::ushort (c/length-is 2)))
(s/def ::tag-type (s/and ::uchar (c/length-is 1)))
(s/def ::body-length (s/and ::ushort (c/length-is 2)))
(s/def ::attribute (s/and ::ushort (c/length-is 2)))
(s/def ::transaction-id (s/and ::uchar (c/length-is 8)))

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
