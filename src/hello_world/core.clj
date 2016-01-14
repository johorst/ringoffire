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


(defn sqlquery [name]
  
;(first (jdbc/query mysql-db [(str "select * from konten where Name='" name "'")])))
(jdbc/execute! mysql-db ["UPDATE konten SET Kontostant = Kontostand - 1 WHERE Name = ?" name]) 
;(jdbc/update! mysql-db :konten {:Kontostand 99} ["Name = ?" name]))
; maybe this sets the guthaben to 99

(defn page [name]
  (str "<html><body>"
       (if name
  (str "Nice to meet you, " name "! \n" "<div id='mgm_response' data='"(sqlquery name)"'></div>")
        (str "<form>"
              "Name: <input name='name' type='text'>"
              "<input type='submit'>"
              "</form>"))
       "</body></html>")
)


(defn handler [{{name "name"} :params}]
  (-> (r/response (page name))
      (r/content-type "text/html")))

(def app
  (-> handler p/wrap-params))

(defn -main
 "Kommentar."
 [& args]
 (j/run-jetty app {:port 8080}))

