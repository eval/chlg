(ns chlog.core)

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

(defn start [& args]
  (if (print-usage? args)
    (print-usage)))

(set! *main-cli-fn* start)
