package ro.ubbcluj.lci.gui.Actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import ro.ubbcluj.lci.gui.browser.FBrowser;
import ro.ubbcluj.lci.gui.browser.FTreeNode;
import ro.ubbcluj.lci.gui.mainframe.GAbstractProject;
import ro.ubbcluj.lci.gui.mainframe.GApplication;
import ro.ubbcluj.lci.gui.mainframe.GMainFrame;
import ro.ubbcluj.lci.gui.mainframe.GRepository;
import ro.ubbcluj.lci.gui.mainframe.GUMLModel;
import ro.ubbcluj.lci.gui.mainframe.ProjectManager;
import ro.ubbcluj.lci.gui.tools.ProgressBar;
import ro.ubbcluj.lci.gui.wizards.AddFilesToProjectWizard;
import ro.ubbcluj.lci.gui.wizards.NewProjectWizard;
import ro.ubbcluj.lci.gui.wizards.Wizard;

public class AProjectActions {
   public static Action newProjectAction = new AProjectActions.NewProjectAction();
   public static Action openProjectAction = new AProjectActions.OpenProjectAction();
   public static Action closeProjectAction = new AProjectActions.CloseProjectAction();
   public static Action saveProjectAction = new AProjectActions.SaveProjectAction();
   public static Action saveProjectAsAction = new AProjectActions.SaveProjectAsAction();
   public static Action addFilesAction = new AProjectActions.AddFilesAction();
   public static Action removeFileAction = new AProjectActions.RemoveFileAction();

   public AProjectActions() {
   }

   public static Action[] getActions() {
      return new Action[]{newProjectAction, openProjectAction, saveProjectAction, saveProjectAsAction, closeProjectAction, addFilesAction, removeFileAction};
   }

   private static int promptSaveOldProject() {
      GAbstractProject project = GRepository.getInstance().getProject();
      if (project == null) {
         return 1;
      } else if (project.isDirty() || project.getActiveModel() != null && project.getActiveModel().isDirty()) {
         String question = "Save project " + project.getProjectName() + " before closing?";
         int choice = JOptionPane.showConfirmDialog(GMainFrame.getMainFrame(), question, "Confirmation", 1, 3);
         return choice;
      } else {
         return 1;
      }
   }

   public static void saveProject(File projectFile) {
      GRepository repository = GRepository.getInstance();
      GUMLModel userModel = repository.getUsermodel();
      if (userModel != null && (userModel.isDirty() || userModel.getDiagrams().size() > 0) && userModel.getOwnerProject() == repository.getProject()) {
         AUMLModelActions.saveUML(new File(userModel.getModelFileName()));
      }

      ProgressBar progressBar = ProgressBar.getProgressBar(GApplication.frame);
      GMainFrame mainframe = GMainFrame.getMainFrame();

      try {
         progressBar.setColor(ProgressBar.SAVE_COLOR);
         progressBar.setIncrement(-1);
         progressBar.startOld("Saving project...");
         ProjectManager.getInstance().saveProject(projectFile);
      } catch (IOException var10) {
         mainframe.updateLog("Could not save project: " + var10.getMessage());
      } catch (Exception var11) {
         var11.printStackTrace();
         GMainFrame.getMainFrame().updateMessages((Object)var11.getLocalizedMessage());
      } finally {
         progressBar.stopOld();
         GMainFrame.getMainFrame().updateProjectActions();
      }

   }

   public static void closeProject(File projectFile) {
      if (projectFile != null) {
         saveProject(projectFile);
      }

      try {
         ProjectManager.getInstance().closeProject();
      } catch (Exception var2) {
         var2.printStackTrace();
         GMainFrame.getMainFrame().updateMessages((Object)var2.getLocalizedMessage());
      }

   }

   public static void openProject(File projectFile) {
      GMainFrame mainframe = GMainFrame.getMainFrame();
      mainframe.clearErrorMessages();
      ProgressBar progressBar = ProgressBar.getProgressBar(GApplication.frame);

      try {
         if (projectFile.getAbsolutePath().lastIndexOf(".oepr") < 0) {
            mainframe.updateMessages((Object)"This is not an OCLE project!");
            return;
         }

         progressBar.setColor(ProgressBar.OPEN_COLOR);
         progressBar.startProgress("Opening project...");
         ProjectManager.getInstance().openProject(projectFile);
      } catch (Exception var7) {
         var7.printStackTrace();
         mainframe.updateMessages((Object)var7.getLocalizedMessage());
      } finally {
         progressBar.stopProgress();
      }

   }

   private static class RemoveFileAction extends AbstractAction {
      public RemoveFileAction() {
         this.putValue("ActionCommandKey", "menubar.project.removeselection");
      }

      public void actionPerformed(ActionEvent e) {
         GAbstractProject activeProject = GRepository.getInstance().getProject();
         FBrowser browser = activeProject.getFileBrowser();
         FTreeNode tn = browser.getSelectedNode();
         if (tn == null) {
            JOptionPane.showMessageDialog(GApplication.frame, "Please select a node in the project browser.", GApplication.APP_NAME, 1);
         } else {
            browser.removeNode(tn);
            activeProject.setDirty(true);
            GMainFrame.getMainFrame().updateProjectActions();
         }

      }
   }

   private static class AddFilesAction extends AbstractAction {
      public AddFilesAction() {
         this.putValue("ActionCommandKey", "menubar.project.addfiles");
      }

      public void actionPerformed(ActionEvent e) {
         Wizard aftpw = new AddFilesToProjectWizard(GApplication.frame, true, true);
         aftpw.runWizard();
         GMainFrame.getMainFrame().updateEvaluationActions();
         GMainFrame.getMainFrame().updateProjectActions();
      }
   }

   private static class SaveProjectAsAction extends AbstractAction {
      public SaveProjectAsAction() {
         this.putValue("ActionCommandKey", "menubar.project.saveProjectAs");
      }

      public void actionPerformed(ActionEvent e) {
         final File file = ActionUtilities.chooseFileForSave("oepr", "Ocle projects (*.oepr)", "Save project");
         if (file != null) {
            (new Thread() {
               public void run() {
                  AProjectActions.saveProject(file);
               }
            }).start();
         }

      }
   }

   private static class SaveProjectAction extends AbstractAction {
      public SaveProjectAction() {
         this.putValue("ActionCommandKey", "menubar.project.saveProject");
      }

      public void actionPerformed(ActionEvent e) {
         final File file = ActionUtilities.getFileForSave(GRepository.getInstance().getProject().getProjectFile().getAbsolutePath(), "oepr", "OCLE projects (*.oepr)", "Save project");
         if (file != null) {
            (new Thread() {
               public void run() {
                  AProjectActions.saveProject(file);
               }
            }).start();
         }

      }
   }

   private static class CloseProjectAction extends AbstractAction {
      public CloseProjectAction() {
         this.putValue("ActionCommandKey", "menubar.project.closeProject");
      }

      public void actionPerformed(ActionEvent e) {
         int choice = AProjectActions.promptSaveOldProject();
         File file1 = null;
         if (choice == 0) {
            String projectFileName = GRepository.getInstance().getProject().getProjectFile().getAbsolutePath();
            file1 = ActionUtilities.getFileForSave(projectFileName, "oepr", "Ocle projects (*.oepr)", "Save project");
         }
         final File file = file1;
         (new Thread() {
            public void run() {
               AProjectActions.closeProject(file);
            }
         }).start();
      }
   }

   public static class OpenProjectAction extends AbstractAction {
      private File file;

      private OpenProjectAction() {
         this.putValue("ActionCommandKey", "menubar.project.openProject");
      }

      public void actionPerformed(ActionEvent e) {
         if (this.file == null || !this.file.exists()) {
            this.file = ActionUtilities.chooseFileForOpen("oepr", "Ocle projects (*.oepr)", "Open project");
         }

         if (this.file != null && this.file.exists()) {
            final File projectFile = this.file;
            (new Thread() {
               public void run() {
                  GAbstractProject oldProject = GRepository.getInstance().getProject();
                  if (oldProject != null) {
                     int choice = AProjectActions.promptSaveOldProject();
                     File projectFilex = null;
                     if (choice == 0) {
                        String projectFileName = oldProject.getProjectFile().getAbsolutePath();
                        projectFilex = ActionUtilities.getFileForSave(projectFileName, "oepr", "Ocle projects (*.oepr)", "Save project");
                     }

                     AProjectActions.closeProject(projectFilex);
                  }

                  AProjectActions.openProject(projectFile);
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

   private static class NewProjectAction extends AbstractAction {
      public NewProjectAction() {
         this.putValue("ActionCommandKey", "menubar.project.newProject");
      }

      public void actionPerformed(ActionEvent e) {
         int choice = AProjectActions.promptSaveOldProject();
          File file1 = null;
         if (choice == 0) {
            String projectFileName = GRepository.getInstance().getProject().getProjectFile().getAbsolutePath();
            file1 = ActionUtilities.getFileForSave(projectFileName, "oepr", "Ocle projects (*.oepr)", "Save project");
         }
         final File file = file1;

         (new Thread() {
            public void run() {
               AProjectActions.closeProject(file);
               NewProjectWizard wizard = new NewProjectWizard(GApplication.frame);
               wizard.runWizard();
               ProjectManager.getInstance().newProject(wizard.getProjectKind(), wizard.getProjectName(), wizard.getProjectFileName(), wizard.getXmlFiles(), wizard.getOclFiles());
            }
         }).start();
      }
   }
}
