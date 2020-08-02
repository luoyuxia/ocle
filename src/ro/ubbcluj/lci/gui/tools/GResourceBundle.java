package ro.ubbcluj.lci.gui.tools;

import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

public class GResourceBundle {
   private ResourceBundle resources;
   public static final String LABEL_SUFIX = "Label";
   public static final String IMAGE_SUFIX = "Image";
   public static final String DISABLED_SUFIX = "Disabled";
   public static final String TOOLTIP_SUFIX = "Tooltip";
   public static final String COMMAND_KEY_SUFIX = "CommandKey";

   public GResourceBundle(String resourcePath) {
      try {
         this.resources = ResourceBundle.getBundle(resourcePath, Locale.getDefault());
      } catch (MissingResourceException var3) {
         System.err.println(this.getClass().getName() + ": Properties not found");
         System.err.println(var3);
         System.exit(1);
      }

   }

   public Object handleGetObject(String obj) {
      return null;
   }

   public Enumeration getKeys() {
      return this.resources.getKeys();
   }

   public String getResourceString(String nm) {
      String str = null;

      try {
         str = this.resources.getString(nm);
      } catch (Exception var4) {
      }

      return str;
   }

   public String[] tokenize(String input) {
      Vector tokens = new Vector();
      StringTokenizer tokenizer = new StringTokenizer(input);

      while(tokenizer.hasMoreTokens()) {
         tokens.addElement(tokenizer.nextToken());
      }

      String[] ret = new String[tokens.size()];

      for(int i = 0; i < tokens.size(); ++i) {
         ret[i] = (String)tokens.get(i);
      }

      return ret;
   }

   public String getResource(String key) {
      String name = this.getResourceString(key);
      return name;
   }
}
