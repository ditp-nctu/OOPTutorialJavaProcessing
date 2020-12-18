package art.cctcc.c1635;

import static art.cctcc.c1635.MySketch.*;
import java.awt.Color;
import processing.data.JSONObject;

/**
 * An abstract class to be extended by actual shape classes.
 *
 * @author Jonathan Chang, Chun-yien <ccy@cctcc.art>
 */
public abstract class MyObject {

   float x, y; // in ratio: [0, 1)
   float size; // in pixels
   int color;  // RGB

   public MyObject(float x, float y, float size, int color) {
      this.x = x;
      this.y = y;
      this.size = size;
      this.color = color;
   }

   public void move(float delta_x, float delta_y) {
      this.x += delta_x + 1;
      this.y += delta_y + 1;
      this.x %= 1;
      this.y %= 1;
   }

   public void resize(float delta) {
      if (delta < 0 && this.size < MIN_SIZE
              || delta > 0 && this.size > MAX_SIZE) {
         return;
      }
      this.size += delta;
   }

   public static MyObject getInstance(float x, float y, float size, int color) {
      MyObject result = Math.random() > 0.5
              ? new Circle(x, y, size, color)
              : new Rect(x, y, size, color);
      return result;
   }

   public static MyObject getInstance(JSONObject jo) {
      return getInstance(jo.getFloat("x"), jo.getFloat("y"),
              jo.getFloat("size"), jo.getInt("color"));
   }

   public int darkerColor() {
      return new Color(color).darker().getRGB();
   }

   public abstract void paint(MySketch canvas);

   @Override
   public String toString() {
      return this.getClass().getSimpleName()
              + "{" + "x=" + x + ", y=" + y + ", size=" + size + ", color=" + color + '}';
   }

}
