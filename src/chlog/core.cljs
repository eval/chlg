(ns chlog.core
  (:require [cljs.core.async :as async :refer [<! >! chan close! put! get! timeout]]
            [cljs.nodejs :as node]
            [clojure.string :as string])
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

(defn gh-request [path & [format]]
  (let [ps (node/require "child_process")
        format (or format :json)
        headers {:accept (str "application/vnd.github.3." (name format))}
        auth_header (if gh-token {:authorization (str "token " gh-token)} {})
        curl_headers (string/join " " (map (fn [[k v]] (str "-H '" (name k) ": " v "'")) (merge headers auth_header)))
        url (str "https://api.github.com/" path)
        c (chan)]
    (.exec ps (str "curl -s " curl_headers " " url) #(put! c %2))
  c))

(defn get-tree [repos]
  (let [path (str "repos/" repos "/git/trees/master")]
    (gh-request path)))

(defn get-content [repos file]
  (let [path (str "repos/" repos "/contents/" file)]
    (gh-request path :raw)))

(defn repos-files [response]
  (let [json (JSON/parse response)]
    (map #(aget % "path") (aget json "tree"))))

(defn changelog-path [files]
  (let [changelog-re #"(?i)changelog|history"]
    (first (filter #(re-find changelog-re %) files))))

(defn print-changelog [repos]
  (go
    (let [repos-root-files (get-tree repos)
          path (changelog-path (repos-files (<! repos-root-files)))
          changelog-content (get-content repos path)]
      (println (<! changelog-content)))))

(defn start [& args]
  (if (print-usage? args)
    (print-usage)
    (let [[repos] args]
      (print-changelog repos))))

(set! *main-cli-fn* start)
