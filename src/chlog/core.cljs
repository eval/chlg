(ns chlog.core
  (:require [cljs.core.async :as async :refer [<! >! chan close! put! get! timeout]]
            [cljs.nodejs :as node])
  (:require-macros [cljs.core.async.macros :as m :refer [go alt!]]))

(defn print-usage []
  (println "
    USAGE:
    chlog <repository>

    EXAMPLES:
    chlog eval/chlog
    # 0.0.1 / 2014-02-9

    Initial release
")
)

(defn print-usage? [args]
  (or
    (not (seq args))
    (= "-h" (first args))))

(def gh-token
  node/process.env.GH_TOKEN)

(defn gh-request [path]
  (let [ps (node/require "child_process")
        url (str "https://api.github.com/" path "?access_token=" gh_token)
        c (chan)]
    (.exec ps (str "curl -s " url) #(put! c (JSON/parse %2)))
  c))

(defn get-tree [repos]
  (let [path (str "repos/" repos "/git/trees/master")]
    (gh-request path)))

(defn repos-files [json]
  (map #(aget % "path") (aget json "tree")))

(defn changelog-path [repos])

(defn print-changelog [repos]
  ;(println (repos-files (gh-request))))
  (println (gh-request)))

(defn print-root-files [repos]
  (let [repos-root-files (get-tree repos)]
    (go
      (println (repos-files (<! repos-root-files))))))

(defn start [& args]
  (if (print-usage? args)
    (print-usage)
    (print-root-files (first args))))

(set! *main-cli-fn* start)
