cchat
=====

Simple example of use of akka io for a sort of chat with a broadcast server.

Sample use (execute each command in a different terminal):

     $ sbt "run serve localhost 5555"
     $ sbt "run connect localhost 5555"
     $ sbt "run connect localhost 5555"
   
You will see the randomly generated client id when each client starts 
(e.g. `Connecting to localhost/127.0.0.1:5555 with id 559`) and the use it to
send messages in the form `id message` (e.g. `559 hello world`).
