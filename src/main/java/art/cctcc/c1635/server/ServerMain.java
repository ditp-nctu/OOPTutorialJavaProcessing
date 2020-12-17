package art.cctcc.c1635.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import processing.core.PApplet;
import processing.data.JSONObject;
import static art.cctcc.c1635.MySketch.MIN_SIZE;
import static art.cctcc.c1635.MySketch.MAX_SIZE;
import java.io.PrintWriter;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
public class ServerMain {

   static PApplet p = new PApplet();
   static int port = 8001;
   static final Logger logger = Logger.getGlobal();

   public static void main(String[] args) throws IOException {

      var server = HttpServer.create(new InetSocketAddress(port), 0);

      server.createContext("/object", (HttpExchange exchange) -> {
         var query = exchange.getRequestURI().getQuery();
         Map<String, String> params = (query == null)
                 ? Map.of()
                 : Arrays.stream(query.split("&"))
                         .map(s -> s.split("="))
                         .filter(a -> a.length == 2)
                         .collect(Collectors.toMap(p -> p[0].toUpperCase(), p -> p[1]));
         var min_size = Integer.parseInt(params.getOrDefault("MIN_SIZE", String.valueOf(MIN_SIZE)));
         var max_size = Integer.parseInt(params.getOrDefault("MAX_SIZE", String.valueOf(MAX_SIZE)));
         var response = new JSONObject();
         response.put("query", query)
                 .put("x", p.random(1))
                 .put("y", p.random(1))
                 .put("size", p.random(min_size, max_size))
                 .put("color", p.color(p.random(150, 250), p.random(150, 250), p.random(150, 250)));
         System.out.println("response = " + response.toString());
         try (var outputStream = exchange.getResponseBody();
                 var writer = new PrintWriter(outputStream)) {
            exchange.sendResponseHeaders(200, response.toString().length());
            response.write(writer);
            outputStream.flush();
         }
      });

      server.setExecutor(Executors.newFixedThreadPool(10));
      server.start();

      logger.log(Level.INFO, " Server started on port {0}", port);
   }
}
