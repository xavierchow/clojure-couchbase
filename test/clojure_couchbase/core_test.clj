(ns clojure-couchbase.core-test
  (:require [clojure.test :refer :all]
            [clojure-couchbase.core :refer :all]))

(deftest a-test
  (testing "SUCCESS"
    (is (= 1 1)))
  (testing "SUCCESS 2"
    (is (= "a" "a"))))
