package ro.ubbcluj.lci.utils;

import java.io.File;
import java.io.IOException;

public class FileUtilities {
   public FileUtilities() {
   }

   public static File asFullPathFile(File file) throws IOException {
      if (file == null) {
         return null;
      } else {
         File fpfile = null;
         fpfile = file.getCanonicalFile();
         return fpfile;
      }
   }

   public static String asFullPathFile(String fileName) throws IOException {
      if (fileName == null) {
         return null;
      } else {
         File ftemp = new File(fileName);
         return ftemp.getCanonicalPath();
      }
   }

   public static String asFullPath(String url) throws IOException {
      return (new File(url)).getCanonicalFile().getAbsolutePath();
   }
}
