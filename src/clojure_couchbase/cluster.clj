(ns clojure-couchbase.cluster
  (:import
   [com.couchbase.client.java Cluster])
  (:require [clojure.data.json :as json]))

(defn connect
  "connnect the cluster"
  [connection-string username password]
  (Cluster/connect connection-string username password))

(defn disconnect
  "disconnnect the cluster"
  [cluster]
  (.disconnect cluster))

(defn query->map
  "Converts a QueryResult to a map"
  [result]
  {:rows (map #(json/read-str (.toString %) :key-fn keyword) (.rowsAsObject result))
   :status (.status (.metaData result))})

(defn query
  "n1ql query"
  [cluster stmt]
  (query->map (.query cluster stmt)))

(defn create-primary-index
  "create primary index"
  ([cluster bucket]
   (create-primary-index cluster bucket "_default" "_default"))
  ([cluster bucket scope collection]
   (let [r (query cluster "SELECT * FROM system:indexes WHERE name=\"idx_primary\";")]
     (when (= 0 (count (:rows r)))
       (query cluster (format "CREATE PRIMARY INDEX idx_primary ON `%s`.%s.%s;" bucket scope collection))))))

