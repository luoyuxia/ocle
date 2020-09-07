package ro.ubbcluj.lci.gui.mainframe;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

   public List newEmptyProject(String projectName, String projectFileName,
                               Object[] xmlFiles, Object[] oclFiles) {
      ArrayList tmpPaths = new ArrayList();
      if (projectFileName != null) {
         File projectFile = new File(projectFileName);
         if (projectFile.exists()) {
            projectFile.delete();
//            int option = JOptionPane.showConfirmDialog(GApplication.frame, "The file " + projectFile.getAbsolutePath() + " already exists.\n" + "Overwrite it?");
//            if (option != 0) {
//               GMainFrame.getMainFrame().updateMessages((Object)(new Warning() {
//                  public String toString() {
//                     return "No files created!";
//                  }
//               }));
//               return new ArrayList();
//            }
         } else {
            try {
               projectFile.createNewFile();
            } catch (IOException var18) {
               JOptionPane.showMessageDialog(GApplication.frame, projectFile.getName() + " could not be created!");
               return new ArrayList();
            }
         }
         tmpPaths.add(projectFile.getAbsoluteFile());

         GAbstractProject project = new GProject();
         project.setProjectName(projectName);

         try {
            project.setProjectFile(projectFile);
         } catch (IOException var17) {
            JOptionPane.showConfirmDialog(GApplication.frame, "Invalid file " + projectFile + '(' + var17.getMessage() + ')');
         }

         try {
            File modelFile = new File(projectFile.getParent(), projectName + ".xml");
            modelFile.createNewFile();
            OutputStream modelOutStream = new FileOutputStream(modelFile);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(modelOutStream));
            InputStream modelInputStream = new FileInputStream(((FileSelectionData)xmlFiles[0]).getFileName());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(modelInputStream));
            String line;
            boolean hasComment = false;
            while ((line = bufferedReader.readLine()) != null) {
               if (line.equals("<!--")) {
                  hasComment = true;
               }
               if (hasComment && !line.equals("-->") && !line.equals("<!--")) {
                  bufferedWriter.write(line);
               }
            }
            bufferedReader.close();
            bufferedWriter.close();
            if (!hasComment) {
               this.copyFile(new FileInputStream(((FileSelectionData)xmlFiles[0]).getFileName()),
                       modelFile);
            }

            tmpPaths.add(modelFile.getAbsoluteFile());

         //   this.copyFile(modelInputStream, modelFile);
            project.attachModel(modelFile.getPath(), true);

//            File modelOclFile = new File(projectFile.getParent(), projectName + ".bcr");
//            modelOclFile.createNewFile();
//            tmpPaths.add(modelOclFile.getAbsoluteFile());
//
//            InputStream modelOclInputStream = new FileInputStream(((FileSelectionData)oclFiles[0]).getFileName());
//            this.copyFile(modelOclInputStream, modelOclFile);
//            project.attachConstraint(modelOclFile.getPath(), true);
             String oclFilePath = ((FileSelectionData)oclFiles[0]).getFileName();
             String oclFileName = oclFilePath.substring(oclFilePath.lastIndexOf(File.separatorChar) + 1);
             File metamodelOclFile = new File(projectFile.getParent(), "_" + oclFileName);
             metamodelOclFile.createNewFile();
             tmpPaths.add(metamodelOclFile.getAbsoluteFile());
             InputStream modelOclInputStream = new FileInputStream(((FileSelectionData)oclFiles[0]).getFileName());
             this.copyFile(modelOclInputStream, metamodelOclFile);
             project.attachConstraint(metamodelOclFile.getPath(), true);

//            InputStream metamodelOclInputStream = this.getClass().getResourceAsStream("/templates/MetaLevelOCL.ocl");
////            File metamodelOclFile = new File(projectFile.getParent(), projectName + "MetaLevel.ocl");
////            metamodelOclFile.createNewFile();
////            this.copyFile(metamodelOclInputStream, metamodelOclFile);
////            project.attachConstraint(metamodelOclFile.getPath(), true);
////            tmpPaths.add(metamodelOclFile.getAbsoluteFile());

         } catch (IOException var16) {
            System.err.println("File could not be created!");
         }

         FileSelectionData fileSelectionData;
         for (int i = 1; i < xmlFiles.length; ++i) {
            fileSelectionData = (FileSelectionData) xmlFiles[i];
            project.attachModel(fileSelectionData.getFileName(), fileSelectionData.isSelected());
         }
         for (int i = 1; i < oclFiles.length; ++i) {
            fileSelectionData = (FileSelectionData) oclFiles[i];
            String fileName = fileSelectionData.getFileName();
            if (!fileSelectionData.getFileName().endsWith(".bcr")) {
               File file = new File(fileName);
               int last_index = fileName.lastIndexOf('.');
               String newFileName = fileName.substring(0, last_index) + ".bcr";
               File newFile = new File(newFileName);
               if (newFile.exists()) {
                  newFile.delete();
               }
               try {
                  this.copyFile(new FileInputStream(file), newFile);
               } catch (IOException e) {
                  e.printStackTrace();
               }
               fileName = newFileName;
            }
            tmpPaths.add(fileName);
            project.attachConstraint(fileName, true);
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
      return tmpPaths;
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
