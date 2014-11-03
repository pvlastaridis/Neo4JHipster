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
    

NodeEntities & Utilization of Spring Data Neo4J
-----------------------------------------------
1) User Nodes and Authority Nodes

The users that can login in JHipster application are represented as nodes and they are connected to nodes Authorities that they represent the rights (roles) of the users in our application. Although this could have been represented as an array of Strings property on node Users we follow the same convention used in JHipster (MySQL and Mongo)and create also Authorities separately as entities in case future application requirements may need to interconnect other type of nodes to authorities nodes as well.

2) Persistent Token Nodes

These nodes represent the session of a user and they contain a series property for security authentication checks, which is refreshed by the security filter every time a request from a user is invoked to our application. The series and token value are stored in a cookie and they are checked by methods in CustomPersistentRememberMeServices class. The persistent tokens nodes are connected to Users also.

3)  PersistentAuditEvent and PersistentAuditEventData Nodes

These nodes are managed by the Spring Boot actuator for audit reporting. The persistentAuditEvent node should have a property PersistentAuditEventData Map<String, String> I chose to represent this as a different node although it would be possible to represent it as an array of Strings where each array entry is a string that can be split to two different strings using a special delimiter.



