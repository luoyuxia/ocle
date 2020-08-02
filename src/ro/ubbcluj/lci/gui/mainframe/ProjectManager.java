package ro.ubbcluj.lci.gui.mainframe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JOptionPane;
import ro.ubbcluj.lci.gui.FileSelectionData;
import ro.ubbcluj.lci.gui.tools.RecentFiles;
import ro.ubbcluj.lci.gui.tools.Warning;

public class ProjectManager {
   private static ProjectManager managerInstance;

   private ProjectManager() {
   }

   public static ProjectManager getInstance() {
      if (managerInstance == null) {
         managerInstance = new ProjectManager();
      }

      return managerInstance;
   }

   public void newProject(String projectKind, String projectName, String projectFileName, Object[] xmlFiles, Object[] oclFiles) {
      if (projectFileName != null) {
         File projectFile = new File(projectFileName);
         if (projectFile.exists()) {
            int option = JOptionPane.showConfirmDialog(GApplication.frame, "The file " + projectFile.getAbsolutePath() + " already exists.\n" + "Overwrite it?");
            if (option != 0) {
               GMainFrame.getMainFrame().updateMessages((Object)(new Warning() {
                  public String toString() {
                     return "No files created!";
                  }
               }));
               return;
            }
         } else {
            try {
               projectFile.createNewFile();
            } catch (IOException var18) {
               JOptionPane.showMessageDialog(GApplication.frame, projectFile.getName() + " could not be created!");
               return;
            }
         }

         GAbstractProject project = new GProject();
         project.setProjectName(projectName);

         try {
            project.setProjectFile(projectFile);
         } catch (IOException var17) {
            JOptionPane.showConfirmDialog(GApplication.frame, "Invalid file " + projectFile + '(' + var17.getMessage() + ')');
         }

         if (projectKind.equals("Empty project")) {
            System.out.println(this.getClass().getResource("/").getPath());
            InputStream modelInputStream = this.getClass().getResourceAsStream("/templates/newModel.xml.zip");
            InputStream metamodelOclInputStream = this.getClass().getResourceAsStream("/templates/MetaLevelOCL.ocl");
            InputStream modelOclInputStream = this.getClass().getResourceAsStream("/templates/ModelLevelOCL.bcr");
            File modelFile = new File(projectFile.getParent(), projectName + "Model.xml.zip");
            File metamodelOclFile = new File(projectFile.getParent(), projectName + "MetaLevel.ocl");
            File modelOclFile = new File(projectFile.getParent(), projectName + "ModelLevel.bcr");

            try {
               modelFile.createNewFile();
               this.copyFile(modelInputStream, modelFile);
               project.attachModel(modelFile.getPath(), true);
               metamodelOclFile.createNewFile();
               this.copyFile(metamodelOclInputStream, metamodelOclFile);
               project.attachConstraint(metamodelOclFile.getPath());
               modelOclFile.createNewFile();
               this.copyFile(modelOclInputStream, modelOclFile);
               project.attachConstraint(modelOclFile.getPath());
            } catch (IOException var16) {
               System.err.println("File could not be created!");
            }
         } else if (projectKind.equals("Existing project")) {
            int i;
            FileSelectionData fileSelectionData;
            for(i = 0; i < xmlFiles.length; ++i) {
               fileSelectionData = (FileSelectionData)xmlFiles[i];
               project.attachModel(fileSelectionData.getFileName(), fileSelectionData.isSelected());
            }

            for(i = 0; i < oclFiles.length; ++i) {
               fileSelectionData = (FileSelectionData)oclFiles[i];
               project.attachConstraint(fileSelectionData.getFileName(), fileSelectionData.isSelected());
            }
         }

         try {
            project.saveProject(projectFile);
         } catch (IOException var15) {
            JOptionPane.showConfirmDialog(GApplication.frame, "Could not write project file " + projectFile + '(' + var15.getMessage() + ')');
         }

         project.setDirty(false);
         GRepository.getInstance().changeProject(project);
      } else {
         JOptionPane.showMessageDialog(GApplication.frame, "No project file was selected. The \n project could not be created.", "Error", 0);
      }

   }

   public void openProject(File projectFile) throws Exception {
      GAbstractProject project = new GProject(projectFile);
      GRepository.getInstance().changeProject(project);
   }

   public void saveProject(File projectFile) throws IOException {
      GRepository.getInstance().getProject().saveProject(projectFile);
   }

   public void closeProject() {
      GRepository repository = GRepository.getInstance();
      GAbstractProject project = repository.getProject();
      if (project != null) {
         this.updateProjectRecentFiles(project.getProjectFile());
         repository.closeProject();
      }

   }

   public void newUMLModel() {
      GRepository repository = GRepository.getInstance();
      GUMLModel userModel = repository.newUserModel((String)null);
      repository.changeUsermodel(userModel);
   }

   public void openUMLModel(File modelFile) {
      GRepository repository = GRepository.getInstance();
      String modelFileName = modelFile.getAbsolutePath();
      GUMLModel gmodel = repository.newUserModel(modelFileName);
      if (gmodel.getModel() == null) {
         repository.changeUsermodel((GUMLModel)null);
         GMainFrame.getMainFrame().updateLog("The UML Model contains errors and could not be loaded. See Messages pane for details.\n");
         if (repository.getProject() != null && repository.getProject().containsModel(modelFileName)) {
            int choice = JOptionPane.showConfirmDialog(GMainFrame.getMainFrame(), "The UML Model contains errors and could not be \nloaded. Would you like to remove the erroneous \n           model from project?", "Error", 0, 0);
            if (choice == 0) {
               repository.getProject().detachModel(modelFileName, false);
            }
         }
      } else {
         repository.changeUsermodel(gmodel);
      }

   }

   public void saveUMLModel(File modelFile) {
      GUMLModel userModel = GRepository.getInstance().getUsermodel();
      if (userModel != null) {
         userModel.saveModel(modelFile);
      }

   }

   public void saveUMLModelAs(File newModelFile) {
      String newFilePath = newModelFile.getAbsolutePath();
      GUMLModel userModel = GRepository.getInstance().getUsermodel();
      if (userModel != null) {
         GAbstractProject project = userModel.getOwnerProject();
         if (project != null) {
            project.detachModel(userModel.getModelFileName(), false);
            project.attachModel(newFilePath, false);
            project.setActiveModelFile(newFilePath);
         }

         userModel.saveModel(newModelFile);
      }

   }

   public void closeUMLModel() {
      GRepository repository = GRepository.getInstance();
      GUMLModel userModel = repository.getUsermodel();
      if (userModel != null) {
         String modelFileName = userModel.getModelFileName();
         if (modelFileName != null) {
            this.updateModelRecentFiles(new File(modelFileName));
         }

         repository.closeUsermodel();
      }

   }

   public void updateProjectRecentFiles(File projectFile) {
      if (projectFile != null) {
         RecentFiles fh = RecentFiles.getInstance();
         RecentFiles.setFile(RecentFiles.PROJECT_FILES);
         String fileName = projectFile.getAbsolutePath();
         if (RecentFiles.isAccepted(fileName)) {
            fh.checkFile(fileName);
         }
      }

   }

   public void updateModelRecentFiles(File modelFile) {
      RecentFiles fh = RecentFiles.getInstance();
      RecentFiles.setFile(RecentFiles.MODEL_FILES);
      String selectedFile = modelFile.getAbsolutePath();
      if (RecentFiles.isAccepted(selectedFile)) {
         fh.checkFile(selectedFile);
      }

   }

   private void copyFile(InputStream source, File destination) {
      try {
         FileOutputStream fileOutputStream = new FileOutputStream(destination);
         boolean var4 = true;

         int r;
         do {
            byte[] buf = new byte[512];
            r = source.read(buf);
            if (r != -1) {
               fileOutputStream.write(buf, 0, r);
            }
         } while(r != -1);
      } catch (FileNotFoundException var6) {
         System.err.println("File Not Found Exception" + var6.getMessage());
      } catch (IOException var7) {
         System.err.println("IO Exception" + var7.getMessage());
      }

   }
}
