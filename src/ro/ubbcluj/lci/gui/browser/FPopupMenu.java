package ro.ubbcluj.lci.gui.browser;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;
import ro.ubbcluj.lci.gui.Actions.AFileActions;
import ro.ubbcluj.lci.gui.Actions.AUMLModelActions;
import ro.ubbcluj.lci.gui.mainframe.GAbstractProject;
import ro.ubbcluj.lci.gui.mainframe.GApplication;
import ro.ubbcluj.lci.gui.mainframe.GMainFrame;
import ro.ubbcluj.lci.gui.mainframe.GRepository;
import ro.ubbcluj.lci.gui.wizards.AddFilesToProjectWizard;

public class FPopupMenu extends JPopupMenu {
   private FBrowser browser;
   private FTreeNode selected;

   public FPopupMenu(FBrowser fBrowser) {
      this.browser = fBrowser;
      ((JTree)fBrowser.getComponent()).addMouseListener(new FPopupMenu.PopupListener());
   }

   protected JPopupMenu createPopup() {
      JPopupMenu pum = new JPopupMenu();
      this.selected = this.browser.getSelectedNode();
      if (this.selected.isDirectory()) {
         if (this.selected != this.browser.getRoot()) {
            pum.add(new GMenuItem(new FPopupMenu.NewDirectoryAction()));
            pum.add(new GMenuItem(new FPopupMenu.AddFileAction()));
         } else {
            pum.add(new GMenuItem(new FPopupMenu.OpenFileAction()));
         }
      } else if (this.selected.isFile()) {
         if (this.selected.isModelFile() || this.selected.isZippedModelFile()) {
            pum.add(new GMenuItem(new FPopupMenu.ActivateModelAction()));
         }

         pum.add(new GMenuItem(new FPopupMenu.OpenFileAction()));
         if (this.selected.isConstraintFile()) {
            pum.add(new GMenuItem(new FPopupMenu.SelectionConstraintAction()));
         }
      }

      pum.add(new GMenuItem(new FPopupMenu.DetachAction()));
      if (this.selected.isModelFile() || this.selected.isZippedModelFile()) {
         GAbstractProject prj = GRepository.getInstance().getProject();
         String amf = prj.getActiveModelFile();
         if (amf != null && this.selected.getUserObject().equals(new File(amf))) {
            pum.add(new GMenuItem(new FPopupMenu.CloseDetachAction()));
         }
      }

      return pum;
   }

   protected void refreshValidation(JPopupMenu pum) {
      Component[] all = pum.getComponents();

      for(int i = 0; i < all.length; ++i) {
         if (all[i] instanceof Validator) {
            ((Validator)all[i]).refreshValidation();
         }
      }

   }

   class PopupListener extends MouseAdapter {
      PopupListener() {
      }

      public void mouseClicked(MouseEvent e) {
         this.maybeShowPopup(e);
      }

      private void maybeShowPopup(MouseEvent e) {
         if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
            JTree tree = (JTree)FPopupMenu.this.browser.getComponent();
            TreePath to_select = tree.getClosestPathForLocation(e.getX(), e.getY());
            tree.setSelectionPath(to_select);
            JPopupMenu pum = FPopupMenu.this.createPopup();
            FPopupMenu.this.refreshValidation(pum);
            pum.show(e.getComponent(), e.getX(), e.getY());
         }

      }
   }

   class SelectionConstraintAction extends AbstractAction implements Validator {
      public SelectionConstraintAction() {
         List sc = FPopupMenu.this.browser.getProject().getSelectedConstraints();
         String fName = ((File)FPopupMenu.this.selected.getUserObject()).getPath();
         boolean exist = false;

         for(int i = 0; i < sc.size(); ++i) {
            if (fName.equals((String)sc.get(i))) {
               this.putValue("Name", "Exclude");
               exist = true;
            }
         }

         if (!exist) {
            this.putValue("Name", "Include");
         }

      }

      public void actionPerformed(ActionEvent evt) {
         List sc = FPopupMenu.this.browser.getProject().getSelectedConstraints();
         String fName = ((File)FPopupMenu.this.selected.getUserObject()).getPath();
         int idx = -1;

         for(int i = 0; i < sc.size(); ++i) {
            if (fName.equals((String)sc.get(i))) {
               idx = i;
            }
         }

         if (idx == -1) {
            sc.add(fName);
         } else {
            sc.remove(idx);
         }

      }

      public void refreshValidation() {
      }
   }

   class CloseDetachAction extends FPopupMenu.DetachAction {
      public CloseDetachAction() {
         super();
         this.putValue("Name", "Close and Remove");
      }

      public void actionPerformed(ActionEvent evt) {
         GAbstractProject prj = FPopupMenu.this.browser.getProject();
         if (!FPopupMenu.this.selected.isModelFile() && !FPopupMenu.this.selected.isZippedModelFile()) {
            super.actionPerformed(evt);
         } else {
            File activeModelFile = new File(prj.getActiveModelFile());
            if (FPopupMenu.this.selected.getUserObject().equals(activeModelFile)) {
               AUMLModelActions.closeUmlAction.actionPerformed(evt);
               if (prj.getActiveModel() == null) {
                  super.actionPerformed(evt);
               }
            }
         }

      }
   }

   class DetachAction extends AbstractAction implements Validator {
      public DetachAction() {
         this.putValue("Name", "Remove");
      }

      public void actionPerformed(ActionEvent evt) {
         GAbstractProject prj = FPopupMenu.this.browser.getProject();
         if (FPopupMenu.this.selected.isFile()) {
            String fName = ((File)FPopupMenu.this.selected.getUserObject()).getPath();
            if (FPopupMenu.this.selected.isConstraintFile()) {
               prj.detachConstraint(fName);
            } else if (FPopupMenu.this.selected.isModelFile() || FPopupMenu.this.selected.isZippedModelFile()) {
               prj.detachModel(fName, true);
            }
         } else if (FPopupMenu.this.selected.isDirectory()) {
            FPopupMenu.this.browser.removeNode(FPopupMenu.this.selected);
         }

         GMainFrame mainframe = GMainFrame.getMainFrame();
         mainframe.updateProjectActions();
         mainframe.updateEvaluationActions();
      }

      public void refreshValidation() {
         this.setEnabled(FPopupMenu.this.selected.getParent() != null && FPopupMenu.this.selected != FPopupMenu.this.browser.getConstraintsNode() && FPopupMenu.this.selected != FPopupMenu.this.browser.getModelsNode() && !FPopupMenu.this.selected.isZipEntry());
      }
   }

   class ActivateModelAction extends AbstractAction implements Validator {
      public ActivateModelAction() {
         this.putValue("Name", "Activate");
      }

      public void actionPerformed(ActionEvent evt) {
         if (FPopupMenu.this.selected.isModelFile() || FPopupMenu.this.selected.isZippedModelFile()) {
            GAbstractProject project = FPopupMenu.this.browser.getProject();
            final File selectedFile = (File)FPopupMenu.this.selected.getUserObject();
            if (selectedFile != null) {
               project.setActiveModelFile(selectedFile.getPath());
               (new Thread() {
                  public void run() {
                     AUMLModelActions.openUML(selectedFile);
                  }
               }).start();
            }
         }

      }

      public void refreshValidation() {
      }
   }

   class OpenFileAction extends AbstractAction {
      public OpenFileAction() {
         if (FPopupMenu.this.selected.isRoot()) {
            this.putValue("Name", "Edit project file");
         } else {
            this.putValue("Name", "Edit");
         }

      }

      public void actionPerformed(ActionEvent evt) {
         String tempDir = System.getProperty("user.home") + System.getProperty("file.separator") + GApplication.APP_TEMP_DIR;
         if (!FPopupMenu.this.selected.isFile()) {
            File projectFile = FPopupMenu.this.browser.getProject().getProjectFile();
            ((AFileActions.OpenFileAction)AFileActions.openFileAction).openFile(projectFile);
         } else {
            FTreeNode parent = (FTreeNode)FPopupMenu.this.selected.getParent();
            if (parent.isZippedModelFile()) {
               File file = (File)parent.getUserObject();

               try {
                  ZipFile zipFile = new ZipFile(file);
                  Enumeration zipEnum = zipFile.entries();

                  while(true) {
                     ZipEntry ze;
                     do {
                        if (!zipEnum.hasMoreElements()) {
                           return;
                        }

                        ze = (ZipEntry)zipEnum.nextElement();
                     } while(!ze.getName().endsWith(FPopupMenu.this.selected.toString()));

                     InputStream ins = zipFile.getInputStream(ze);
                     File outFile = new File(tempDir, ze.getName());
                     File parentDir = outFile.getParentFile();
                     if (!parentDir.exists()) {
                        parentDir.mkdir();
                     }

                     FPopupMenu.this.selected.setUserObject(outFile);
                     FileOutputStream fos = new FileOutputStream(outFile);
                     byte[] bin = new byte[4096];

                     int bread;
                     while((bread = ins.read(bin, 0, 4096)) > -1) {
                        fos.write(bin, 0, bread);
                     }

                     ins.close();
                     fos.close();
                     ((AFileActions.OpenFileAction)AFileActions.openFileAction).openFile(outFile);
                  }
               } catch (Exception var14) {
                  var14.printStackTrace();
               }
            } else {
               ((AFileActions.OpenFileAction)AFileActions.openFileAction).openFile((File)FPopupMenu.this.selected.getUserObject());
            }
         }

      }

      public void refreshValidation() {
      }
   }

   class AddFileAction extends AbstractAction implements Validator {
      private boolean constraints = true;

      public AddFileAction() {
         this.putValue("Name", "Add file(s)");

         FTreeNode node;
         for(node = FPopupMenu.this.selected; node.getParent() != FPopupMenu.this.browser.getRoot(); node = (FTreeNode)node.getParent()) {
         }

         this.constraints = node == FPopupMenu.this.browser.getConstraintsNode();
      }

      public void actionPerformed(ActionEvent evt) {
         AddFilesToProjectWizard aftpw = new AddFilesToProjectWizard(GApplication.frame, this.constraints, !this.constraints);
         aftpw.runWizard();
      }

      public void refreshValidation() {
         this.setEnabled(!FPopupMenu.this.selected.isRoot());
      }
   }

   class NewDirectoryAction extends AbstractAction implements Validator {
      public NewDirectoryAction() {
         this.putValue("Name", "New directory");
      }

      public void actionPerformed(ActionEvent evt) {
         FPopupMenu.this.browser.newDirectory();
      }

      public void refreshValidation() {
         this.setEnabled(true);
         if (FPopupMenu.this.selected == FPopupMenu.this.browser.getRoot()) {
            this.setEnabled(false);
         }

      }
   }
}
