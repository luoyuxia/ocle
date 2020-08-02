package ro.ubbcluj.lci.xmi.utils;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

public class XMIUtilities {
   public XMIUtilities() {
   }

   public static String toURI(String filename) {
      return filename.replace('\\', '/');
   }

   public static String toURI(File file) {
      try {
         String filename = file.getCanonicalPath();
         return toURI(filename);
      } catch (IOException var2) {
         return null;
      }
   }

   public static String toURI(File file, boolean directory) {
      try {
         return toURI(file.getCanonicalPath(), directory);
      } catch (IOException var3) {
         return null;
      }
   }

   public static String toURI(String filename, boolean directory) {
      String uri = toURI(filename);
      if (directory && !uri.endsWith("/")) {
         uri = uri + "/";
      } else if (!directory && uri.endsWith("/")) {
         uri = uri.substring(0, uri.length() - 1);
      }

      return uri;
   }

   public static String createLocalURI(String directory, String filename) {
      return "file://" + toURI(directory, true) + toURI(filename, false);
   }

   public static String createLocalURI(String filename) {
      return "file://" + toURI(filename, false);
   }

   public static String resolveRelativePath(String baseDirectory, String forFile) {
      String relativePath = null;
      if (forFile.indexOf(baseDirectory) > -1) {
         relativePath = forFile.substring(baseDirectory.length()).toString();
      }

      return relativePath;
   }

   public static String resolveRelativePath(String baseDirectory, String currentFile, String forFile) {
      currentFile = baseDirectory + currentFile;
      String currentFileDirectory = currentFile.substring(0, currentFile.lastIndexOf(47) + 1);
      StringBuffer path = new StringBuffer(currentFileDirectory);
      StringTokenizer st = new StringTokenizer(forFile, "/");

      String nextToken;
      while(st.hasMoreTokens()) {
         nextToken = st.nextToken();
         String lastDirectory = path.substring(0, path.length() - 1).toString();
         lastDirectory = lastDirectory.substring(lastDirectory.lastIndexOf("/") + 1);
         if (nextToken.equals("..")) {
            path.setLength(path.length() - lastDirectory.length() - 1);
         } else if (!nextToken.equals(lastDirectory)) {
            path.append(nextToken + "/");
         }
      }

      nextToken = toURI(path.toString(), false);
      return resolveRelativePath(baseDirectory, nextToken);
   }

   public static String getAbsoluteFromRelativePath(String baseDir, String relativePath) {
      String baseDirname = toURI(baseDir, true);
      String relPathname = toURI(relativePath);
      StringBuffer absolutePath = new StringBuffer(baseDir.startsWith("/") ? "/" : "");
      StringTokenizer dirTokenizer = new StringTokenizer(baseDirname, "/");
      StringTokenizer relpathTokenizer = new StringTokenizer(relPathname, "/");
      int dirCount = dirTokenizer.countTokens();
      boolean stillGoes = true;

      while(stillGoes) {
         String relpathToken = relpathTokenizer.hasMoreTokens() ? relpathTokenizer.nextToken() : null;
         --dirCount;
         if (relpathToken == null || dirCount == -1) {
            return null;
         }

         if (!relpathToken.equals("..")) {
            for(stillGoes = false; dirCount >= 0; --dirCount) {
               absolutePath.append(dirTokenizer.nextToken() + "/");
            }

            absolutePath.append(relpathToken);

            while(relpathTokenizer.hasMoreTokens()) {
               absolutePath.append("/" + relpathTokenizer.nextToken());
            }
         }
      }

      return absolutePath.toString();
   }

   public static String getPathRelativeTo(File forFile, File baseDirectory) {
      try {
         StringBuffer relativePath = new StringBuffer();
         String forFileName = toURI(forFile.getCanonicalPath());
         String baseDirectoryName = toURI(baseDirectory.getCanonicalPath());
         StringTokenizer fileTokenizer = new StringTokenizer(forFileName, "/");
         StringTokenizer dirTokenizer = new StringTokenizer(baseDirectoryName, "/");
         boolean matched = false;
         boolean stillMatches = true;
         String fileToken = null;
         String dirToken = null;

         while(stillMatches) {
            fileToken = fileTokenizer.hasMoreTokens() ? fileTokenizer.nextToken() : null;
            dirToken = dirTokenizer.hasMoreTokens() ? dirTokenizer.nextToken() : null;
            if (fileToken == null || dirToken == null) {
               break;
            }

            if (fileToken.equals(dirToken)) {
               matched = true;
            } else {
               stillMatches = false;
            }
         }

         if (matched && fileToken != null) {
            if (dirToken != null) {
               relativePath.append("..");

               while(dirTokenizer.hasMoreTokens()) {
                  dirToken = dirTokenizer.nextToken();
                  relativePath.append("/..");
               }

               relativePath.append("/");
            }

            relativePath.append(fileToken);

            while(fileTokenizer.hasMoreTokens()) {
               relativePath.append("/" + fileTokenizer.nextToken());
            }

            return relativePath.toString();
         } else {
            return null;
         }
      } catch (IOException var12) {
         return null;
      }
   }
}
