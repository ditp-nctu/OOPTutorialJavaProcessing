/*
 * Copyright 2020 Jonathan Chang, Chun-yien <ccy@musicapoetica.org>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package art.cctcc.c1635.server;

import java.io.IOException;
import java.util.logging.Logger;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.stream.Collectors;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import processing.core.PApplet;
import static art.cctcc.c1635.MySketch.MAX_SIZE;
import static art.cctcc.c1635.MySketch.MIN_SIZE;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
public class ServerMain {

  static final Logger logger = Logger.getGlobal();
  static int port = 8001;

  public static void main(String[] args) throws IOException {

    var server = HttpServer.create(new InetSocketAddress(port), 0);
    server.createContext("/object", ServerMain::_object);
    //server.setExecutor(Executors.newFixedThreadPool(10));
    server.start();
    logger.log(Level.INFO, " Server started on port {0}", port);
  }

  public static void _object(HttpExchange exchange) {

    int min_size = MIN_SIZE, max_size = MAX_SIZE;
    var query = exchange.getRequestURI().getQuery();
    var msg = "ok";
    try {
      var params = Arrays.stream(query.split("&"))
              .map(s -> s.split("="))
              .filter(a -> a.length == 2)
              .collect(Collectors.toMap(p -> p[0].toLowerCase(), p -> p[1]));
      min_size = Integer.parseInt(params.get("min_size"));
      max_size = Integer.parseInt(params.get("max_size"));
    } catch (NullPointerException ex) {
      msg = "Empty query, using defaults.";
      logger.log(Level.INFO, " {0}", msg);
    } catch (NumberFormatException ex) {
      msg = "Invalid query: " + query;
      logger.log(Level.INFO, " {0}", msg);
    }
    var response = new Response(query, min_size, max_size, msg);
    logger.log(Level.INFO, " response = {0}", response);
    try ( var outputStream = exchange.getResponseBody()) {
      var output = response.toString().getBytes();
      exchange.sendResponseHeaders(200, output.length);
      outputStream.write(output);
      outputStream.flush();
    } catch (IOException ex) {
      Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
