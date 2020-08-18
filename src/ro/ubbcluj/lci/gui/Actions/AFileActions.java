package ro.ubbcluj.lci.gui.Actions;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import ro.ubbcluj.lci.gui.dialogs.ChooseModelAndOCLDialog;
import ro.ubbcluj.lci.gui.editor.AbstractPad;
import ro.ubbcluj.lci.gui.editor.Editor;
import ro.ubbcluj.lci.gui.editor.TextDocumentPad;
import ro.ubbcluj.lci.gui.mainframe.GApplication;
import ro.ubbcluj.lci.gui.mainframe.GMainFrame;
import ro.ubbcluj.lci.gui.mainframe.ProjectManager;
import ro.ubbcluj.lci.gui.tools.AFileFilter;
import ro.ubbcluj.lci.gui.tools.RecentFiles;
import ro.ubbcluj.lci.gui.wizards.NewProjectWizard;

public class AFileActions {
   public static Action newFileAction = new AFileActions.NewFileAction();
   public static Action openFileAction = new AFileActions.OpenFileAction();
   public static Action closeFileAction = new AFileActions.CloseFileAction();
   public static Action closeAllAction = new AFileActions.CloseAllAction();
   public static Action saveFileAction = new AFileActions.SaveFileAction();
   public static Action saveFileAsAction = new AFileActions.SaveFileAsAction();
   public static Action saveAllAction = new AFileActions.SaveAllAction();
   public static Action exitAction = new AFileActions.ExitAction();
   public static Action chooseFileAction = new AFileActions.ChooseFileAction();

   public AFileActions() {
   }

   public static Action[] getActions() {
      Action[] actions = new Action[]{newFileAction, openFileAction,
              closeFileAction, closeAllAction,
              saveFileAction, saveFileAsAction, saveAllAction,
              chooseFileAction,
              exitAction};
      return actions;
   }

   public static class ExitAction extends AbstractAction {
      private ExitAction() {
         this.putValue("ActionCommandKey", "menubar.file.exit");
      }

      public void actionPerformed(ActionEvent e) {
         GApplication.getApplication().exit(0);
      }
   }

   public static class SaveAllAction extends AbstractAction {
      public SaveAllAction() {
         this.putValue("ActionCommandKey", "menubar.file.saveAll");
      }

      public void actionPerformed(ActionEvent evt) {
         Editor ed = GApplication.getApplication().getEditor();
         AFileFilter ff = new AFileFilter("ocl", "OCL text files (*.ocl)");
         AFileFilter ff2 = new AFileFilter("bcr", "OCL text files (*.bcr)");
         ed.addFileFilter(ff);
         ed.addFileFilter(ff2);
         ed.saveAll();
         ed.removeFileFilter(ff);
         ed.removeFileFilter(ff2);
      }
   }

   public static class SaveFileAsAction extends AbstractAction {
      private SaveFileAsAction() {
         this.putValue("ActionCommandKey", "menubar.file.saveFileAs");
      }

      public void actionPerformed(ActionEvent ev) {
         AFileFilter[] ff = new AFileFilter[]{new AFileFilter("ocl", "OCL text files (*.ocl)"), new AFileFilter("bcr", "OCL text files (*.bcr)")};
         File f = AFileFilter.chooseFile(1, GMainFrame.getMainFrame().getViewContainer().getComponent(), ff);
         if (f != null) {
            int f2 = GApplication.getApplication().getEditor().saveActiveFileAs(f);
            if (f2 == -104) {
               GMainFrame.getMainFrame().updateLog("File " + f.getAbsolutePath() + " written.\n");
            } else {
               GMainFrame.getMainFrame().updateLog("File " + f.getAbsolutePath() + " could not be written.\n");
            }
         }

      }
   }

   public static class SaveFileAction extends AbstractAction {
      private SaveFileAction() {
         this.putValue("ActionCommandKey", "menubar.file.saveFile");
         this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(83, 2));
         this.setEnabled(false);
      }

      public void actionPerformed(ActionEvent evt) {
         Editor ed = GApplication.getApplication().getEditor();
         TextDocumentPad activePad = (TextDocumentPad)ed.getActivePad();
         if (!activePad.getModel().isAssigned()) {
            AFileActions.saveFileAsAction.actionPerformed(evt);
         } else {
            int f = ed.saveActiveFile();
            if (f == -104) {
               String f2 = activePad.getModel().getFileName();
               GMainFrame.getMainFrame().updateLog("File " + f2 + " written.\n");
            } else {
               GMainFrame.getMainFrame().updateLog("File could not be written.\n");
            }
         }

      }
   }

   public static class CloseAllAction extends AbstractAction {
      private CloseAllAction() {
         this.putValue("ActionCommandKey", "menubar.file.closeAll");
      }

      public void actionPerformed(ActionEvent evt) {
         Editor ed = GApplication.getApplication().getEditor();
         AFileFilter filter = new AFileFilter("ocl", "OCL text files(*.ocl)");
         ed.addFileFilter(filter);
         ed.getView().closeAll();
         ed.removeFileFilter(filter);
      }
   }

   public static class CloseFileAction extends AbstractAction {
      private CloseFileAction() {
         this.putValue("ActionCommandKey", "menubar.file.closeFile");
         this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(115, 2));
      }

      public void actionPerformed(ActionEvent evt) {
         if (this.isEnabled()) {
            Editor ed = GApplication.getApplication().getEditor();
            AbstractPad pad = ed.getView().getActivePad();
            AFileFilter ff = new AFileFilter("ocl", "OCL text files (*.ocl)");
            ed.addFileFilter(ff);
            ed.getView().removePadView(pad);
            ed.removeFileFilter(ff);
         }
      }
   }

   public static class OpenFileAction extends AbstractAction {
      private OpenFileAction() {
         this.putValue("ActionCommandKey", "menubar.file.openFile");
         this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(79, 2));
      }

      public void actionPerformed(ActionEvent ev) {
         AFileFilter[] ff = new AFileFilter[]{new AFileFilter("java", "Java files (*.java)"), new AFileFilter("ocl", "OCL text files (*.ocl)"), new AFileFilter("bcr", "OCL text files (*.bcr)")};
         File f = AFileFilter.chooseFile(0, GApplication.getApplication().getEditor().getView().getComponent(), ff);
         if (f != null) {
            this.openFile(f);
         }

      }

      public void openFile(File f) {
         AFileActions.OpenFileAction.OpenFileThread thr = new AFileActions.OpenFileAction.OpenFileThread(f);
         thr.start();
      }

      private class OpenFileThread extends Thread {
         private File file;

         public OpenFileThread(File f) {
            this.file = f;
         }

         public void run() {
            GApplication app = GApplication.getApplication();
            File f2 = app.getEditor().openFile(this.file);
            if (f2 != null) {
               RecentFiles fh = RecentFiles.getInstance();
               RecentFiles.setFile(RecentFiles.OCL_FILES);
               String selectedFile = f2.getAbsolutePath();
               if (RecentFiles.isAccepted(selectedFile)) {
                  fh.checkFile(selectedFile);
               }

               GMainFrame.getMainFrame().updateLog("Opened file " + f2.getAbsolutePath() + ".\n");
            } else {
               GMainFrame.getMainFrame().updateLog("File " + this.file.getAbsolutePath() + " could not be opened.\n");
            }

         }
      }
   }

   public static class NewFileAction extends AbstractAction {
      private NewFileAction() {
         this.putValue("ActionCommandKey", "menubar.file.newFile");
         this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(78, 2));
      }

      public void actionPerformed(ActionEvent ev) {
         GMainFrame.getMainFrame().clearErrorMessages();
         GApplication.getApplication().getEditor().newFile();
      }
   }

   public static class ChooseFileAction extends AbstractAction  {
      private ChooseFileAction() {
         this.putValue("ActionCommandKey", "menubar.file.chooseFile");
         this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(76, 2));
      }
      public void actionPerformed(ActionEvent ev) {
         (new Thread() {
            public void run() {
               ChooseModelAndOCLDialog modelAndOCLDialog = new ChooseModelAndOCLDialog(GApplication.frame);
            //   modelAndOCLDialog.show();
            //   ProjectManager.getInstance().newProject(wizard.getProjectKind(), wizard.getProjectName(), wizard.getProjectFileName(), wizard.getXmlFiles(), wizard.getOclFiles());
            }
         }).start();
      }
   }

}
