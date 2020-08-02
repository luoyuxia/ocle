package ro.ubbcluj.lci.gui.mainframe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ro.ubbcluj.lci.gui.Actions.AUMLModelActions;
import ro.ubbcluj.lci.gui.Actions.CompilationInfo;
import ro.ubbcluj.lci.gui.browser.FBrowser;
import ro.ubbcluj.lci.utils.FileUtilities;

public class GProject implements GAbstractProject {
   protected List views;
   private boolean readOnly;
   private File prjFile;
   private String prjName;
   private List models;
   private List constraints;
   private List selectedConstraints;
   private String activeModelFile;
   private GUMLModel activeModel;
   private transient CompilationInfo wfrInfo;
   private transient CompilationInfo bcrInfo;
   private boolean dirty;

   public GProject(boolean ro) {
      this.views = new ArrayList();
      this.readOnly = false;
      this.prjFile = null;
      this.prjName = "OCLE Project";
      this.models = new ArrayList();
      this.constraints = new ArrayList();
      this.selectedConstraints = new ArrayList();
      this.activeModelFile = null;
      this.activeModel = null;
      this.wfrInfo = null;
      this.bcrInfo = null;
      this.dirty = false;
      this.readOnly = ro;
      FBrowser prjBrowser = new FBrowser(this);
      this.views.add(prjBrowser);
      this.activeModel = null;
      this.wfrInfo = new CompilationInfo(false);
      this.wfrInfo.setMode(0);
      this.bcrInfo = new CompilationInfo(false);
      this.bcrInfo.setMode(1);
   }

   public GProject(String filename, boolean ro) throws Exception {
      this(new File(filename), ro);
   }

   public GProject() {
      this(false);
   }

   public GProject(String filename) throws Exception {
      this(filename, false);
   }

   public GProject(File file) throws Exception {
      this(file, false);
   }

   public GProject(File file, boolean ro) throws Exception {
      this.views = new ArrayList();
      this.readOnly = false;
      this.prjFile = null;
      this.prjName = "OCLE Project";
      this.models = new ArrayList();
      this.constraints = new ArrayList();
      this.selectedConstraints = new ArrayList();
      this.activeModelFile = null;
      this.activeModel = null;
      this.wfrInfo = null;
      this.bcrInfo = null;
      this.dirty = false;
      this.readOnly = ro;
      this.loadProject(file);
      this.wfrInfo = new CompilationInfo(false);
      this.wfrInfo.setMode(0);
      this.bcrInfo = new CompilationInfo(false);
      this.bcrInfo.setMode(1);
   }

   public boolean isDirty() {
      return this.dirty;
   }

   public void setWfrInfo(CompilationInfo ci) {
      this.wfrInfo = ci;
   }

   public CompilationInfo getWfrInfo() {
      return this.wfrInfo;
   }

   public void setBcrInfo(CompilationInfo ci) {
      this.bcrInfo = ci;
   }

   public CompilationInfo getBcrInfo() {
      return this.bcrInfo;
   }

   public FBrowser getFileBrowser() {
      return this.views.size() > 0 ? (FBrowser)this.views.get(0) : null;
   }

   public File getProjectFile() {
      return this.prjFile;
   }

   public void setProjectFile(File file) throws IOException {
      File tf = FileUtilities.asFullPathFile(file);
      if (tf != null) {
         this.prjFile = tf;
      }

   }

   public boolean containsModel(String modelFileName) {
      if (modelFileName != null && this.models != null) {
         String realFileName = null;

         try {
            realFileName = FileUtilities.asFullPathFile(modelFileName);
         } catch (IOException var4) {
         }

         return realFileName == null ? false : this.models.contains(realFileName);
      } else {
         return false;
      }
   }

   public String getActiveModelFile() {
      return this.activeModelFile;
   }

   public void setActiveModelFile(String newFile) {
      if (newFile != null) {
         try {
            this.activeModelFile = FileUtilities.asFullPathFile(newFile);
            this.setDirty(true);
         } catch (IOException var3) {
         }
      } else {
         this.activeModelFile = null;
      }

   }

   public List getSelectedConstraints() {
      return this.selectedConstraints;
   }

   public GUMLModel getActiveModel() {
      return this.activeModel;
   }

   public void setActiveModel(GUMLModel model) {
      this.activeModel = model;
   }

   public String getProjectName() {
      return this.prjName;
   }

   public void setProjectName(String newName) {
      this.prjName = newName;
      this.getFileBrowser().projectNameChanged();
   }

   public void attachModel(String fileName, boolean loadModel) {
      String realFileName = null;

      try {
         realFileName = FileUtilities.asFullPathFile(fileName);
      } catch (IOException var8) {
      }

      if (realFileName != null) {
         boolean existsInPrj = false;
         if (this.models.contains(realFileName)) {
            existsInPrj = true;
            if (realFileName.equals(this.activeModelFile)) {
               GMainFrame.getMainFrame().updateMessages((Object)("File " + realFileName + " already is the active model in this project."));
               return;
            }
         }

         GUMLModel newActiveModel = null;
         if (loadModel) {
            GRepository repository = GRepository.getInstance();
            if (repository.getUsermodel() == null) {
               repository.newUserModel(realFileName);
               newActiveModel = repository.getUsermodel();
            } else {
               String openModelFileName = repository.getUsermodel().getModelFileName();
               if (openModelFileName.equals(realFileName)) {
                  newActiveModel = repository.getUsermodel();
               } else {
                  newActiveModel = new GUMLModel(realFileName);
               }
            }
         }

         if (loadModel) {
            this.activeModel = newActiveModel;
            this.setActiveModelFile(realFileName);
         }

         if (!existsInPrj) {
            this.models.add(realFileName);
            if (fileName.endsWith("xml.zip")) {
               this.getFileBrowser().addModel(realFileName, true);
            } else {
               this.getFileBrowser().addModel(realFileName, false);
            }

            GMainFrame.getMainFrame().updateLog("UML Model from XMI file \"" + realFileName + "\" attached to project.\n");
            this.setDirty(true);
         }

      }
   }

   public void detachModel(String fileName, boolean loadModel) {
      if (this.models.contains(fileName)) {
         this.models.remove(fileName);
         this.getFileBrowser().removeFile(fileName);
         GMainFrame.getMainFrame().updateLog("UML Model from XMI file \"" + fileName + "\" detached from project.\n");
         this.setDirty(true);
         if (!loadModel) {
            return;
         }

         if (this.activeModelFile != null && this.activeModelFile.equals(fileName)) {
            if (this.models.size() > 0) {
               final String nextmodelfile = (String)this.models.get(0);
               if (nextmodelfile != null) {
                  (new Thread() {
                     public void run() {
                        AUMLModelActions.openUML(new File(nextmodelfile));
                     }
                  }).start();
               }
            } else {
               this.activeModelFile = null;
               this.activeModel = new GUMLModel();
            }
         }
      }

   }

   public void attachConstraint(String fileName) {
      this.attachConstraint(fileName, true);
   }

   public void attachConstraint(String fileName, boolean selected) {
      GMainFrame mf = GMainFrame.getMainFrame();
      String realFileName = null;

      try {
         realFileName = FileUtilities.asFullPathFile(fileName);
      } catch (IOException var6) {
      }

      if (realFileName == null) {
         mf.updateLog("Invalid file " + fileName + ". It will not be attached to the project");
      } else if (this.constraints.contains(realFileName)) {
         mf.updateMessages((Object)("File " + realFileName + " already exists in this project."));
      } else {
         this.constraints.add(realFileName);
         if (selected) {
            this.selectedConstraints.add(realFileName);
         }

         this.getFileBrowser().addConstraint(realFileName);
         mf.updateLog("OCL Specifications file \"" + realFileName + "\" attached to project.\n");
         this.setDirty(true);
      }
   }

   public void detachConstraint(String fileName) {
      String realFileName = null;

      try {
         realFileName = FileUtilities.asFullPathFile(fileName);
      } catch (IOException var4) {
         GMainFrame.getMainFrame().updateLog("File " + fileName + "does not exist or is not accessible");
         return;
      }

      if (this.constraints.contains(realFileName)) {
         this.constraints.remove(realFileName);
         this.selectedConstraints.remove(realFileName);
         this.getFileBrowser().removeFile(realFileName);
         GMainFrame.getMainFrame().updateLog("OCL Specifications file \"" + realFileName + "\" detached from project.\n");
         this.setDirty(true);
      }

   }

   public void addView(GView view) {
      this.views.add(view);
   }

   public boolean removeView(GView view) {
      return this.views.remove(view);
   }

   public void loadProject(String filename) throws Exception {
      this.loadProject(new File(filename));
   }

   public void loadProject(File file) throws Exception {
      this.prjFile = file;
      this.views.clear();
      FBrowser prjBrowser = new FBrowser(this);
      this.views.add(prjBrowser);
      String theName = (String)prjBrowser.getRoot().getUserObject();
      this.prjName = theName != null ? theName : this.prjName;
      this.models = prjBrowser.getAllModels();
      this.constraints = prjBrowser.getAllConstraints();
      this.setDirty(false);
   }

   public void saveProject(File file) throws IOException {
      this.setProjectFile(file);
      this.setDirty(false);
      this.getFileBrowser().writeXml();
   }

   public void setReadOnly(boolean ro) {
      this.readOnly = ro;
   }

   public boolean isReadOnly() {
      return this.readOnly;
   }

   public void setDirty(boolean dirty) {
      this.dirty = dirty;
   }
}
