(ns cljwc.test-core
  (:require [cljwc.core :as core]
            [clojure.tools.cli :as cli]
            [clojure.test :refer :all]))

(deftest test-cljwc-core
  (testing "test the cljwc"
    (is (= 42 (count "Answer to Life the Universe and Everything")))))

