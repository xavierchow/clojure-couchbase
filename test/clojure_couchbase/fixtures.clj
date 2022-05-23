(ns clojure-couchbase.fixtures
  (:require [clojure-couchbase.cluster :as clus]
            [clojure-couchbase.collection :as col]))

(def username "Administrator")
(def password "password")
(def test-bucket "clojure_cb_test")

(def ^:dynamic *col* nil)
(def ^:dynamic *clus* nil)

(defn init-collection
  [f]
  (let [cluster (clus/connect "couchbase://localhost" username password)
        collection (col/get-collection cluster test-bucket)]
    (try
      (binding [*col* collection *clus* cluster]
        (f))
      (finally (clus/disconnect cluster)))))

(defn get-collection
  ""
  [] *col*)
(defn get-cluster
  ""
  [] *clus*)

