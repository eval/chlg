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

(defn gh-request []
  (let [ps (require "child_process")
        url "https://api.github.com/repos/bundler/bundler/git/trees/master"]
    (.exec ps (str "curl -s " url) #(println (repos-files (JSON/parse %2))))))

(def gh-files-json
  (JSON/parse "{\"tree\": [{\"path\":\"spec\"},{\"path\":\"lib\"}]}"))

(defn repos-files [json]
  (map #(aget % "path") (aget json "tree")))
  ;(.. (JSON/parse gh-files-json) -tree))
  ;(map #(.-path %) (.-tree gh-files-json)))

(defn changelog-path [repos])

(defn print-changelog [repos]
  ;(println (repos-files (gh-request))))
  (println (gh-request)))

(defn print-changelog2 [repos]
  (let [ps (require "child_process")
        repos-url (str "https://raw.github.com/" repos "/master/railties/CHANGELOG.md")]
    (.exec ps (str "curl -s " repos-url) #(println %2))))

(defn start [& args]
  (if (print-usage? args)
    (print-usage)
    (print-changelog (first args))))

(set! *main-cli-fn* start)
