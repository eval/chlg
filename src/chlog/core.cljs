(ns chlog.core)

(defn start [& args]
  (println "Hello Chlog"))

(set! *main-cli-fn* start)
