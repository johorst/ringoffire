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
  (jdbc/execute! mysql-db ["insert into transaktionen_geberkonten2nehmerkonten (betrag) select MIN(geberkonten.stand) from geberkonten where geberkonten.nr in ( ? , '00000000-0000-0000-0000-00000025')" name])
  (jdbc/execute! mysql-db ["insert into transaktionen_geberkonten2nehmerkonten (valid) select valid from geberkonten where geberkonten.nr =?" name])
  (jdbc/update! mysql-db :transaktionen_geberkonten2nehmerkonten {:sender name, :empfaenger empfaenger, :valid 0} (sql/where {:valid 1}))
  (jdbc/execute! mysql-db ["update geberkonten set stand = stand - 25 where geberkonten.nr = ? AND stand > 0" name])
)



(defn page [name empfaenger]
  (str "<!DOCTYPE html><body>"
       (if name
  (str "Nice to meet you, " name "! \n" "<div id='mgm_response' data='"(sqlquery name empfaenger)"'></div>")
        (str "<form>"
              "Name: <input name='name' type='text'>"
              "<input type='submit'>"
              "</form>"))
       "</body></html>")
)


(defn handler [{{name "name" empfaenger "empfaenger"} :params}]
  (-> (r/response (page name empfaenger))
      (r/content-type "text/html")))

(def app
  (-> handler p/wrap-params))

(defn -main
 "Kommentar."
 [& args]
 (j/run-jetty app {:port 8080}))

