(ns chlg.core
  (:require [cljs.core.async :as async :refer [<! >! chan close! put! take! timeout]]
            [cljs.nodejs :as node]
            [clojure.string :as string])
  (:require-macros [cljs.core.async.macros :as m :refer [go alt!]]))

(defn print-usage []
  (println "
    USAGE:
    chlg <repository>

    EXAMPLES:
    chlg eval/chlg
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
    (.exec ps (str "curl -s " curl_headers " '" url "'") #(put! c %2))
  c))

(defn get-tree [repos]
  (let [path (str "repos/" repos "/git/trees/master")]
    (gh-request path)))

(defn get-search [q]
  (let [path (str "search/repositories?per_page=1&q=" q)]
    (gh-request path)))

(defn get-content [repos file]
  (let [path (str "repos/" repos "/contents/" file)]
    (gh-request path :raw)))

(defn repos-files [response]
  (let [json (JSON/parse response)]
    (map #(aget % "path") (aget json "tree"))))

(defn filter-repos-full-name [search-response]
  (let [json (JSON/parse search-response)]
    (aget (first (aget json "items")) "full_name")))

(defn changelog-path [files]
  (let [changelog-re #"(?i)changelog|history"]
    (first (filter #(re-find changelog-re %) files))))

(defn print-changelog [repos]
  (go
    (let [repos-root-files (get-tree repos)
          path (changelog-path (repos-files (<! repos-root-files)))
          changelog-content (get-content repos path)]
      (println (if path
                  (str "Showing '" path "' of repository '" repos "':\n\n"
                       (<! changelog-content))
                  (str "No changelog found for '" repos "'"))))))

(defn search-full-repos-name [q]
  (go
    (let [request (get-search q)]
      (filter-repos-full-name (<! request)))))

(defn start [& args]
  (if (print-usage? args)
    (print-usage)
    (go
      (let [[repos] args
            full-name (if (re-find #"/" repos) repos (<! (search-full-repos-name repos)))]
        (print-changelog full-name)))))

(set! *main-cli-fn* start)
