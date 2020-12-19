# For client-server demo
Firstly, compile the code:
```
mvn clean package
```
Secondly, start __ServerMain.java__ written in simple HttpServer:
```
java -jar target\ProcessingInJava-1.0-SNAPSHOT-jar-with-dependencies.jar
```
or __MainVerticle.java__ implemented with Vert.x:
```
mvn exec:java
```
Lastly, run __MySketchClient.java__:
```
java -cp target\ProcessingInJava-1.0-SNAPSHOT-jar-with-dependencies.jar art.cctcc.c1635.MySketchClient
```