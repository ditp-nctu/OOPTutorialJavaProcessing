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

import java.util.List;
import java.util.Vector;
import processing.core.PApplet;
import processing.core.PImage;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@cctcc.art>
 */
public class MySketch extends PApplet {

  public static final int MIN_SIZE = 5;
  public static final int MAX_SIZE = 50;
  public static final int AMOUNT = 500;
  public static final float DELTA_MOVE = 3.0f;
  public static final float DELTA_SIZE = 0.007f;

  protected List<MyObject> pool = new Vector<>(AMOUNT);
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
    frameRate(30);
    img = loadImage(getClass().getResource("/nctu.png").getFile());
    new Thread(this::generate).start();
    MyObject.HEIGHT = height;
  }

  public void generate() {

    while (pool.size() < AMOUNT) {
      var x = random(1);
      var y = random(1);
      var size = random(
              MIN_SIZE * height / 768,
              MAX_SIZE * height / 768);
      var color = color(random(150, 250), random(150, 250), random(150, 250));
      pool.add(MyObject.getInstance(x, y, size, color));
    }
  }

  @Override
  public void draw() {

    background(bgColor);
    textSize(MAX_SIZE / 2 * height / 768);
    List.copyOf(pool).stream()
            .forEach(obj -> {
              obj.paint(this);
              obj.move(random(-DELTA_MOVE, DELTA_MOVE) / height,
                      random(-DELTA_MOVE, DELTA_MOVE) / height);
              if (pool.size() == AMOUNT) {
                obj.resize(random(-DELTA_SIZE * height, DELTA_SIZE * height));
              } else {
                fill(0);
                text(pool.size(), MAX_SIZE / 2 * height / 768, MAX_SIZE / 2 * height / 768);
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
    PApplet.main(MySketch.class);
  }
}
