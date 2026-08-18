(require '[clojure.java.shell :refer (sh)])
(require '[clojure.string :as string])

(defn git-ref
  []
  (or (System/getenv "GIT_COMMIT")
      (string/trim (:out (sh "git" "rev-parse" "HEAD")))
      ""))

(defproject org.cyverse/dewey "3.0.1-SNAPSHOT"
  :description "This is a RabbitMQ client responsible for keeping an elasticsearch index
                synchronized with an iRODS repository using messages produced by iRODS."
  :url "https://github.com/cyverse-de/dewey"
  :license {:name "BSD"
            :url "http://iplantcollaborative.org/sites/default/files/iPLANT-LICENSE.txt"}
  :manifest {"Git-Ref" ~(git-ref)}
  :uberjar-name "dewey-standalone.jar"
  :main ^:skip-aot dewey.core
  ;; Fail the build on a new dependency conflict rather than printing a
  ;; warning nobody reads.
  :pedantic? :abort
  ;; Records versions Leiningen already resolves, read off the resolved
  ;; classpath rather than copied from lein's "Consider using these
  ;; :managed-dependencies" hint -- that hint names the version that LOST the
  ;; conflict, so pasting it would be a silent upgrade.
  :managed-dependencies [[clj-http "3.13.0"]
                         [commons-codec "1.16.1"]
                         [commons-io "2.16.1"]
                         [dev.weavejester/medley "1.9.0"]
                         [hiccup "1.0.5"]
                         [org.apache.commons/commons-compress "1.8"]
                         [org.apache.commons/commons-fileupload2-core "2.0.0-M4"]
                         [org.apache.httpcomponents/httpcore-nio "4.4.13"]
                         [org.clojure/core.cache "0.7.1"]
                         [org.clojure/core.memoize "0.7.1"]
                         [org.clojure/data.priority-map "0.0.7"]
                         [org.clojure/java.classpath "1.0.0"]
                         [org.clojure/tools.namespace "1.4.4"]
                         [org.clojure/tools.reader "1.3.6"]
                         [org.ring-clojure/ring-core-protocols "1.15.1"]
                         [org.ring-clojure/ring-websocket-protocols "1.15.1"]
                         [prismatic/schema "1.1.12"]
                         [ring/ring-core "1.15.1"]]
  :dependencies [[org.clojure/clojure "1.12.5"]
                 [org.clojure/tools.cli "1.4.256"]
                 [org.clojure/test.check "1.1.3"]
                 [cheshire "6.2.0"]
                 [com.fasterxml.jackson.core/jackson-core "2.21.1"]
                 [com.fasterxml.jackson.core/jackson-databind "2.21.1"]
                 [com.fasterxml.jackson.dataformat/jackson-dataformat-cbor "2.21.1"]
                 [com.fasterxml.jackson.dataformat/jackson-dataformat-smile "2.21.1"]
                 [com.novemberain/langohr "5.6.0" :exclusions [org.slf4j/slf4j-api]]
                 [liberator "0.15.3"]
                 [compojure "1.7.2"]
                 [ring "1.15.5"]
                 [slingshot "0.12.2"]
                 [org.cyverse/clj-jargon "3.1.6"
                   :exclusions [[org.slf4j/slf4j-log4j12]
                                [log4j]]]
                 [org.cyverse/clojure-commons "3.0.13"]
                 [org.cyverse/common-cli "2.8.3"]
                 [org.cyverse/service-logging "2.8.6"]
                 [org.cyverse/event-messages "0.0.1"]
                 [me.raynes/fs "1.4.6"]
                 [cc.qbits/spandex "0.8.2"]
                 [org.apache.httpcomponents/httpcore "4.4.16"]]
  :eastwood {:exclude-namespaces [:test-paths]
             :linters [:wrong-arity :wrong-ns-form :wrong-pre-post :wrong-tag :misplaced-docstrings]}
  :plugins [[jonase/eastwood "1.4.3"]
            [lein-ancient "1.0.0"]
            [test2junit "1.4.4"]]
  :resource-paths []
  :profiles {:dev     {:dependencies   [[midje "1.10.10"]]
                       :resource-paths ["dev-resources"]}
             :uberjar {:aot :all}}
  :jvm-opts ["-Dlogback.configurationFile=/etc/iplant/de/logging/dewey-logging.xml"])
