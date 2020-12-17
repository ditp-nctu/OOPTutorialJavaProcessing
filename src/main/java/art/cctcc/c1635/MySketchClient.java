package art.cctcc.c1635;

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
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create(String.format("%s:%d/object?%s=%d&%s=%d",
                      server_address, port,
                      "MIN_SIZE", MIN_SIZE * height / 768,
                      "MAX_SIZE", MAX_SIZE * height / 768)
              ))
              .build();
      while (pool.size() < AMOUNT) {
         System.out.print(pool.size() + ": ");
         client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                 .thenApply(HttpResponse::body)
                 .thenApply(this::parseJSONObject)
                 .thenApply(
                         jo -> MyObject.getInstance(
                                 jo.getFloat("x"), jo.getFloat("y"),
                                 jo.getFloat("size"), jo.getInt("color")))
                 .thenAccept(pool::add)
                 .join();
      }
   }

   public static void main(String[] args) {
      System.setProperty("sun.java2d.uiScale", "1.0");
      PApplet.main("art.cctcc.c1635.MySketchClient");
   }
}
