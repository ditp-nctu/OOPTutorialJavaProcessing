package art.cctcc.c1635;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import processing.core.*;
import static processing.core.PConstants.RGB;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@cctcc.art>
 */
public class MySketchClient extends MySketch {

   String server_address = "http://localhost:8001/object";

   int counter = 0;
   Runnable generator = () -> {
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create(String.format(server_address + "?%s=%d&%s=%d",
                      "MIN_SIZE", MIN_SIZE * height / 768,
                      "MAX_SIZE", MAX_SIZE * height / 768)
              ))
              .build();
      while (counter < AMOUNT) {
         client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                 .thenApply(HttpResponse::body)
                 .thenAccept(body -> {
                    var result = parseJSONObject(body);
                    var x = result.getFloat("x");
                    var y = result.getFloat("y");
                    var size = result.getFloat("size");
                    var color = result.getInt("color");
                    System.out.printf("%d: %f,%f,%f,%d\n", counter, x, y, size, color);
                    pool[counter++] = MyObject.getInstance(x, y, size, color);
                 })
                 .join();
      }
   };

   @Override
   public void setup() {
      colorMode(RGB);
      img = loadImage(getClass().getResource("/nctu.png").getFile());
      new Thread(generator).start();
   }

   public static void main(String[] args) {
      System.setProperty("sun.java2d.uiScale", "1.0");
      PApplet.main("art.cctcc.c1635.MySketchClient");
   }
}
