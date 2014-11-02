README for neojhipster
==========================
Some running instructions on the fly.

This project is based on the coolest seed app JHipster !!!!. More about it here (http://jhipster.github.io/).

Running this example
--------------------

Make sure you have Maven and Node installed. Clone this repo and in the root directory of your clone give:

    bower install
    npm install

then

    mvn spring-boot:run
    
Play with this app at http://localhost:8080/ 

Finally if you need grunt server (so you can live-reload changes on the front-end)

To have this at http://localhost:8080/ change the file Gruntfile.js line 85 from
0.0.0.0 to localhost and give

    grunt server
    




