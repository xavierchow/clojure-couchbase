(ns clojure-couchbase.cluster-test
  (:require  [clojure.test :refer [deftest testing is use-fixtures] :as t]
             [clojure-couchbase.fixtures :as fx]
             [clojure-couchbase.cluster :as c]
             [clojure-couchbase.collection :as col])
  (:import [com.couchbase.client.java.query QueryStatus]))

(use-fixtures :once fx/init-collection)

(def pet {:name "smith"
          :age 2
          :gene "Persia"
          :owner {:name "kelly" :gender 35}})

(deftest query
  (c/create-primary-index (fx/get-cluster) fx/test-bucket)
  (testing "not found"
    (is (= 0 (count (:rows (c/query (fx/get-cluster) (format "SELECT meta().id FROM `%s` where name = \"not_found\"" fx/test-bucket)))))))

  (testing "select with where"
    (dorun
     (map #(col/upsert
            (fx/get-collection)
            (str (:name pet) "-" %)
            (assoc pet :name (str (:name pet) "-" %)))
          (range 10)))
    (let [result
          (c/query (fx/get-cluster) (format "SELECT b.* FROM `%s` b where name = \"smith-7\"" fx/test-bucket))]
      (is (= QueryStatus/SUCCESS (:status result)))
      (is (= 1 (count (:rows result))))
      (is (= "smith-7" (:name (first (:rows result))))))))
