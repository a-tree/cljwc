(ns cljwc.core
  (:require [clojure.tools.cli :as cli]
            [clojure.java.io :as io]
            [clojure.string :as str])
  (:gen-class))

(def cljwc-options
  [["-h" "--help" "Show Help" ]
   ["-i" "--input inputfile" "Input"]
   ["-c" "--bytes" "print the byte counts"]
   ["-m" "--chars" "Count characters"]
   ["-l" "--line" "Count lines"]
   ;; additional options
   ])

(def usage
  (str
   "Usage: "
   "cljwc [option] ...\n"
   "\n"
   "-i, --input  <inputfile>  :default stdin\n"
   "-c, --bytes               count bytes\n"
   "-m, --chars               count characters\n"
   "-l, --line                count lines\n"
   "-h, --help                display this help and exit.\n"))

(defn bufferd-redear [s]
  (if-let [s (nil? s)]
    (io/reader System/in)
    (io/reader s)))

(defn count-anything [s buffer-spliter]
  (-> s
      slurp
      buffer-spliter
      count))

(defn linecount [in]
  (count-anything (bufferd-redear in) str/split-lines))

(defn split-words [s]
  (remove str/blank? (str/split s #"\s")))

(defn wordcount [in]
  (count-anything (bufferd-redear in) split-words))

(defn charcount [in]
  (count-anything (bufferd-redear in) #(str/split % #"")))

(defn bytescount [in]
  (count-anything (bufferd-redear in) #(.getBytes (str/join %))))

(defn -main
  [& args]
  "cljwc main"
  (let [{:keys [options   ;; The options map, keyed by :id, mapped to the parsed value
                arguments ;; A vector of unprocessed arguments
                summary   ;; A string containing a minimal options summary
                errors]}  ;; A possible vector of error message strings generated during parsing; nil when no errors exist
        (cli/parse-opts args cljwc-options)]
    (when (some? errors)
      (println "error")
      (println usage)
      (System/exit 0))
    (cond
      (:help options)  (println usage)
      (:line options)  (println (linecount (:input options)))
      (:chars options) (println (charcount (:input options)))
      (:bytes options) (println (bytescount (:input options)))
      :else            (println (wordcount (:input options))))))
