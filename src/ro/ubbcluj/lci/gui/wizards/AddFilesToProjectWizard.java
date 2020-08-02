package ro.ubbcluj.lci.gui.wizards;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import javax.swing.DefaultListModel;
import ro.ubbcluj.lci.gui.FileSelectionData;
import ro.ubbcluj.lci.gui.Actions.AUMLModelActions;
import ro.ubbcluj.lci.gui.mainframe.GAbstractProject;
import ro.ubbcluj.lci.gui.mainframe.GMainFrame;
import ro.ubbcluj.lci.gui.mainframe.GRepository;
import ro.ubbcluj.lci.gui.tools.AFileFilter;
import ro.ubbcluj.lci.gui.wizards.newprojectsteps.SelectFilesWizardStep;

public class AddFilesToProjectWizard extends Wizard {
   private SelectFilesWizardStep sofws;
   private SelectFilesWizardStep sxfws;

   public AddFilesToProjectWizard(Frame owner, boolean ocl, boolean uml) {
      super(owner, "Add Files to Project");
      if (ocl) {
         AFileFilter filter = new AFileFilter("ocl", "Ocl text files (*.ocl, *.bcr)");
         filter.addExtension("bcr");
         this.registerWizardStep(this.sofws = new SelectFilesWizardStep(this, "Attach OCL specification files to project", new AFileFilter[]{filter}, false, true));
      }

      if (uml) {
         this.registerWizardStep(this.sxfws = new SelectFilesWizardStep(this, "Attach UML models in XMI format to project", new AFileFilter[]{new AFileFilter("xml,xml.zip", "XMI and packed XMI files (*.xml, *.xml.zip)")}, true, false));
      }

   }

   protected void endWizard() {
      super.endWizard();
      if (!this.cancelled) {
         GAbstractProject project = GRepository.getInstance().getProject();
         GMainFrame mainframe = GMainFrame.getMainFrame();
         if (this.sofws != null) {
            DefaultListModel oclfiles = (DefaultListModel)this.sofws.getResult();

            for(int i = 0; i < oclfiles.size(); ++i) {
               FileSelectionData fileSelectionData = (FileSelectionData)oclfiles.elementAt(i);

               try {
                  File file = new File(fileSelectionData.getFileName());
                  if (!file.exists()) {
                     file.createNewFile();
                  }

                  project.attachConstraint(fileSelectionData.getFileName(), fileSelectionData.isSelected());
               } catch (IOException var8) {
                  mainframe.updateLog("File " + fileSelectionData.getFileName() + " is not valid and was not" + " attached to the project");
               }
            }
         }

         String activeModelFile = null;
         if (this.sxfws != null) {
            DefaultListModel xmlfiles = (DefaultListModel)this.sxfws.getResult();

            for(int i = 0; i < xmlfiles.size(); ++i) {
               FileSelectionData fileSelectionData = (FileSelectionData)xmlfiles.elementAt(i);
               if (fileSelectionData.isSelected()) {
                  activeModelFile = fileSelectionData.getFileName();
               }

               File file = new File(fileSelectionData.getFileName());
               if (file.exists()) {
                  project.attachModel(fileSelectionData.getFileName(), false);
               } else {
                  mainframe.updateLog("File " + file.getAbsolutePath() + " does not exist and" + " was not attached to the project");
               }
            }
         }

         if (activeModelFile != null) {
            final File modelFile = new File(activeModelFile);
            (new Thread() {
               public void run() {
                  AUMLModelActions.openUML(modelFile);
               }
            }).start();
         }

      }
   }
}
