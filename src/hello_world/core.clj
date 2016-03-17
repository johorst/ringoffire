(ns hello-world.core
  (:require [ring.middleware.params :as p]
        [ring.util.response :as r]
        [ring.adapter.jetty :as j]
        [clojure.java.jdbc :as jdbc]
        [java-jdbc.sql :as sql])
  (:gen-class))

(let [db-host "localhost"
      db-port 3306
      db-name "mgm_schema"]

  (def mysql-db {:classname "com.mysql.jdbc.Driver" ; must be in classpath
           :subprotocol "mysql"
           :subname (str "//" db-host ":" db-port "/" db-name)
           :user "usernameplaceholder"
           :password "passwordplaceholder"}))


(defn sqlquery [name empfaenger]
  (jdbc/execute! mysql-db ["insert into transaktionen_geberkonten2nehmerkonten (betrag) select MIN(geberkonten.stand) from geberkonten where geberkonten.nr in ( ? , '00000000-0000-0000-0000-00000025')" name])
  (jdbc/update! mysql-db :transaktionen_geberkonten2nehmerkonten {:sender name, :empfaenger empfaenger, :toggle 0} (sql/where {:toggle 1}))
  (jdbc/execute! mysql-db ["update transaktionen_geberkonten2nehmerkonten set betrag = betrag * (select count(*) from geberkonten where geberkonten.nr = ? AND stand > 0) where sender=?" name name])
  (jdbc/execute! mysql-db ["update geberkonten set stand = stand - 25 where geberkonten.nr = ? AND stand > 0" name])
)

(defn sqlquery_s [saldo]
 (str  
 (:stand
  (first (jdbc/query mysql-db ["select stand from geberkonten where geberkonten.nr = ?" saldo]))) "  "
  (vals
  (first (jdbc/query mysql-db ["select sum(betrag) from transaktionen_geberkonten2nehmerkonten where empfaenger = ?" saldo]))
  ))
)

(defn page [name empfaenger saldo]
  (str "<!DOCTYPE html><body>"
       (if name
  (str "Nice to meet you, " name "! \n" "<div id='mgm_response' data='"(sqlquery name empfaenger)"'></div>")
         (str "<form>"
              "Name: <input name='name' type='text'>"
              "<input type='submit'>"
              "</form>"))
       (if saldo
  (str "Dein Guthaben ist: " (sqlquery_s saldo)))
            "</body></html>")
)


(defn handler [{{name "name" empfaenger "empfaenger" saldo "saldo"} :params}]
  (-> (r/response (page name empfaenger saldo))
      (r/content-type "text/html")))

(def app
  (-> handler p/wrap-params))

(defn -main
 "Kommentar."
 [& args]
 (j/run-jetty app {:port 8080}))

