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
package art.cctcc.c1635;

import java.awt.Color;
import processing.data.JSONObject;
import static art.cctcc.c1635.MySketch.*;

/**
 * An abstract class to be extended by actual shape classes.
 *
 * @author Jonathan Chang, Chun-yien <ccy@cctcc.art>
 */
public abstract class MyObject {

  public static float HEIGHT;
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
    if (delta < 0 && this.size < MIN_SIZE * HEIGHT / 768
            || delta > 0 && this.size > MAX_SIZE * HEIGHT / 768) {
      System.out.println("delta=" + delta + ", size=" + size);
      return;
    }
    this.size += delta;
  }

  public static MyObject getInstance(float x, float y, float size, int color) {
    var result = Math.random() > 0.5
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
