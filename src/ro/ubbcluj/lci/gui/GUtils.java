package ro.ubbcluj.lci.gui;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class GUtils {
   public static final String resourcePath = "/resources/";

   public GUtils() {
   }

   public static Icon loadIcon(String url) {
      return new ImageIcon((GUtils.class).getResource(url));
   }
}
