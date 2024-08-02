(use '[clojure.java.shell :only (sh)])
(require '[clojure.string :as string])

(defn git-ref
  []
  (or (System/getenv "GIT_COMMIT")
      (string/trim (:out (sh "git" "rev-parse" "HEAD")))
      ""))

(defproject org.cyverse/dewey "2.12.0-SNAPSHOT"
  :description "This is a RabbitMQ client responsible for keeping an elasticsearch index
                synchronized with an iRODS repository using messages produced by iRODS."
  :url "https://github.com/cyverse-de/dewey"
  :license {:name "BSD"
            :url "http://iplantcollaborative.org/sites/default/files/iPLANT-LICENSE.txt"}
  :manifest {"Git-Ref" ~(git-ref)}
  :uberjar-name "dewey-standalone.jar"
  :main ^:skip-aot dewey.core
  :dependencies [[org.clojure/clojure "1.11.3"]
                 [org.clojure/tools.cli "1.1.230"]
                 [org.clojure/test.check "1.1.1"]
                 [cheshire "5.13.0"
                   :exclusions [[com.fasterxml.jackson.dataformat/jackson-dataformat-cbor]
                                [com.fasterxml.jackson.dataformat/jackson-dataformat-smile]
                                [com.fasterxml.jackson.core/jackson-annotations]
                                [com.fasterxml.jackson.core/jackson-databind]
                                [com.fasterxml.jackson.core/jackson-core]]]
                 [com.novemberain/langohr "5.4.0" :exclusions [org.slf4j/slf4j-api]]
                 [liberator "0.15.3"]
                 [compojure "1.7.1"]
                 [ring "1.12.2"]
                 [slingshot "0.12.2"]
                 [org.cyverse/clj-jargon "3.1.1"
                   :exclusions [[org.slf4j/slf4j-log4j12]
                                [log4j]]]
                 [org.cyverse/clojure-commons "3.0.9-SNAPSHOT"]
                 [org.cyverse/common-cli "2.8.2"]
                 [org.cyverse/service-logging "2.8.4"]
                 [org.cyverse/event-messages "0.0.1"]
                 [me.raynes/fs "1.4.6"]
                 [cc.qbits/spandex "0.8.2"]
                 [org.apache.httpcomponents/httpcore "4.4.16"]]
  :eastwood {:exclude-namespaces [:test-paths]
             :linters [:wrong-arity :wrong-ns-form :wrong-pre-post :wrong-tag :misplaced-docstrings]}
  :plugins [[jonase/eastwood "1.4.3"]
            [lein-ancient "0.7.0"]
            [test2junit "1.4.4"]]
  :resource-paths []
  :profiles {:dev     {:dependencies   [[midje "1.10.10"]]
                       :jvm-opts       ["-Dotel.javaagent.enabled=false"]
                       :resource-paths ["dev-resources"]}
             :uberjar {:aot :all}}
  :jvm-opts ["-Dlogback.configurationFile=/etc/iplant/de/logging/dewey-logging.xml" "-javaagent:./opentelemetry-javaagent.jar" "-Dotel.resource.attributes=service.name=dewey"])
