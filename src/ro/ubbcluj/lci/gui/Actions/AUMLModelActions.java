package ro.ubbcluj.lci.gui.Actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.StringTokenizer;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import ro.ubbcluj.lci.gui.mainframe.GApplication;
import ro.ubbcluj.lci.gui.mainframe.GMainFrame;
import ro.ubbcluj.lci.gui.mainframe.GRepository;
import ro.ubbcluj.lci.gui.mainframe.GUMLModel;
import ro.ubbcluj.lci.gui.mainframe.ProjectManager;
import ro.ubbcluj.lci.gui.tools.AFileFilter;
import ro.ubbcluj.lci.gui.tools.ProgressBar;
import ro.ubbcluj.lci.ocl.ImportBoldObjects;
import ro.ubbcluj.lci.ocl.ImportJar;
import ro.ubbcluj.lci.ocl.ImportUseObjects;
import ro.ubbcluj.lci.redtd.DTDToUMLFacade;
import ro.ubbcluj.lci.redtd.UMLTransformer;

public class AUMLModelActions {
   public static Action newUmlAction = new AUMLModelActions.NewUMLAction();
   public static Action openUmlAction = new AUMLModelActions.OpenUMLAction();
   public static Action closeUmlAction = new AUMLModelActions.CloseUMLAction();
   public static Action saveUmlAction = new AUMLModelActions.SaveUMLAction();
   public static Action saveUmlAsAction = new AUMLModelActions.SaveUMLAsAction();
   public static Action importJarAction = new AUMLModelActions.ImportJarAction();
   public static Action importJDKAction = new AUMLModelActions.ImportJDKAction();
   public static Action importBoldAction = new AUMLModelActions.ImportBoldAction();
   public static Action importUseAction = new AUMLModelActions.ImportUseAction();
   public static Action reverseDTDAction = new AUMLModelActions.ReverseDTDAction();
   public static Action saveDTDAction = new AUMLModelActions.SaveDTDAction();

   public AUMLModelActions() {
   }

   public static Action[] getActions() {
      Action[] actions = new Action[]{newUmlAction, openUmlAction, closeUmlAction, saveUmlAction, saveUmlAsAction, importJarAction, importJDKAction, importBoldAction, importUseAction, reverseDTDAction, saveDTDAction};
      return actions;
   }

   private static int promptSaveOldModel() {
      GUMLModel usermodel = GRepository.getInstance().getUsermodel();
      if (usermodel != null && usermodel.isDirty()) {
         String text = "Save model " + usermodel.getModel().getName() + " before closing?";
         int choice = JOptionPane.showConfirmDialog(GMainFrame.getMainFrame(), text, "Warning", 1, 2);
         return choice;
      } else {
         return 1;
      }
   }

   public static void saveUML(File modelFile) {
      ProgressBar progressBar = ProgressBar.getProgressBar(GApplication.frame);

      try {
         progressBar.setColor(ProgressBar.SAVE_COLOR);
         progressBar.setIncrement(-1);
         progressBar.startOld("Saving UML model...");
         ProjectManager.getInstance().saveUMLModel(modelFile);
      } catch (Exception var6) {
         var6.printStackTrace();
         GMainFrame.getMainFrame().updateMessages((Object)var6.getLocalizedMessage());
      } finally {
         progressBar.stopOld();
      }

   }

   public static void openUML(File modelFile) {
      GMainFrame mainFrame = GMainFrame.getMainFrame();
      mainFrame.clearErrorMessages();
      ProgressBar progressBar = ProgressBar.getProgressBar(GApplication.frame);

      try {
         progressBar.setColor(ProgressBar.OPEN_COLOR);
         progressBar.startProgress("Opening UML model..");
         ProjectManager projectManager = ProjectManager.getInstance();
         projectManager.closeUMLModel();
         projectManager.openUMLModel(modelFile);
      } catch (Exception var7) {
         var7.printStackTrace();
         mainFrame.updateMessages((Object)var7.getLocalizedMessage());
      } finally {
         progressBar.stopProgress();
      }

   }

   private static class SaveDTDThread extends Thread {
      private File selectedFile;

      protected SaveDTDThread(File file) {
         this.selectedFile = file;
      }

      public void run() {
         synchronized(AUMLModelActions.SaveDTDThread.class) {
            try {
               UMLTransformer.getInstance(0).transformModel(GRepository.getInstance().getUsermodel().getModel(), this.selectedFile);
            } catch (Exception var9) {
               var9.printStackTrace();
               GMainFrame.getMainFrame().updateMessages((Object)var9.getLocalizedMessage());
            } finally {
               GMainFrame.getMainFrame().updateModelActions();
               GMainFrame.getMainFrame().updateLog("UML model saved as DTD in " + this.selectedFile.getAbsolutePath() + ".\n");
            }

         }
      }
   }

   public static class ReDTDThread extends Thread {
      private ProgressBar pb;
      private File selectedFile;

      public ReDTDThread(File file) {
         this.selectedFile = file;
         this.pb = ProgressBar.getProgressBar(GApplication.frame);
      }

      public void run() {
         synchronized(AUMLModelActions.ReDTDThread.class) {
            GMainFrame mainFrame = GMainFrame.getMainFrame();
            mainFrame.clearErrorMessages();

            try {
               this.pb.setColor(ProgressBar.OPEN_COLOR);
               this.pb.setIncrement(-1);
               this.pb.disableProgressString();
               this.pb.startOld("Reversing DTD model..");
               GRepository repo = GRepository.getInstance();
               GUMLModel model = repo.getUsermodel();
               String dtdFilename = this.selectedFile.getName();
               if (model == null) {
                  model = repo.newUserModel((String)null);
                  repo.changeUsermodel(model);
                  mainFrame.updateModelActions();
                  model.getModel().setName(dtdFilename.substring(0, dtdFilename.indexOf(46)));
               }

               if (!dtdFilename.toLowerCase().endsWith(".dtd") && !dtdFilename.toLowerCase().endsWith(".xml")) {
                  GMainFrame.getMainFrame().updateLog("The specified file (" + this.selectedFile.getAbsolutePath() + ") should have dtd or xml " + "extension");
               } else {
                  long time1 = System.currentTimeMillis();
                  (new DTDToUMLFacade()).reDTD(this.selectedFile);
                  long time2 = System.currentTimeMillis();
                  GMainFrame.getMainFrame().updateLog("DTD model for " + this.selectedFile.getName() + " loaded in " + (time2 - time1) + " miliseconds.\n");
               }

               repo.getUsermodel().getBrowser().refresh();
            } catch (Exception var15) {
               var15.printStackTrace();
               mainFrame.updateMessages((Object)var15.getMessage());
            } finally {
               this.pb.stopOld();
            }

         }
      }
   }

   public static class SaveDTDAction extends AbstractAction {
      private SaveDTDAction() {
         this.putValue("ActionCommandKey", "menubar.umlmodel.saveDTD");
      }

      public void actionPerformed(ActionEvent e) {
         try {
            AFileFilter[] ff = new AFileFilter[]{new AFileFilter("dtd", "DTD files (*.dtd)")};
            File file = AFileFilter.chooseFile(1, GMainFrame.getMainFrame(), ff, "Save DTD");
            if (file != null) {
               (new AUMLModelActions.SaveDTDThread(file)).start();
            }
         } catch (Exception var4) {
            var4.printStackTrace();
         }

      }
   }

   public static class ReverseDTDAction extends AbstractAction {
      private ReverseDTDAction() {
         this.putValue("ActionCommandKey", "menubar.umlmodel.reverseDTD");
      }

      public void actionPerformed(ActionEvent evt) {
         File file = ActionUtilities.chooseFileForOpen("dtd,xml", "DTD and XML files (*.dtd, *.xml)", "Select an external DTD file or an XML file with internal DTD");
         if (file != null) {
            (new AUMLModelActions.ReDTDThread(file)).start();
         }

      }
   }

   public static class ImportUseAction extends AbstractAction {
      private ImportUseAction() {
         this.putValue("ActionCommandKey", "menubar.umlmodel.importUse");
      }

      public void actionPerformed(ActionEvent evt) {
         AFileFilter ff = new AFileFilter("cmd", "CMD files (*.cmd)");
         File f = AFileFilter.chooseFile(0, GMainFrame.getMainFrame(), new AFileFilter[]{ff});
         if (f != null) {
            ImportUseObjects uoi = new ImportUseObjects(GRepository.getInstance().getUsermodel().getModel(), f.getAbsolutePath());
            uoi.addObjects();
            GRepository.getInstance().getUsermodel().getBrowser().refresh();
         }

      }
   }

   public static class ImportBoldAction extends AbstractAction {
      private ImportBoldAction() {
         this.putValue("ActionCommandKey", "menubar.umlmodel.importBold");
      }

      public void actionPerformed(ActionEvent evt) {
         AFileFilter ff = new AFileFilter("xml", "BoldSoft XML files (*.xml)");
         File f = AFileFilter.chooseFile(0, GMainFrame.getMainFrame(), new AFileFilter[]{ff});
         if (f != null) {
            ImportBoldObjects mdpo = new ImportBoldObjects(GRepository.getInstance().getUsermodel().getModel(), f.getAbsolutePath());
            mdpo.addObjects();
            GRepository.getInstance().getUsermodel().getBrowser().refresh();
         }

      }
   }

   public static class ImportJDKAction extends AbstractAction {
      private ImportJDKAction() {
         this.putValue("ActionCommandKey", "menubar.umlmodel.importJDK");
      }

      public void actionPerformed(ActionEvent evt) {
         String bootPath = System.getProperty("sun.boot.class.path");
         StringTokenizer tok = new StringTokenizer(bootPath, ";");

         while(tok.hasMoreTokens()) {
            final String jar = tok.nextToken();
            if (jar.endsWith("\\rt.jar") || jar.endsWith("/rt.jar")) {
               (new Thread() {
                  public void run() {
                     ImportJar.importJar(jar, GRepository.getInstance().getUsermodel().getModel(), false);
                     GRepository.getInstance().getUsermodel().getBrowser().refresh();
                  }
               }).start();
               break;
            }
         }

      }
   }

   public static class ImportJarAction extends AbstractAction {
      private ImportJarAction() {
         this.putValue("ActionCommandKey", "menubar.umlmodel.importJar");
      }

      public void actionPerformed(ActionEvent evt) {
         AFileFilter ff = new AFileFilter("jar,class", "JAR archives and .class files(*.jar, *.class)");
         final File f = AFileFilter.chooseFile(0, GMainFrame.getMainFrame(), new AFileFilter[]{ff});
         if (f != null) {
            if (f.getName().endsWith(".class")) {
               ImportJar.importClass(f.getAbsolutePath(), GRepository.getInstance().getUsermodel().getModel());
               GRepository.getInstance().getUsermodel().getBrowser().refresh();
            } else {
               (new Thread() {
                  public void run() {
                     ImportJar.importJar(f.getAbsolutePath(), GRepository.getInstance().getUsermodel().getModel(), true);
                     GRepository.getInstance().getUsermodel().getBrowser().refresh();
                  }
               }).start();
            }
         }

      }
   }

   public static class SaveUMLAction extends AbstractAction {
      private SaveUMLAction() {
         this.putValue("ActionCommandKey", "menubar.umlmodel.saveUml");
      }

      public void actionPerformed(ActionEvent e) {
         final GUMLModel userModel = GRepository.getInstance().getUsermodel();
         String modelFileName = userModel.getModelFileName();
         final File modelFile = ActionUtilities.getFileForSave(modelFileName, "xml.zip", "Packed XML files (*.xml.zip)", "Save UML model");
         (new Thread() {
            public void run() {
               if (userModel.isDirty()) {
                  AUMLModelActions.saveUML(modelFile);
                  GMainFrame.getMainFrame().updateModelActions();
               }

            }
         }).start();
      }
   }

   public static class SaveUMLAsAction extends AbstractAction {
      private SaveUMLAsAction() {
         this.putValue("ActionCommandKey", "menubar.umlmodel.saveUmlAs");
      }

      public void actionPerformed(ActionEvent e) {
         final File modelFile = ActionUtilities.chooseFileForSave("xml.zip", "Packed XML files (*.xml.zip)", "Save UML model");
         if (modelFile != null) {
            (new Thread() {
               public void run() {
                  ProjectManager.getInstance().saveUMLModelAs(modelFile);
                  GMainFrame.getMainFrame().updateModelActions();
               }
            }).start();
         }

      }
   }

   private static class CloseUMLAction extends AbstractAction {
      CloseUMLAction() {
         this.putValue("ActionCommandKey", "menubar.umlmodel.closeUml");
      }

      public void actionPerformed(ActionEvent e) {
         int choice = AUMLModelActions.promptSaveOldModel();
          File file1 = null;
         final GUMLModel userModel = GRepository.getInstance().getUsermodel();
         if (choice == 0) {
            String modelFileName = userModel.getModelFileName();
            file1 = ActionUtilities.getFileForSave(modelFileName, "xml.zip", "Packed XML files (*.xml.zip)", "Save UML model");
         }
         final File file = file1;

         (new Thread() {
            public void run() {
               if (file != null && userModel.isDirty()) {
                  AUMLModelActions.saveUML(file);
               }

               try {
                  ProjectManager.getInstance().closeUMLModel();
               } catch (Exception var2) {
                  var2.printStackTrace();
                  GMainFrame.getMainFrame().updateMessages((Object)var2.getLocalizedMessage());
               }

            }
         }).start();
      }
   }

   public static class OpenUMLAction extends AbstractAction {
      private File file;

      private OpenUMLAction() {
         this.putValue("ActionCommandKey", "menubar.umlmodel.openUml");
      }

      public void actionPerformed(ActionEvent e) {
         if (this.file == null || !this.file.exists()) {
            this.file = ActionUtilities.chooseFileForOpen("xml,xml.zip", "XML and packed XML files (*.xml, *.xml.zip)", "Open UML model");
         }

         if (this.file != null && this.file.exists()) {
            ProjectManager.getInstance().updateModelRecentFiles(this.file);
            final File modelFile = this.file;
            (new Thread() {
               public void run() {
                  GUMLModel userModel = GRepository.getInstance().getUsermodel();
                  if (userModel != null && userModel.isDirty()) {
                     int choice = AUMLModelActions.promptSaveOldModel();
                     File oldModelFile = null;
                     if (choice == 0) {
                        String modelFileName = userModel.getModelFileName();
                        oldModelFile = ActionUtilities.getFileForSave(modelFileName, "xml.zip", "Packed XML files (*.xml.zip)", "Save UML model");
                     }

                     AUMLModelActions.saveUML(oldModelFile);
                  }

                  AUMLModelActions.openUML(modelFile);
               }
            }).start();
         } else if (this.file != null) {
            GMainFrame.getMainFrame().updateMessages((Object)("File " + this.file.getAbsolutePath() + " could not be loaded."));
         }

         this.file = null;
      }

      public void setFile(File file) {
         this.file = file;
      }
   }

   public static class NewUMLAction extends AbstractAction {
      private NewUMLAction() {
         this.putValue("ActionCommandKey", "menubar.umlmodel.newUml");
      }

      public void actionPerformed(ActionEvent e) {
         int choice = AUMLModelActions.promptSaveOldModel();
         File file1 = null;
         final GUMLModel userModel = GRepository.getInstance().getUsermodel();
         if (choice == 0) {
            String modelFileName = userModel.getModelFileName();
            file1 = ActionUtilities.getFileForSave(modelFileName, "xml.zip", "Packed XML files (*.xml.zip)", "Save UML model");
         }
         final File file = file1;

         (new Thread() {
            public void run() {
               if (file != null && userModel.isDirty()) {
                  AUMLModelActions.saveUML(file);
               }

               try {
                  ProjectManager projectManager = ProjectManager.getInstance();
                  projectManager.closeUMLModel();
                  projectManager.newUMLModel();
               } catch (Exception var2) {
                  var2.printStackTrace();
                  GMainFrame.getMainFrame().updateMessages((Object)var2.getLocalizedMessage());
               }

            }
         }).start();
      }
   }
}
