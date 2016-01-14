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
           :user "usernameplaceholder"
           :password "passwordplaceholder"}))


(defn sqlquery [name empfaenger]
  
;(first (jdbc/query mysql-db [(str "select * from konten where Name='" name "'")])))
(jdbc/execute! mysql-db ["UPDATE konten SET Kontostand = Kontostand - 1 WHERE Kontostand > 0 AND Name = ?" name])
(jdbc/execute! mysql-db ["UPDATE konten SET Kontostand = Kontostand + 1 WHERE NurZahlen = 0 AND Name = ?" empfaenger])) 
;(jdbc/update! mysql-db :konten {:Kontostand 99} ["Name = ?" name]))
; maybe this sets the guthaben to 99

(defn page [name]
  (str "<html><body>"
       (if name
  (str "Nice to meet you, " name "! \n" "<div id='mgm_response' data='"(sqlquery [name empfaenger])"'></div>")
        (str "<form>"
              "Name: <input name='name' type='text'>"
              "<input type='submit'>"
              "</form>"))
       "</body></html>")
)


(defn handler [{{name "name", empfaenger "empfaenger"} :params}]
  (-> (r/response (page [name empfaenger]))
      (r/content-type "text/html")))

(def app
  (-> handler p/wrap-params))

(defn -main
 "Kommentar."
 [& args]
 (j/run-jetty app {:port 8080}))

