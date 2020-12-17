package art.cctcc.c1635.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import processing.core.PApplet;
import processing.data.JSONObject;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
public class ServerMain {

   static PApplet p = new PApplet();

   public static void main(String[] args) throws IOException {

      HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
      var logger = Logger.getGlobal();
      server.createContext("/", (HttpExchange exchange) -> {
         var params = Arrays.stream(exchange.getRequestURI().getQuery().split("&"))
                 .map(s -> s.split("="))
                 .collect(Collectors.toMap(p -> p[0], p -> p[1]));
         var min_size = Integer.parseInt(params.get("MIN_SIZE"));
         var max_size = Integer.parseInt(params.get("MAX_SIZE"));
         var x = p.random(1);
         var y = p.random(1);
         var size = p.random(min_size, max_size);
         var color = p.color(p.random(150, 250), p.random(150, 250), p.random(150, 250));
         var response = new JSONObject();
         response.put("x", (float) Math.random());
         response.put("y", (float) Math.random());
         response.put("size", (float) p.random(min_size, max_size));
         response.put("color", color);
         System.out.println("response = " + response.toString());
         try (OutputStream outputStream = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, response.toString().length());
            outputStream.write(response.toString().getBytes());
            outputStream.flush();
         }
      });

      server.setExecutor(Executors.newFixedThreadPool(10));
      server.start();

      logger.info(" Server started on port 8001");
   }
}
