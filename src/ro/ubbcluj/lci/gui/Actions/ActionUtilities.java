package ro.ubbcluj.lci.gui.Actions;

import java.io.File;
import ro.ubbcluj.lci.gui.mainframe.GMainFrame;
import ro.ubbcluj.lci.gui.tools.AFileFilter;

public class ActionUtilities {
   public ActionUtilities() {
   }

   public static File getFileForSave(String fileName, String fileExtension, String description, String chooserTitle) {
      File modelFile = null;
      if (fileName == null) {
         modelFile = chooseFileForSave(fileExtension, description, chooserTitle);
      } else {
         modelFile = new File(fileName);
      }

      return modelFile;
   }

   public static File chooseFileForSave(String fileExtension, String description, String chooserTitle) {
      AFileFilter[] ff = new AFileFilter[]{new AFileFilter(fileExtension, description)};
      return AFileFilter.chooseFile(1, GMainFrame.getMainFrame(), ff, chooserTitle);
   }

   public static File chooseFileForOpen(String fileExtension, String description, String chooserTitle) {
      AFileFilter[] ff = new AFileFilter[]{new AFileFilter(fileExtension, description)};
      return AFileFilter.chooseFile(0, GMainFrame.getMainFrame(), ff, chooserTitle);
   }
}
