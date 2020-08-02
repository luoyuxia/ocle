package ro.ubbcluj.lci.gui.editor.utils;

public class Utilities {
   public static final char[] separators = new char[]{' ', '\t', '\n', '\r', '.', '-', '+', '*', '\\', '/', '<', '>', '(', ')', '[', ']', '{', '}'};

   public Utilities() {
   }

   public static boolean startsWithSeparator(String t) {
      for(int i = 0; i < separators.length; ++i) {
         if (t.startsWith(String.valueOf(separators[i]))) {
            return true;
         }
      }

      return false;
   }

   public static boolean endsWithSeparator(String t) {
      for(int i = 0; i < separators.length; ++i) {
         if (t.endsWith(String.valueOf(separators[i]))) {
            return true;
         }
      }

      return false;
   }

   public static String getFileNameSuffix(String fileName) {
      if (fileName == null) {
         return null;
      } else {
         int p = fileName.lastIndexOf(46);
         return p < 0 ? null : fileName.substring(p + 1).toLowerCase();
      }
   }
}
