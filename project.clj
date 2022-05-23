(defproject clojure-couchbase "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [com.couchbase.client/java-client "3.3.0"]
                 [org.clojure/data.json "2.4.0"]
                 [org.clojure/test.check "1.1.0"] ;; FIXME
                 ]
  :repl-options {:init-ns clojure-couchbase.core})
