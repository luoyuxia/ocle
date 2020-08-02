package ro.ubbcluj.lci.gui.tools;

import java.awt.Component;
import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.StringTokenizer;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class AFileFilter extends FileFilter implements FilenameFilter {
   public static final int OPEN = 0;
   public static final int SAVE = 1;
   public static File LAST_DIR = new File(".");
   private static JFileChooser jFileChooser = new JFileChooser();
   private String description;
   private LinkedList extensions = new LinkedList();

   public static File chooseFile(int mode, Component parent, FileFilter[] filters, String title) {
      String old = jFileChooser.getDialogTitle();
      jFileChooser.setDialogTitle(title);
      File result = chooseFile(mode, parent, filters);
      jFileChooser.setDialogTitle(old);
      return result;
   }

   public static File chooseFile(int mode, Component parent, FileFilter[] filters) {
      jFileChooser.setCurrentDirectory(LAST_DIR);
      jFileChooser.setMultiSelectionEnabled(false);

      int i;
      for(i = 0; i < filters.length; ++i) {
         jFileChooser.addChoosableFileFilter(filters[i]);
      }

      int ret;
      if (mode == 1) {
         ret = jFileChooser.showSaveDialog(parent);
      } else {
         ret = jFileChooser.showOpenDialog(parent);
      }

      FileFilter ff = jFileChooser.getFileFilter();

      for(i = 0; i < filters.length; ++i) {
         jFileChooser.removeChoosableFileFilter(filters[i]);
      }

      File result;
      if (ret == 0) {
         result = jFileChooser.getSelectedFile();
         if (result == null) {
            return null;
         } else {
            LAST_DIR = result.getParentFile();
            if (ff instanceof AFileFilter && mode == 1) {
               AFileFilter aff = (AFileFilter)ff;
               String[] ext = aff.getExtensions();
               if (ext.length == 1) {
                  String e = ext[0];
                  String path = result.getAbsolutePath();
                  if (!path.endsWith("." + e)) {
                     path = path + "." + e;
                     result = new File(path);
                  }
               }
            }

            return result;
         }
      } else {
         result = jFileChooser.getCurrentDirectory();
         if (result != null) {
            LAST_DIR = result;
         }

         return null;
      }
   }

   public static File chooseDirectory(Component parent) {
      jFileChooser.setCurrentDirectory(LAST_DIR);
      jFileChooser.setMultiSelectionEnabled(false);
      FileFilter f = new FileFilter() {
         public boolean accept(File f) {
            return f.isDirectory();
         }

         public String getDescription() {
            return "Directories";
         }
      };
      jFileChooser.addChoosableFileFilter(f);
      jFileChooser.setFileSelectionMode(1);
      int ret = jFileChooser.showDialog(parent, "Choose directory");
      File cd = jFileChooser.getCurrentDirectory();
      if (cd != null) {
         LAST_DIR = cd;
      }

      jFileChooser.removeChoosableFileFilter(f);
      jFileChooser.setFileSelectionMode(2);
      return ret == 0 ? jFileChooser.getSelectedFile() : null;
   }

   public AFileFilter(String ext, String description) {
      this.addExtension(ext);
      this.description = description;
   }

   public AFileFilter() {
   }

   public static final File[] chooseFiles(Component parent, String title, FileFilter[] filters) {
      jFileChooser.setCurrentDirectory(LAST_DIR);
      jFileChooser.setMultiSelectionEnabled(true);

      int i;
      for(i = 0; i < filters.length; ++i) {
         jFileChooser.addChoosableFileFilter(filters[i]);
      }

      int ret = jFileChooser.showDialog(parent, title);
      File temp = jFileChooser.getCurrentDirectory();
      if (temp != null) {
         LAST_DIR = temp;
      }

      File[] files;
      if (ret == 0) {
         files = jFileChooser.getSelectedFiles();
         FileFilter currentFilter = jFileChooser.getFileFilter();
         if (currentFilter instanceof AFileFilter) {
            AFileFilter aff = (AFileFilter)currentFilter;
            String[] ext = aff.getExtensions();
            if (ext.length == 1) {
               String suffix = '.' + ext[0];

               for(int j = 0; j < files.length; ++j) {
                  String fileName = files[j].getAbsolutePath();
                  if (!fileName.endsWith(suffix)) {
                     files[j] = new File(fileName + suffix);
                  }
               }
            }
         }
      } else {
         files = null;
      }

      for(i = 0; i < filters.length; ++i) {
         jFileChooser.removeChoosableFileFilter(filters[i]);
      }

      return files;
   }

   public void addExtension(String ext) {
      StringTokenizer tok = new StringTokenizer(ext, ",");

      while(tok.hasMoreElements()) {
         this.extensions.add(tok.nextToken().toLowerCase());
      }

   }

   public boolean accept(File f) {
      return f.isDirectory() ? true : this.accept(f, f.getName());
   }

   public boolean accept(File f, String name) {
      boolean result = false;
      if (f != null) {
         ListIterator iter = this.extensions.listIterator(0);

         while(iter.hasNext() && !result) {
            String ext = (String)iter.next();
            if (name.toLowerCase().endsWith(ext)) {
               result = true;
            }
         }
      }

      return result;
   }

   public String getDescription() {
      return this.description;
   }

   public String[] getExtensions() {
      String[] e = (String[])this.extensions.toArray(new String[0]);
      return e;
   }

   public static void setCurrentDirectory(File dir) {
      if (!dir.isDirectory()) {
         throw new IllegalArgumentException("Must use a directory as a parameter.");
      } else {
         LAST_DIR = dir;
      }
   }
}
