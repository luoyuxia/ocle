package ro.ubbcluj.lci.gui.wizards;

import java.awt.Frame;
import java.util.Vector;
import javax.swing.DefaultListModel;
import ro.ubbcluj.lci.gui.tools.AFileFilter;
import ro.ubbcluj.lci.gui.wizards.newprojectsteps.KindOfProjectWizardStep;
import ro.ubbcluj.lci.gui.wizards.newprojectsteps.ProjectNameWizardStep;
import ro.ubbcluj.lci.gui.wizards.newprojectsteps.SelectFilesWizardStep;

public class NewProjectWizard extends Wizard {
   private KindOfProjectWizardStep kopws;
   private ProjectNameWizardStep pnws;
   private SelectFilesWizardStep sofws;
   private SelectFilesWizardStep sxfws;
   private boolean twoSteps;

   public NewProjectWizard(Frame owner) {
      super(owner, "Create New Project");
      this.registerWizardStep(this.kopws = new KindOfProjectWizardStep(this));
      this.registerWizardStep(this.pnws = new ProjectNameWizardStep(this));
      this.registerWizardStep(this.sxfws = new SelectFilesWizardStep(this, "Attach UML models in XMI format to project", new AFileFilter[]{new AFileFilter("xml,xml.zip", "XMI and packed XMI files (*.xml, *.xml.zip)")}, true, false));
      AFileFilter oclFilter = new AFileFilter("ocl", "Ocl text files (*.ocl, *.bcr)");
      oclFilter.addExtension("bcr");
      this.registerWizardStep(this.sofws = new SelectFilesWizardStep(this, "Attach OCL specifications files to project", new AFileFilter[]{oclFilter}, false, true));
      this.twoSteps = false;
   }

   public String getProjectKind() {
      return (String)this.kopws.getResult();
   }

   public String getProjectName() {
      Object newProjectInfos = this.pnws.getResult();
      if (newProjectInfos != null) {
         Object projectName = ((Vector)newProjectInfos).elementAt(0);
         if (projectName != null) {
            return (String)projectName;
         }
      }

      return null;
   }

   public String getProjectFileName() {
      Object newProjectInfos = this.pnws.getResult();
      if (newProjectInfos != null) {
         Object projectName = ((Vector)newProjectInfos).elementAt(1);
         if (projectName != null) {
            return (String)projectName;
         }
      }

      return null;
   }

   public Object[] getXmlFiles() {
      DefaultListModel xmlFiles = (DefaultListModel)this.sxfws.getResult();
      return xmlFiles != null ? xmlFiles.toArray() : null;
   }

   public Object[] getOclFiles() {
      DefaultListModel oclfiles = (DefaultListModel)this.sofws.getResult();
      return oclfiles != null ? oclfiles.toArray() : null;
   }

   protected void refreshBtnStates() {
      this.btn[0].setEnabled(this.currentStep != 0);
      this.btn[1].setEnabled(false);
      this.btn[2].setEnabled(false);
      this.btnContinue = (this.currentStep == this.steps.size() - 1 || this.twoSteps) && (this.currentStep == this.steps.size() - 3 || !this.twoSteps) ? this.btn[2] : this.btn[1];
   }

   protected void processStepResult(int step) {
      if (step == 0) {
         String kopString = (String)this.kopws.getResult();
         if (kopString.equals("Empty project")) {
            this.twoSteps = true;
         } else {
            this.twoSteps = false;
         }
      }

   }
}
