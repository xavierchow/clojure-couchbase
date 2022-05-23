(ns clojure-couchbase.collection-test
  (:require  [clojure.test :refer [deftest testing is use-fixtures] :as t]
             [clojure-couchbase.collection :as col]
             [clojure-couchbase.fixtures :as fx])
  (:import [java.util UUID]
           [com.couchbase.client.core.error DocumentNotFoundException]))

(use-fixtures :once fx/init-collection)
(defn uuid
  []
  (.toString (UUID/randomUUID)))

(deftest curd
  (testing "upsert"
    (let [c (fx/get-collection)
          id (uuid)
          res (col/upsert c id  {:a 1 :b 2})]
      (is (not= nil (:cas res)))
      (is (not= nil (:mutation-token res)))))

  (testing "get"
    (let [c (fx/get-collection)
          id (uuid)
          _ (col/upsert c id {:a 1 :b 2})
          doc (col/get c id)]
      (is (= {:a 1 :b 2} (:content doc)))))

  (testing "replace"
    (testing "should update the doc"
      (let [c (fx/get-collection)
            id (uuid)
            _ (col/upsert c id {:a 1 :b 2})
            _ (col/replace c id {:a 3 :b 4})
            doc (col/get c id)]
        (is (= {:a 3 :b 4} (:content doc)))))
    (testing "should throw"
      (is (thrown? DocumentNotFoundException (col/replace (fx/get-collection) (uuid) {:a 3 :b 4})))))

  (testing "remove"
    (let [c (fx/get-collection)
          id (uuid)
          _ (col/upsert c id {:a 1 :b 2})
          res (col/remove c id)]
      (is (not= nil (:cas res)))
      (is (thrown? DocumentNotFoundException (col/get c id))))))
