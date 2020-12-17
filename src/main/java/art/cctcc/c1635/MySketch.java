package art.cctcc.c1635;

import java.util.ArrayList;
import java.util.List;
import processing.core.*;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@cctcc.art>
 */
public class MySketch extends PApplet {

   static public final int MIN_SIZE = 5;
   static public final int MAX_SIZE = 50;
   static final int AMOUNT = 1000;
   static final float DELTA_MOVE = 3.0f; // in pixel
   static final float DELTA_SIZE = 0.01f; // in ratio

   List<MyObject> pool = new ArrayList<>(AMOUNT);
   int bgColor = 255;
   PImage img;
   boolean isClicked;

   @Override
   public void settings() {
      fullScreen();
   }

   @Override
   public void setup() {
      colorMode(RGB);
      for (int i = 0; i < AMOUNT; i++) {
         var x = random(1);
         var y = random(1);
         var size = random(MIN_SIZE, MAX_SIZE);
         var color = color(random(150, 250), random(150, 250), random(150, 250));
         pool.add(MyObject.getInstance(x, y, size, color));
      }
      img = loadImage(getClass().getResource("/nctu.png").getFile());
   }

   @Override
   public void draw() {
      background(bgColor);
      var it = pool.iterator();
      List.copyOf(pool).stream().forEach(obj -> {
         obj.paint(this);
         obj.move(random(-DELTA_MOVE, DELTA_MOVE) / width,
                 random(-DELTA_MOVE, DELTA_MOVE) / height);
         if (pool.size() == AMOUNT) {
            obj.resize(random(-DELTA_SIZE * height, DELTA_SIZE * height));
         }
      });
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
      PApplet.main("art.cctcc.c1635.MySketch");
   }
}
