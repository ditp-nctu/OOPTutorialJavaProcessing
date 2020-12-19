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

import java.util.logging.Level;
import java.util.logging.Logger;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import processing.core.PApplet;
import static art.cctcc.c1635.MySketch.MAX_SIZE;
import static art.cctcc.c1635.MySketch.MIN_SIZE;

public class MainVerticle extends AbstractVerticle {

   static final Logger logger = Logger.getGlobal();
   int port = 8001;
   PApplet p = new PApplet();

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
      try {
         min_size = Integer.parseInt(queryParams.get("min_size"));
         max_size = Integer.parseInt(queryParams.get("max_size"));
      } catch (NumberFormatException e) {
         if (queryParams.isEmpty()) {
            logger.log(Level.INFO, " empty query, using defaults.");
         } else {
            logger.log(Level.INFO, " invalid query: {0}", ctx.request().query());
         }
      }
      var response = new Response(ctx.request().query(), min_size, max_size);
      logger.log(Level.INFO, " response = {0}", response);
      ctx.response()
              .putHeader("content-type", "application/json")
              .end(response.toString());
   }
}
