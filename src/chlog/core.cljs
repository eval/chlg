(ns chlog.core
  (:use [cljs.nodejs :only [require]]))

(defn print-usage []
  (println "
    USAGE:
    chlog <repository>

    EXAMPLES:
    chlog eval/chlog
    # 0.0.1 / 2014-02-9

    Initial release
"))

(defn print-usage? [args]
  (or
    (not (seq args))
    (= "-h" (first args))))

(defn print-changelog [repos]
  (let [ps (require "child_process")
        repos-url (str "https://raw.github.com/" repos "/master/railties/CHANGELOG.md")]
    (.exec ps (str "curl -s " repos-url) #(println %2))))

(defn start [& args]
  (if (print-usage? args)
    (print-usage)
    (print-changelog (first args))))

(set! *main-cli-fn* start)
