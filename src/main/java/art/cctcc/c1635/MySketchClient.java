package art.cctcc.c1635;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import processing.core.*;
import static processing.core.PConstants.RGB;
import static processing.core.PConstants.RIGHT;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@cctcc.art>
 */
public class MySketchClient extends MySketch {

   List<MyObject> pool = new ArrayList<>();

   @Override
   public void settings() {
      fullScreen();
   }

   @Override
   public void setup() {
      colorMode(RGB);
      img = loadImage(getClass().getResource("/nctu.png").getFile());
   }
   HttpClient client = HttpClient.newHttpClient();

   @Override
   public void draw() {
      if (pool.size() < AMOUNT) {

         HttpRequest request = HttpRequest.newBuilder()
                 .uri(URI.create(String.format("http://localhost:8001/?%s=%d&%s=%d",
                         "MIN_SIZE", (int) MIN_SIZE,
                         "MAX_SIZE", (int) MAX_SIZE)))
                 .build();
         client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                 .thenApply(HttpResponse::body)
                 .thenAccept(body -> {
                    var result = parseJSONObject(body);
                    var x = result.getFloat("x");
                    var y = result.getFloat("y");
                    var size = result.getFloat("size");
                    var color = result.getInt("color");
                    System.out.printf("%f,%f,%f,%d\n", x, y, size, color);
                    pool.add(MyObject.getInstance(x, y, size, color));
                 })
                 .join();
      }
      background(bgColor);
      for (MyObject obj : pool) {
         obj.paint(this);
         obj.move(random(-DELTA_MOVE / width, DELTA_MOVE / width),
                 random(-DELTA_MOVE / height, DELTA_MOVE / height));
         obj.resize(random(-DELTA_SIZE, DELTA_SIZE));
      }
      if (mousePressed) {
         fill(255);
         circle(width / 2, height / 2, img.pixelWidth * 1.5f);
         image(img, (width - img.pixelWidth) / 2,
                 (height - img.height) / 2);
         if (mouseButton == RIGHT) {
            noLoop();
         } else if (isClicked == false) {
            isClicked = true;
            bgColor = color(random(200, 255), random(200, 255), random(200, 255));
         }
      }
   }

   @Override
   public void mouseReleased() {
      isClicked = false;
   }

   public static void main(String[] args) {
      System.setProperty("sun.java2d.uiScale", "1.0");
      PApplet.main("art.cctcc.c1635.MySketchClient");
   }
}
