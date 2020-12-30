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
package art.cctcc.c1635.client;

import art.cctcc.c1635.MyObject;
import art.cctcc.c1635.MySketch;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import processing.core.*;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@cctcc.art>
 */
public class MySketchClient extends MySketch {

   String server_address = "http://localhost";
   int port = 8001;

   @Override
   public void generate() {
      var client = HttpClient.newHttpClient();
      var request = HttpRequest.newBuilder()
              .uri(URI.create(String.format("%s:%d/object?%s=%d&%s=%d",
                      server_address, port,
                      "MIN_SIZE", MIN_SIZE * height / 768,
                      "MAX_SIZE", MAX_SIZE * height / 768)))
              .build();
      while (pool.size() < AMOUNT) {
         client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                 .thenApply(HttpResponse::body)
                 .thenApply(this::parseJSONObject)
                 .thenApply(MyObject::getInstance)
                 .thenAccept(myObj -> {
                    pool.add(myObj);
                    System.out.printf("%d: Creating %s\n", pool.size(), myObj);
                 })
                 .join();
      }
   }

   public static void main(String[] args) {
      System.setProperty("sun.java2d.uiScale", "1.0");
      PApplet.main("art.cctcc.c1635.MySketchClient");
   }
}
