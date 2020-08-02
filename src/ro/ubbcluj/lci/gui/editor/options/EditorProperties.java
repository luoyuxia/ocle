package ro.ubbcluj.lci.gui.editor.options;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class EditorProperties {
   public static Properties defProps;
   public static Properties appProps;
   private static String directory = null;
   public static final byte CARET = 100;
   public static final byte BRACKET = 101;
   public static final byte SELECTION = 102;
   public static final byte LINE = 103;
   public static final byte EOL = 104;
   public static final HashMap tokenNames = new HashMap();

   public EditorProperties() {
   }

   public static void setDirectory(String dir) {
      directory = dir;
   }

   public static void initProperties() {
      if (appProps == null) {
         try {
            defProps = new Properties();
            String sep = System.getProperty("file.separator");
            File f = new File(System.getProperty("user.home") + sep + directory + sep + "default_editor_options.properties");
            InputStream in = null;
            if (!f.exists()) {
               in = Class.forName("ro.ubbcluj.lci.gui.editor.options.EditorProperties").getResourceAsStream("default_editor_options.properties");
            } else {
               in = new FileInputStream(f);
            }

            defProps.load((InputStream)in);
            ((InputStream)in).close();
            appProps = new Properties(defProps);
            f = new File(System.getProperty("user.home") + sep + directory + sep + "editor_options.properties");
            if (f.exists()) {
               in = new FileInputStream(f);
               appProps.load(in);
               in.close();
            }
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

   }

   public static void saveAppProps(Properties props) {
      String sep = System.getProperty("file.separator");
      File f = new File(System.getProperty("user.home") + sep + directory);
      if (!f.exists()) {
         f.mkdir();
      }

      f = new File(System.getProperty("user.home") + sep + directory + sep + "editor_options.properties");

      try {
         f.createNewFile();
         FileOutputStream out = new FileOutputStream(f);
         props.store(out, "User-selected options for the OCL editor");
         out.close();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void saveDefProps(Properties props) {
      String sep = System.getProperty("file.separator");
      File f = new File(System.getProperty("user.home") + sep + directory);
      if (!f.exists()) {
         f.mkdir();
      }

      f = new File(System.getProperty("user.home") + sep + directory + sep + "default_editor_options.properties");

      try {
         f.createNewFile();
         FileOutputStream out = new FileOutputStream(f);
         props.store(out, "Default options for the OCL editor");
         out.close();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   static {
      tokenNames.put("One-line comments", new Byte((byte)1));
      tokenNames.put("Multiple-line comments", new Byte((byte)2));
      tokenNames.put("Invalid tokens", new Byte((byte)10));
      tokenNames.put("Keywords", new Byte((byte)6));
      tokenNames.put("Strings & numbers", new Byte((byte)3));
      tokenNames.put("Enum literals", new Byte((byte)4));
      tokenNames.put("Null tokens", new Byte((byte)0));
      tokenNames.put("Caret color", new Byte(CARET));
      tokenNames.put("Bracket highlight color", new Byte(BRACKET));
      tokenNames.put("Selection color", new Byte(SELECTION));
      tokenNames.put("Line highlight color", new Byte(LINE));
      tokenNames.put("EOL markers color", new Byte(EOL));
   }
}
