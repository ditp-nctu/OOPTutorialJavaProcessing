package art.cctcc.c1635;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import processing.core.*;
import processing.data.JSONObject;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@cctcc.art>
 */
public class MySketchClient extends MySketch {

   String server_address = "http://localhost";
   int port = 8001;

   @Override
   public void generate() {
      try {
         URL url = new URL(String.format("%s:%d/object?%s=%d&%s=%d",
                 server_address, port,
                 "MIN_SIZE", MIN_SIZE * height / 768,
                 "MAX_SIZE", MAX_SIZE * height / 768));
         while (pool.size() < AMOUNT) {
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            try (InputStream input = http.getInputStream();
                    InputStreamReader isr = new InputStreamReader(input);
                    BufferedReader br = new BufferedReader(isr)) {
               String body = br.lines().collect(Collectors.joining());
               JSONObject jo = parseJSONObject(body);
               MyObject myObj = MyObject.getInstance(jo);
               pool.add(myObj);
               System.out.printf("%d: Creating %s\n", pool.size(), myObj);
            }
         }
      } catch (IOException ex) {
         Logger.getLogger(MySketchClient.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public static void main(String[] args) {
      System.setProperty("sun.java2d.uiScale", "1.0");
      PApplet.main("art.cctcc.c1635.MySketchClient");
   }
}
