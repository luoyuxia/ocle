package ro.ubbcluj.lci.gui.mainframe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import ro.ubbcluj.lci.gui.Actions.CompilationInfo;
import ro.ubbcluj.lci.gui.browser.BTree.CustomFilter;
import ro.ubbcluj.lci.gui.browser.BTree.Filter;
import ro.ubbcluj.lci.gui.diagrams.GAbstractDiagram;
import ro.ubbcluj.lci.gui.editor.AbstractPad;
import ro.ubbcluj.lci.gui.editor.Editor;
import ro.ubbcluj.lci.gui.editor.TextDocumentModel;
import ro.ubbcluj.lci.gui.properties.GProperties;
import ro.ubbcluj.lci.ocl.OclCompiler;
import ro.ubbcluj.lci.ocl.batcheval.BatchEvaluationSystem;
import ro.ubbcluj.lci.redtd.ReDTDModelCache;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.ext.profiles.DefaultProfileFactory;
import ro.ubbcluj.lci.uml.ext.profiles.Profile;
import ro.ubbcluj.lci.uml.ext.profiles.ProfileManager;
import ro.ubbcluj.lci.uml.modelManagement.Model;
import ro.ubbcluj.lci.utils.ModelFactory;
import ro.ubbcluj.lci.utils.ModelListener;
import ro.ubbcluj.lci.utils.uml.UMLNavigationUtilities;

public class GRepository {
   private GAbstractProject project;
   private GUMLModel metamodel;
   private GUMLModel usermodel;
   private OclCompiler compiler;
   private BatchEvaluationSystem ev_sys;
   private ProfileManager profileManager = null;
   private Filter currentUserModelBrowserFilter = null;
   private Collection lastCompilationInfos = new ArrayList();
   private static GRepository repository = null;

   private GRepository() {
   }

   public static final GRepository getInstance() {
      if (repository == null) {
         repository = new GRepository();
      }

      return repository;
   }

   public void setLastCompilationInfos(CompilationInfo[] lci) {
      this.lastCompilationInfos.clear();

      for(int i = 0; i < lci.length; ++i) {
         this.lastCompilationInfos.add(lci[i]);
      }

   }

   public CompilationInfo[] getLastCompilationInfos() {
      return (CompilationInfo[])this.lastCompilationInfos.toArray(new CompilationInfo[0]);
   }

   public CompilationInfo getInfoForFile(String fileName) {
      CompilationInfo result = null;
      Collection tmpCI = new LinkedHashSet(this.lastCompilationInfos);
      CompilationInfo ci;
      if (this.project != null) {
         ci = this.project.getWfrInfo();
         if (ci != null) {
            tmpCI.add(ci);
         }

         ci = this.project.getBcrInfo();
         if (ci != null) {
            tmpCI.add(ci);
         }
      }

      Iterator it = tmpCI.iterator();

      while(result == null && it.hasNext()) {
         ci = (CompilationInfo)it.next();
         if (ci.isAffected(fileName)) {
            result = ci;
         }
      }

      if (result == null && this.profileManager != null) {
         Profile p = this.profileManager.getProfileForFile(fileName);
         if (p != null) {
            result = p.getInfo();
         }
      }

      return result;
   }

   void removeInfo(CompilationInfo ci) {
      this.lastCompilationInfos.remove(ci);
      if (this.project != null) {
         if (ci == this.project.getWfrInfo()) {
            this.project.setWfrInfo(new CompilationInfo(false));
         } else if (ci == this.project.getBcrInfo()) {
            this.project.setBcrInfo(new CompilationInfo(false));
         }
      }

   }

   public void setLastCompilationInfos(Collection lci) {
      this.lastCompilationInfos.clear();
      this.lastCompilationInfos.addAll(lci);
   }

   private void clearCompilationInfos() {
      Collection tempCI = new HashSet(this.lastCompilationInfos);
      CompilationInfo ci;
      if (this.project != null) {
         ci = this.project.getWfrInfo();
         if (ci != null) {
            tempCI.add(ci);
            ci.clearUserModelData();
         }

         ci = this.project.getBcrInfo();
         if (ci != null) {
            tempCI.add(ci);
            this.project.setBcrInfo(new CompilationInfo(false));
         }
      }

      Iterator itTempCI = tempCI.iterator();

      while(itTempCI.hasNext()) {
         ci = (CompilationInfo)itTempCI.next();
         if (ci.getMode() == 0) {
            ci.clearUserModelData();
         } else {
            this.lastCompilationInfos.remove(ci);
         }
      }

      tempCI.clear();
      if (this.profileManager != null) {
         this.profileManager.cleanUp();
      }

   }

   public Filter getCurrentBrowserFilter() {
      if (this.currentUserModelBrowserFilter == null) {
         this.currentUserModelBrowserFilter = new CustomFilter();
      }

      return this.currentUserModelBrowserFilter;
   }

   public void setCurrentBrowserFilter(Filter f) {
      this.currentUserModelBrowserFilter = f;
   }

   public void setMetamodel(GUMLModel metamodel) {
      this.metamodel = metamodel;
      ModelFactory.addModelListener(metamodel.getBrowser());
      Iterator it = metamodel.getDiagrams().iterator();

      while(it.hasNext()) {
         ModelFactory.addModelListener((ModelListener)it.next());
      }

      ModelFactory.addModelListener(GProperties.getInstance());
   }

   public final GUMLModel getMetamodel() {
      return this.metamodel;
   }

   public void setCompiler(OclCompiler compiler) {
      this.compiler = compiler;
   }

   public final OclCompiler getCompiler() {
      if (this.compiler == null) {
         this.compiler = new OclCompiler();
         this.compiler.setDumpListener(new OclDumpListener());
      }

      return this.compiler;
   }

   public final void setEvaluationSystem(BatchEvaluationSystem ev_sys) {
      this.ev_sys = ev_sys;
   }

   public final BatchEvaluationSystem getEvaluationSystem() {
      return this.ev_sys;
   }

   private void resetCompiler() {
      if (this.compiler != null) {
         this.compiler = new OclCompiler();
         this.compiler.setDumpListener(new OclDumpListener());
      }

      OclCompiler.cleanup();
   }

   public void loadProfiles() {
      if (this.profileManager == null) {
         this.profileManager = new ProfileManager("metamodel/profiles/profiles.xml", new DefaultProfileFactory());
      }

   }

   public final ProfileManager getProfileManager() {
      return this.profileManager;
   }

   public final GUMLModel getUsermodel() {
      return this.usermodel;
   }

   public void changeUsermodel(GUMLModel newUsermodel) {
      this.usermodel = newUsermodel;
      if (this.usermodel != null) {
         ModelFactory.currentModel = this.usermodel.getModel();
         if (this.usermodel.getBrowser() != null) {
            ModelFactory.addModelListener(this.usermodel.getBrowser());
         }

         Iterator it = this.usermodel.getDiagrams().iterator();

         while(it.hasNext()) {
            ModelFactory.addModelListener((ModelListener)it.next());
         }

         ModelFactory.addModelListener(ReDTDModelCache.getInstance());
         ModelFactory.fireModelEvent(this.usermodel.getModel(), (Object)null, 0);
         Iterator itc = this.lastCompilationInfos.iterator();
         Model model = this.usermodel.getModel();

         while(itc.hasNext()) {
            CompilationInfo ci = (CompilationInfo)itc.next();
            if (ci.getMode() == 0) {
               ci.setTargetModel(model);
            }
         }
      }

      this.refreshModelProjectReferences();
      GMainFrame.getMainFrame().usermodelChanged();
      System.gc();
   }

   public void closeUsermodel() {
      GMainFrame mainframe = GMainFrame.getMainFrame();
      if (this.usermodel != null) {
         ModelFactory.removeModelListener(this.usermodel.getBrowser());
         Iterator it = this.usermodel.getDiagrams().iterator();

         while(it.hasNext()) {
            Object dgm = it.next();
            ModelFactory.removeModelListener((ModelListener)dgm);
            ((GAbstractDiagram)dgm).clear();
         }

         if (this.usermodel.getModel() != null) {
            RepositoryChangeAgent.removeAgent(this.usermodel.getModel());
         }

         this.clearCompilationInfos();
         this.resetCompiler();
         ReDTDModelCache dtdModelCache = ReDTDModelCache.getInstance();
         ModelFactory.removeModelListener(dtdModelCache);
         dtdModelCache.clearCache();
         ModelFactory.clearStaticReferences();
         this.usermodel = null;
         this.refreshModelProjectReferences();
         GProperties.getInstance().setUserObject(this.metamodel);
         mainframe.usermodelChanged();
         System.gc();
      }

   }

   public final GAbstractProject getProject() {
      return this.project;
   }

   public void changeProject(GAbstractProject newProject) {
      this.project = newProject;
      GUMLModel lastModel = this.getUsermodel();
      GUMLModel projectActiveModel = this.project.getActiveModel();
      if (lastModel != projectActiveModel) {
         lastModel.setOwnerProject((GAbstractProject)null);
         this.closeUsermodel();
      }

      if (projectActiveModel != null) {
         this.changeUsermodel(projectActiveModel);
      } else {
         String activeFileName = this.project.getActiveModelFile();
         if (activeFileName != null) {
            projectActiveModel = this.newUserModel(activeFileName);
            this.changeUsermodel(projectActiveModel);
         } else {
            this.changeUsermodel(lastModel);
         }
      }

      this.refreshModelProjectReferences();
      GMainFrame.getMainFrame().activeProjectChanged();
      this.project.setDirty(false);
      System.gc();
   }

   public void closeProject() {
      boolean bCloseUserModel = this.usermodel != null && this.usermodel.getOwnerProject() == this.project;
      if (bCloseUserModel) {
         this.closeUsermodel();
      }

      ArrayList containedFiles = this.project.getFileBrowser().getAllFiles();
      ro.ubbcluj.lci.gui.editor.mdi.PadContainer viewContainer = GMainFrame.getMainFrame().getViewContainer();
      Editor edRef = GApplication.getApplication().getEditor();
      ArrayList toBeRemoved = new ArrayList();
      Iterator models = edRef.getFiles().iterator();

      while(true) {
         TextDocumentModel cModel;
         do {
            if (!models.hasNext()) {
               viewContainer.closeAll((AbstractPad[])toBeRemoved.toArray(new AbstractPad[0]));
               CompilationInfo ci = this.project.getWfrInfo();
               this.lastCompilationInfos.remove(ci);
               ci = this.project.getBcrInfo();
               this.lastCompilationInfos.remove(ci);
               cModel = null;
               this.project = null;
               this.refreshModelProjectReferences();
               GMainFrame.getMainFrame().activeProjectChanged();
               System.gc();
               return;
            }

            cModel = (TextDocumentModel)models.next();
         } while(!containedFiles.contains(cModel.getFileName()));

         AbstractPad[] views = cModel.getViews();

         for(int i = 0; i < views.length; ++i) {
            toBeRemoved.add(views[i]);
         }
      }
   }

   public GUMLModel newUserModel(String fileName) {
      GUMLModel userModel = new GUMLModel(fileName);
      Model umdl = userModel.getModel();
      if (umdl != null) {
         UMLNavigationUtilities.setOwnerModel(umdl);
      }

      return userModel;
   }

   private void refreshModelProjectReferences() {
      if (this.usermodel != null && this.project != null) {
         if (this.project.containsModel(this.usermodel.getModelFileName())) {
            this.usermodel.setOwnerProject(this.project);
            this.project.setActiveModelFile(this.usermodel.getModelFileName());
            this.project.setActiveModel(this.usermodel);
         } else {
            this.usermodel.setOwnerProject((GAbstractProject)null);
            this.project.setActiveModelFile((String)null);
            this.project.setActiveModel((GUMLModel)null);
         }
      } else if (this.project == null && this.usermodel != null) {
         this.usermodel.setOwnerProject((GAbstractProject)null);
      }

   }
}
