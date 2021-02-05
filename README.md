# For client-server demo
Firstly, compile the code:
```
mvn clean package
```
Secondly, start __ServerMain.java__ written in simple HttpServer:
```
mvn exec:java@server
```
or __MainVerticle.java__ implemented with Vert.x:
```
mvn exec:java@vertx
```
Lastly, run __MySketchClient.java__:
```
mvn exec:java@client
```