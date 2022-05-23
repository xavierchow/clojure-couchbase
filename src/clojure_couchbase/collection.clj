(ns clojure-couchbase.collection
  (:refer-clojure :exclude [get replace remove])
  (:import [com.couchbase.client.java Collection]
           [com.couchbase.client.java Cluster]
           [com.couchbase.client.java.json JsonObject])
  (:require [clojure.data.json :as json]))

;; TODO move to util
(defn mutation-result->map
  [mutation-result]
  {:cas (.cas mutation-result)
   :mutation-token (.mutationToken mutation-result)})
(defn doc->map
  "Returns the content of a JsonDocument as a map"
  [get-result]
  {:cas (.cas get-result)
   :expiry-time (.expiryTime get-result)
   :content  (-> (.contentAsObject get-result) .toString (json/read-str  :key-fn keyword))})

(defn get-collection
  "get the colleciton"
  ([cluster bucket-name]
   (get-collection cluster bucket-name "_default" "_default"))
  ([cluster bucket-name scope collection]
   (-> (.bucket cluster bucket-name) (.scope scope) (.collection collection))))

(defn get
  "get document"
  [col id] (doc->map (.get col id)))

(defn upsert
  "upsert document"
  [col id json-map]
  (let [json-object (JsonObject/fromJson (json/write-str json-map))]
    (mutation-result->map (.upsert col id json-object))))

(defn replace
  "replace document"
  [col id json-map]
  (let [json-object (JsonObject/fromJson (json/write-str json-map))]
    (mutation-result->map (.replace col id json-object))))
(defn remove
  "remove a document"
  [col id] (mutation-result->map (.remove col id)))

