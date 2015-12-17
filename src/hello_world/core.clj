;; When executed, this file will run a basic web server
;; on http://localhost:8080.
(ns hello-world.core
  (:require [ring.middleware.params :as p]
        [ring.util.response :as r]
        [ring.adapter.jetty :as j]
            [clojure.java.jdbc :as jdbc])
  (:gen-class))

(let [db-host "localhost"
      db-port 3306
      db-name "mgm_schema"]

  (def mysql-db {:classname "com.mysql.jdbc.Driver" ; must be in classpath
           :subprotocol "mysql"
           :subname (str "//" db-host ":" db-port "/" db-name)
           ; Any additional keys are passed to the driver
           ; as driver-specific properties.
           :user ""
           :password ""}))


(defn sqlquery [name]
  
(first (jdbc/query mysql-db [(str "select * from konten where Name='" name "'")])))

(defn page [name]
  (str "<html><body>"
       (if name
  (str "Nice to meet you, " name "! \n" (sqlquery name) )
         (str "<form>"
              "Name: <input name='name' type='text'>"
              "<input type='submit'>"
              "</form>"))
       "</body></html>"))

(defn handler [{{name "name"} :params}]
  (-> (r/response (page name))
      (r/content-type "text/html")))

;(def sqlquery
;    (j/query mysql-db ["select * from konten"]))

(def app
  (-> handler p/wrap-params))

 
;           {:Kontonummer 11 :Kontostand 31}
;           {:Kontonummer 12 :Kontostand 11})


(defn -main
 "Kommentar."
 [& args]
 (j/run-jetty app {:port 8080}))
;(def server (run-jetty app {:port 8080 :join? false}))

