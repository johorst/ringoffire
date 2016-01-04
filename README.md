# hello-world

A Clojure library designed to show a "cut through" from application layer to Database (mySQL).
Based on cojure / ring (which uses jetty).

## Usage
(don't ;) ) <br>
Be shure to have a MySQL Instance running.<br>
clone project.<br>
'lein run'<br>
starts server. As printed in hello-world/core.clj your mySQL DB is connected (and written to).

my deploy.bat:
cd server
rm * -rf
git clone https://github.com/johorst/ringoffire.git
cd ringoffire
//TODO: mysql credentials
lein run


## License

Copyright © 2015 ?

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
