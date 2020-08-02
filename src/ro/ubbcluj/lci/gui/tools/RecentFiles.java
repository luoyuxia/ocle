package ro.ubbcluj.lci.gui.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import ro.ubbcluj.lci.gui.Actions.AFileActions;
import ro.ubbcluj.lci.gui.Actions.AProjectActions;
import ro.ubbcluj.lci.gui.Actions.AUMLModelActions;
import ro.ubbcluj.lci.gui.mainframe.GApplication;
import ro.ubbcluj.lci.gui.mainframe.GMainFrame;

public class RecentFiles {
   private static int MAX_NR = 5;
   public static String PROJECT_FILES;
   public static String MODEL_FILES;
   public static String OCL_FILES;
   private static String[] PROJECT_FILTERS;
   private static String[] MODEL_FILTERS;
   private static String[] OCL_FILTERS;
   private static String file;
   private static RecentFiles instance;
   private ArrayList files;
   private ArrayList dates;
   private static String[] filters;

   public static RecentFiles getInstance() {
      if (instance == null) {
         instance = new RecentFiles();
      }

      return instance;
   }

   private RecentFiles() {
      this.files = new ArrayList(MAX_NR);
      this.dates = new ArrayList(MAX_NR);
      this.initArrays();
      if (!isFileEmpty()) {
         this.initFromFile();
      }

   }

   private void initArrays() {
      for(int i = 0; i < MAX_NR; ++i) {
         this.files.add(new String(""));
         this.dates.add(new Long(0L));
      }

   }

   private static boolean isFileEmpty() {
      try {
         String dirName = System.getProperty("user.home") + System.getProperty("file.separator") + GApplication.APP_CONF_DIR;
         File dirPropsFile = new File(dirName);
         if (!dirPropsFile.exists() && !dirPropsFile.mkdir()) {
            System.err.println("Could not create properties directory " + dirName);
            return true;
         }

         RandomAccessFile raf = new RandomAccessFile(file, "rw");
         if (raf.length() > 0L) {
            raf.close();
            return false;
         }

         raf.close();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return true;
   }

   public static boolean isAccepted(String filename) {
      for(int i = 0; i < filters.length; ++i) {
         if (filename.endsWith(filters[i].trim())) {
            return true;
         }
      }

      return false;
   }

   public static void setFilters(String[] newFilters) {
      filters = newFilters;
   }

   public static void setFile(String filename) {
      if (filename.equals(PROJECT_FILES)) {
         file = PROJECT_FILES;
         filters = PROJECT_FILTERS;
      } else if (filename.equals(MODEL_FILES)) {
         file = MODEL_FILES;
         filters = MODEL_FILTERS;
      } else if (filename.equals(OCL_FILES)) {
         file = OCL_FILES;
         filters = OCL_FILTERS;
      }
   }

   private void initToFile() {
      try {
         RandomAccessFile fd = new RandomAccessFile(file, "rw");

         for(int i = 0; i < MAX_NR; ++i) {
            fd.writeUTF((String)this.files.get(i));
            fd.writeLong(((Long)this.dates.get(i)).longValue());
         }

         fd.close();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   private void initFromFile() {
      try {
         this.files.removeAll(this.files);
         this.dates.removeAll(this.dates);
         RandomAccessFile fd = new RandomAccessFile(file, "rw");

         for(int i = 0; i < MAX_NR; ++i) {
            if (fd.getFilePointer() < fd.length()) {
               String s = fd.readUTF();
               Long l = new Long(fd.readLong());
               File test = new File(s);
               if (test.exists()) {
                  this.files.add(s);
                  this.dates.add(l);
               } else {
                  this.files.add(new String(""));
                  this.dates.add(new Long(0L));
               }
            }
         }

         fd.close();
         if (this.files.size() == 0) {
            this.initArrays();
         }
      } catch (Exception var6) {
         System.out.println("Exception in:" + this.getClass().getName() + ":" + var6.getLocalizedMessage());
      }

   }

   private boolean checkExtension(String file_name) {
      for(int i = 0; i < filters.length; ++i) {
         if (file_name.endsWith(filters[i]) || file_name.equals("")) {
            return true;
         }
      }

      return false;
   }

   public void checkFile(String file_name) {
      this.initFromFile();
      if (this.files.contains(file_name)) {
         if (this.files.indexOf(file_name) != 0) {
            this.files.remove(file_name);
            this.files.add(0, file_name);
            this.initToFile();
         }

      } else {
         long cur_time = (new Date()).getTime();
         long fd = 0L;
         long flag = cur_time;
         int pos = -1;

         for(int i = 0; i < this.dates.size(); ++i) {
            if (((Long)this.dates.get(i)).longValue() < flag) {
               flag = ((Long)this.dates.get(i)).longValue();
               pos = i;
            }
         }

         if (pos != -1) {
            if (this.dates.size() == 5) {
               this.files.remove(pos);
               this.files.add(0, file_name);
               this.dates.remove(pos);
               this.dates.add(0, new Long(cur_time));
            } else {
               this.files.add(0, file_name);
               this.dates.add(0, new Long(cur_time));
            }

            this.initToFile();
         }
      }
   }

   public JMenu buildMenu(String filetype) {
      setFile(filetype);
      JMenu menu = new JMenu("Open recent");
      menu.setIcon(new ImageIcon(this.getClass().getResource("/resources/null.gif")));
      this.initFromFile();

      for(int i = 0; i < this.files.size(); ++i) {
         String fn = (String)this.files.get(i);
         if (!fn.equals("")) {
            int ind = i + 1;
            JMenuItem item = new JMenuItem(ind + ") " + fn);
            item.setActionCommand(fn);
            item.addActionListener(new RecentFiles.RecentFilesActionListener());
            menu.add(item);
         }
      }

      return menu;
   }

   public ArrayList getMenuItems(String filetype) {
      setFile(filetype);
      ArrayList result = new ArrayList();
      this.initFromFile();

      for(int i = 0; i < this.files.size(); ++i) {
         String fn = (String)this.files.get(i);
         if (!fn.equals("")) {
            int ind = i + 1;
            JMenuItem item = new JMenuItem(ind + ") " + fn);
            item.setActionCommand(fn);
            item.addActionListener(new RecentFiles.RecentFilesActionListener());
            result.add(item);
         }
      }

      return result;
   }

   static {
      PROJECT_FILES = System.getProperty("user.home") + System.getProperty("file.separator") + GApplication.APP_CONF_DIR + System.getProperty("file.separator") + "project.recent";
      MODEL_FILES = System.getProperty("user.home") + System.getProperty("file.separator") + GApplication.APP_CONF_DIR + System.getProperty("file.separator") + "model.recent";
      OCL_FILES = System.getProperty("user.home") + System.getProperty("file.separator") + GApplication.APP_CONF_DIR + System.getProperty("file.separator") + "ocl.recent";
      PROJECT_FILTERS = new String[]{".oepr"};
      MODEL_FILTERS = new String[]{"xmi", "xml", "xml.zip"};
      OCL_FILTERS = new String[]{".ocl"};
      file = PROJECT_FILES;
      instance = null;
      filters = PROJECT_FILTERS;
   }

   class RecentFilesActionListener implements ActionListener {
      RecentFilesActionListener() {
      }

      public void actionPerformed(ActionEvent e) {
         String filename = e.getActionCommand();
         if (filename.endsWith(".oepr")) {
            AProjectActions.OpenProjectAction apo = (AProjectActions.OpenProjectAction)AProjectActions.openProjectAction;
            apo.setFile(new File(filename));
            apo.actionPerformed(e);
         } else if (!filename.endsWith(".xml") && !filename.endsWith("xml.zip") && !filename.endsWith("xmi")) {
            if (filename.endsWith(".ocl")) {
               ((AFileActions.OpenFileAction)AFileActions.openFileAction).openFile(new File(filename));
            } else {
               GMainFrame.getMainFrame().updateMessages((Object)"Unknown format ! ");
            }
         } else {
            AUMLModelActions.OpenUMLAction aou = (AUMLModelActions.OpenUMLAction)AUMLModelActions.openUmlAction;
            aou.setFile(new File(filename));
            aou.actionPerformed(e);
         }
      }
   }
}
