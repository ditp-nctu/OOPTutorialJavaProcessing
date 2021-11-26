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

import art.cctcc.c1635.server.ex.InvalidParameterException;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import static art.cctcc.c1635.MySketch.MAX_SIZE;
import static art.cctcc.c1635.MySketch.MIN_SIZE;

public class MainVerticle extends AbstractVerticle {

  static final Logger logger = Logger.getGlobal();
  int port = 8001;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    var router = Router.router(vertx);
    router.get("/object").handler(this::_object);
    vertx.createHttpServer()
            .requestHandler(router)
            .listen(port);
    logger.log(Level.INFO, " Server started on port {0}", port);
  }

  public void _object(RoutingContext ctx) {

    int min_size = MIN_SIZE, max_size = MAX_SIZE;
    var queryParams = ctx.queryParams();
    var msg = "ok";
    try {
      min_size = Integer.parseInt(queryParams.get("min_size"));
      max_size = Integer.parseInt(queryParams.get("max_size"));
      if (min_size > max_size) {
        throw new InvalidParameterException("Invalid query: min_size > max_size.");
      }
    } catch (NumberFormatException e) {
      if (queryParams.isEmpty()) {
        msg = "Empty query, using defaults.";
        logger.log(Level.INFO, " {0}", msg);
      } else {
        msg = "Invalid query (must specify min_size & max_size): " + ctx.request().query();
        logger.log(Level.INFO, " {0}", msg);
      }
    } catch (InvalidParameterException ex) {
      msg = ex.getMessage();
      logger.log(Level.INFO, " {0}", ex.getMessage());
      min_size = MIN_SIZE;
      max_size = MAX_SIZE;
    }

    var response = new Response(ctx.request().query(), min_size, max_size, msg);
    logger.log(Level.INFO, " response = {0}", response);
    ctx.response()
            .putHeader("content-type", "application/json")
            .end(response.toString());
  }
}
