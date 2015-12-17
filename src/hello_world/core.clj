;; When executed, this file will run a basic web server
;; on http://localhost:8080.

(ns ring.example.params
  (:use ring.middleware.params
        ring.util.response
        ring.adapter.jetty))

(defn page [name]
  (str "<html><body>"
       (if name
         (str "Nice to meet you, " name "!")
         (str "<form>"
              "Name: <input name='name' type='text'>"
              "<input type='submit'>"
              "</form>"))
       "</body></html>"))

(defn handler [{{name "name"} :params}]
  (-> (response (page name))
      (content-type "text/html")))

;(def sqlquery
;    (j/query mysql-db ["select * from konten"]))

(def app
  (-> handler wrap-params))

(require '[clojure.java.jdbc :as j])
 
(let [db-host "localhost"
      db-port 3306
      db-name "mgm_schema"]
 
  (def mysql-db {:classname "com.mysql.jdbc.Driver" ; must be in classpath
           :subprotocol "mysql"
           :subname (str "//" db-host ":" db-port "/" db-name)
           ; Any additional keys are passed to the driver
           ; as driver-specific properties.
           :user "johorst"
           :password "666jojojo"}))
;geht:
(j/insert! mysql-db :konten
           {:Kontonummer 11 :Kontostand 31}
           {:Kontonummer 12 :Kontostand 11})


(run-jetty app {:port 8080})
;(def server (run-jetty app {:port 8080 :join? false}))

