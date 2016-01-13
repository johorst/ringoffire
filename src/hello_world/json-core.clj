(ns hello-world.json-core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [ring.adapter.jetty :as jetty]
            [compojure.handler :as handler]))
;            [clojure.java.jdbc :as jdbc])
;  (:gen-class))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;(let [db-host "localhost"
;      db-port 3306
;      db-name "mgm_schema"]

;(def mysql-db {:classname "com.mysql.jdbc.Driver" ; must be in classpath
;           :subprotocol "mysql"
;           :subname (str "//" db-host ":" db-port "/" db-name)
           ; Any additional keys are passed to the driver
           ; as driver-specific properties.
;           :user "usernameplaceholder"
;           :password "passwordplaceholder"}))

;(defn sqlquery [name]
;(first (jdbc/query mysql-db [(str "select * from konten where Name='" name "'")])))
;(jdbc/update! mysql-db :konten {:Guthaben 99} ["Name = ?" name]))
;maybe this sets the guthaben to 99
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;,

(defn- get-article [id]
	{:body {:body "<p>body text</p>"
	        :id id
	        :url (str "http://www.newscorp.com/articles/" id)}
	 :headers {"Cache-Control" "no-transform,public,max-age=10"}})

(defroutes app-routes
  (GET "/api/v1/article/:id" [id] (get-article id)) 
  (route/not-found "Not Found"))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-params)
      (middleware/wrap-json-response)))

(defn -main [& args]
  (jetty/run-jetty app))
