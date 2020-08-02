package ro.ubbcluj.lci.gui.browser;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import ro.ubbcluj.lci.gui.browser.BTree.BTreeNode;
import ro.ubbcluj.lci.gui.browser.BTree.BrowserFilterDialog;
import ro.ubbcluj.lci.gui.browser.BTree.Filter;
import ro.ubbcluj.lci.gui.diagrams.ClassCell;
import ro.ubbcluj.lci.gui.diagrams.ClassDiagram;
import ro.ubbcluj.lci.gui.diagrams.Diagram;
import ro.ubbcluj.lci.gui.diagrams.GAbstractDiagram;
import ro.ubbcluj.lci.gui.diagrams.filters.ClassFilterDialog;
import ro.ubbcluj.lci.gui.mainframe.GApplication;
import ro.ubbcluj.lci.gui.mainframe.GMainFrame;
import ro.ubbcluj.lci.gui.mainframe.GRepository;
import ro.ubbcluj.lci.gui.mainframe.GUMLModel;
import ro.ubbcluj.lci.gui.tools.AFileFilter;
import ro.ubbcluj.lci.gui.tools.GResourceBundle;
import ro.ubbcluj.lci.gui.tools.JDialogChooser;
import ro.ubbcluj.lci.gui.tools.ProgressBar;
import ro.ubbcluj.lci.ocl.ImportJar;
import ro.ubbcluj.lci.redtd.DTDToUMLFacade;
import ro.ubbcluj.lci.redtd.ReDTDModelCache;
import ro.ubbcluj.lci.redtd.UMLTransformer;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Collaboration;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLink;
import ro.ubbcluj.lci.uml.foundation.core.AssociationClass;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Element;
import ro.ubbcluj.lci.uml.foundation.core.ElementOwnership;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.Stereotype;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TaggedValue;
import ro.ubbcluj.lci.uml.modelManagement.Model;
import ro.ubbcluj.lci.utils.CreateObjectDialog;
import ro.ubbcluj.lci.utils.CreateStereotypeDialog;
import ro.ubbcluj.lci.utils.CreateTaggedValueDialog;
import ro.ubbcluj.lci.utils.EditStereotypesDialog;
import ro.ubbcluj.lci.utils.ModelFactory;

public class GBPopupMenu extends JPopupMenu {
   protected GBrowser gbrowser;
   private static GResourceBundle resources = new GResourceBundle("ro.ubbcluj.lci.gui.browser.browserPopup");
   private static GResourceBundle menuRes = new GResourceBundle("ro.ubbcluj.lci.gui.browser.contextMenu");
   private JTree browser;

   public GBPopupMenu(GBrowser comp) {
      this.browser = (JTree)comp.getComponent();
      this.gbrowser = comp;
      this.browser.addMouseListener(new GBPopupMenu.PopupListener());
   }

   void refreshValidation(JPopupMenu pum) {
      Component[] all = pum.getComponents();

      for(int i = 0; i < all.length; ++i) {
         if (all[i] instanceof Validator) {
            ((Validator)all[i]).refreshValidation();
         }
      }

   }

   protected JPopupMenu createPopup(Object element) {
      JPopupMenu pum = new JPopupMenu();
      if (element instanceof Diagram) {
         pum.add(new GBPopupMenu.EditDiagramAction());
         if (element instanceof ClassDiagram) {
            pum.add(new GBPopupMenu.FilterClassDiagramAction());
         }

         pum.add(new GMenuItem(new GBPopupMenu.DeleteAction()));
         return pum;
      } else {
         if (!this.gbrowser.getModel().isReadOnly()) {
            String s = menuRes.getResourceString(((Element)element).getMetaclassName());
            if (s != null) {
               String[] items = menuRes.tokenize(s);
               GMenu newMenu = new GMenu("New");

               for(int i = 0; i < items.length; ++i) {
                  if (items[i].equals("-")) {
                     newMenu.addSeparator();
                  } else if (items[i].endsWith("Diagram")) {
                     newMenu.add(new GMenuItem(new GBPopupMenu.NewDiagramAction(items[i])));
                  } else {
                     newMenu.add(new GMenuItem(new GBPopupMenu.NewElementAction(items[i])));
                  }
               }

               pum.add(newMenu);
            }

            if (!(element instanceof Diagram) && !(element instanceof Stereotype)) {
               pum.add(new GMenuItem(new GBPopupMenu.EditStereotypesAction(element)));
            }

            if (element instanceof ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Object || element instanceof Stereotype) {
               pum.add(new GMenuItem(new GBPopupMenu.EditAction(((ModelElement)element).getMetaclassName())));
            }

            if (element instanceof Collaboration) {
               pum.add(new GMenuItem(new GBPopupMenu.ImportObjectsFromXMLAction(element)));
               pum.add(new GMenuItem(new GBPopupMenu.ExportObjectsToXMLAction(element)));
            }

            pum.add(new GMenuItem(new GBPopupMenu.DeleteAction()));
            pum.addSeparator();
         }

         pum.add(new GMenuItem(new GBPopupMenu.FullImportAction()));
         pum.addSeparator();
         pum.add(new GMenuItem(new GBPopupMenu.BrowserFilterAction()));
         pum.add(new GMenuItem(new GBPopupMenu.ExpandTreeNodeAction()));
         pum.add(new GMenuItem(new GBPopupMenu.ColapseTreeNodeAction()));
         pum.addSeparator();
         pum.add(new GMenuItem(new GBPopupMenu.ShowInDiagramAction()));
         pum.add(new GMenuItem(new GBPopupMenu.FindAction()));
         return pum;
      }
   }

   public static class ExportObjectsToXMLAction extends AbstractAction {
      private Collaboration collaboration;

      private ExportObjectsToXMLAction(Object collaboration) {
         this.putValue("Name", "Export Objects to XML");
         if (collaboration != null && collaboration instanceof Collaboration) {
            this.collaboration = (Collaboration)collaboration;
         }

      }

      public void actionPerformed(ActionEvent e) {
         try {
            AFileFilter[] ff = new AFileFilter[]{new AFileFilter("xml", "XML files (*.xml)")};
            final File file = AFileFilter.chooseFile(1, GMainFrame.getMainFrame(), ff, "Export XML");
            if (file != null) {
               if (this.collaboration == null) {
                  GMainFrame.getMainFrame().updateLog("You have to choose a collaboration in order to export XML objects.");
                  return;
               }

               (new Thread() {
                  public void run() {
                     try {
                        ReDTDModelCache.getInstance().setXmlCollaboration(ExportObjectsToXMLAction.this.collaboration);
                        UMLTransformer.getInstance(1).transformModel(GRepository.getInstance().getUsermodel().getModel(), file);
                     } catch (Exception var6) {
                        var6.printStackTrace();
                        GMainFrame.getMainFrame().updateMessages((Object)var6.getLocalizedMessage());
                     } finally {
                        GMainFrame.getMainFrame().updateModelActions();
                        GMainFrame.getMainFrame().updateLog("XML objects saved as XML in " + file.getAbsolutePath() + ".\n");
                     }

                  }
               }).start();
            }
         } catch (Exception var4) {
            var4.printStackTrace();
         }

      }
   }

   public static class ImportObjectsFromXMLAction extends AbstractAction {
      private Collaboration collaboration;

      private ImportObjectsFromXMLAction(Object collaboration) {
         this.collaboration = null;
         this.putValue("Name", "Import Objects from XML");
         if (collaboration != null && collaboration instanceof Collaboration) {
            this.collaboration = (Collaboration)collaboration;
         }

      }

      public void actionPerformed(ActionEvent evt) {
         if (this.collaboration == null) {
            GMainFrame.getMainFrame().updateLog("You have to create or choose a collaboration in order to import XML objects.");
         } else {
            AFileFilter[] ff = new AFileFilter[]{new AFileFilter("xml", "XML files (*.xml)")};
            GMainFrame mainframe = GMainFrame.getMainFrame();
            final File f = AFileFilter.chooseFile(0, mainframe, ff, "Select an XML file");
            if (f != null) {
               (new Thread() {
                  public void run() {
                     ProgressBar pb = null;

                     try {
                        pb = ProgressBar.getProgressBar(GApplication.frame);
                        String xmlFilename = f.getName();
                        if (xmlFilename.toLowerCase().endsWith(".xml")) {
                           pb.setColor(ProgressBar.OPEN_COLOR);
                           pb.setIncrement(-1);
                           pb.disableProgressString();
                           pb.startOld("Loading XML objects..");
                           long time1 = System.currentTimeMillis();
                           ReDTDModelCache.getInstance().setXmlCollaboration(ImportObjectsFromXMLAction.this.collaboration);
                           (new DTDToUMLFacade()).importXMLObjects(f);
                           long time2 = System.currentTimeMillis();
                           GMainFrame.getMainFrame().updateLog("XML objects loaded in " + (time2 - time1) + " miliseconds.\n");
                           GRepository.getInstance().getUsermodel().getBrowser().refresh();
                        } else {
                           GMainFrame.getMainFrame().updateLog("The specified file (" + f.getAbsolutePath() + ") should have xml extension");
                        }
                     } catch (Exception var10) {
                        var10.printStackTrace();
                        GMainFrame.getMainFrame().updateMessages((Object)var10.getMessage());
                     } finally {
                        pb.stopOld();
                        pb.enableProgressString();
                     }

                  }
               }).start();
            }

         }
      }
   }

   private class EditDiagramAction extends AbstractAction {
      public EditDiagramAction() {
         this.putValue("Name", "Open");
      }

      public void actionPerformed(ActionEvent evt) {
         GAbstractDiagram diagToActivate = (GAbstractDiagram)((DefaultMutableTreeNode)GBPopupMenu.this.browser.getLastSelectedPathComponent()).getUserObject();
         GMainFrame.getMainFrame().addNewDiagramFrame(diagToActivate);
      }
   }

   class FullImportAction extends AbstractAction implements Validator {
      public FullImportAction() {
         String name = "Reverse-engineer class";
         this.putValue("Name", name);
      }

      public void actionPerformed(ActionEvent a) {
         BTreeNode node = (BTreeNode)GBPopupMenu.this.browser.getLastSelectedPathComponent();
         Object obj = node.getUserObject();
         if (obj instanceof Classifier) {
            Enumeration en = ((Classifier)obj).getTaggedValueList();

            while(en.hasMoreElements()) {
               TaggedValue tv = (TaggedValue)en.nextElement();
               if ("jar".equals(tv.getName())) {
                  ImportJar.importJarClass(tv.getDataValue(), (Classifier)obj);
               }
            }
         }

         ModelFactory.fireModelEvent(obj, (Object)null, 21);
      }

      public void refreshValidation() {
         BTreeNode tn = (BTreeNode)GBPopupMenu.this.browser.getLastSelectedPathComponent();
         this.setEnabled(false);
         Object obj = tn.getUserObject();
         if (obj instanceof Classifier) {
            Enumeration en = ((Classifier)obj).getTaggedValueList();

            while(en.hasMoreElements()) {
               TaggedValue tv = (TaggedValue)en.nextElement();
               if ("jar".equals(tv.getName())) {
                  this.setEnabled(true);
               }
            }
         }

      }
   }

   class BrowserFilterAction extends AbstractAction {
      public BrowserFilterAction() {
         this.putValue("Name", "Filter");
      }

      public void actionPerformed(ActionEvent evt) {
         try {
            BrowserFilterDialog dialog = new BrowserFilterDialog(GBPopupMenu.this.gbrowser.getFilter());
            dialog.setBounds(300, 200, 580, 500);
            dialog.setVisible(true);
            if (dialog.wereChangesMade()) {
               Filter newFilter = dialog.getFilter();
               GBPopupMenu.this.gbrowser.setFilter(newFilter);
               GRepository.getInstance().setCurrentBrowserFilter(newFilter);
            }
         } catch (Exception var4) {
         }

      }
   }

   class FilterClassDiagramAction extends AbstractAction {
      public FilterClassDiagramAction() {
         this.putValue("Name", "Filter classes");
      }

      public void actionPerformed(ActionEvent evt) {
         GUMLModel gmodel = GBPopupMenu.this.gbrowser.getModel();
         BTreeNode node = (BTreeNode)GBPopupMenu.this.browser.getLastSelectedPathComponent();
         if (node.getUserObject() instanceof ClassDiagram) {
            ClassDiagram diag = (ClassDiagram)node.getUserObject();
            ClassFilterDialog.show(diag, (ClassCell)null);
         }
      }
   }

   class ShowInDiagramAction extends AbstractAction {
      JDialogChooser dialog;
      Element el;
      List diags;
      JList list;

      public ShowInDiagramAction() {
         this.putValue("Name", "Show in diagram");
      }

      public void actionPerformed(ActionEvent evt) {
         TreePath selPath = GBPopupMenu.this.browser.getSelectionPath();
         if (selPath != null) {
            BTreeNode node = (BTreeNode)selPath.getLastPathComponent();
            this.el = (Element)node.getUserObject();
            this.diags = GBPopupMenu.this.gbrowser.getModel().getDiagramsFor(this.el);
            if (this.diags.size() == 1) {
               GAbstractDiagram diag = (GAbstractDiagram)this.diags.get(0);
               if (diag.getView() == null) {
                  GMainFrame.getMainFrame().addExistingDiagramFrame(diag);
               }
            } else if (this.diags.size() > 1) {
               this.showDialog();
            }

         }
      }

      private void showDialog() {
         this.dialog = new JDialogChooser("Open diagram");
         this.dialog.setListContent(this.diags.toArray());
         this.dialog.setSelectionMode(2);
         this.dialog.setInfo("Choose a diagram containing element: " + this.el);
         this.dialog.show();
         if (this.dialog.getExitCode() == 1) {
            Object[] sel = this.dialog.getSelectedValues();

            for(int i = 0; i < sel.length; ++i) {
               GMainFrame.getMainFrame().addExistingDiagramFrame((GAbstractDiagram)sel[i]);
            }
         }

      }
   }

   class FindAction extends AbstractAction implements Validator {
      public FindAction() {
         String name = GBPopupMenu.resources.getResourceString("FindLabel");
         this.putValue("Name", name);
      }

      public void actionPerformed(ActionEvent evt) {
         GFind findDialog = new GFind(GBPopupMenu.this.gbrowser);
         findDialog.setBounds(300, 200, 540, 450);
         findDialog.setVisible(true);
      }

      public void refreshValidation() {
      }
   }

   class ColapseTreeNodeAction extends AbstractAction implements Validator {
      public ColapseTreeNodeAction() {
         String name = GBPopupMenu.resources.getResourceString("ColapseTreeNodeLabel");
         this.putValue("Name", name);
      }

      public void actionPerformed(ActionEvent e) {
         GBPopupMenu.this.browser.collapsePath(GBPopupMenu.this.browser.getSelectionPath());
      }

      public void refreshValidation() {
         this.setEnabled(true);
         BTreeNode tn = (BTreeNode)GBPopupMenu.this.browser.getLastSelectedPathComponent();
         if (tn.isLeaf()) {
            this.setEnabled(false);
         }

         TreePath path = GBPopupMenu.this.browser.getLeadSelectionPath();
         if (GBPopupMenu.this.browser.isCollapsed(path)) {
            this.setEnabled(false);
         }

      }
   }

   class ExpandTreeNodeAction extends AbstractAction implements Validator {
      public ExpandTreeNodeAction() {
         String name = GBPopupMenu.resources.getResourceString("ExpandTreeNodeLabel");
         this.putValue("Name", name);
      }

      public void actionPerformed(ActionEvent e) {
         BTreeNode tn = (BTreeNode)GBPopupMenu.this.browser.getLastSelectedPathComponent();
         if (!tn.isExplored()) {
            tn.explore(GBPopupMenu.this.gbrowser.getFilter());
         }

         Thread t = new Thread() {
            public void run() {
               ExpandTreeNodeAction.this.fullExpand(GBPopupMenu.this.browser.getSelectionPath());
            }
         };
         t.run();
      }

      private void fullExpand(TreePath path) {
         GBPopupMenu.this.browser.expandPath(path);
         BTreeNode node = (BTreeNode)path.getLastPathComponent();

         for(int i = 0; i < node.getChildCount(); ++i) {
            BTreeNode child = (BTreeNode)node.getChildAt(i);
            TreePath newPath = path.pathByAddingChild(child);
            this.fullExpand(newPath);
         }

      }

      public void refreshValidation() {
         BTreeNode tn = (BTreeNode)GBPopupMenu.this.browser.getLastSelectedPathComponent();
         this.setEnabled(true);
         if (tn.isLeaf()) {
            this.setEnabled(false);
         }

         TreePath path = GBPopupMenu.this.browser.getLeadSelectionPath();
         if (GBPopupMenu.this.browser.isExpanded(path)) {
            this.setEnabled(false);
         }

      }
   }

   class EditStereotypesAction extends AbstractAction {
      protected Object element;

      public EditStereotypesAction(Object element) {
         this.putValue("Name", "Edit stereotypes");
         this.element = element;
      }

      public void actionPerformed(ActionEvent e) {
         EditStereotypesDialog editDialog = new EditStereotypesDialog((ModelElement)this.element);
         editDialog.setLocation(100, 100);
         editDialog.setSize(500, 350);
         editDialog.setVisible(true);
         if (editDialog.getReturnCode() == 1) {
            editDialog.update();
         }

         ModelFactory.fireModelEvent(this.element, (Object)null, 20);
         ModelFactory.fireModelEvent(this.element, (Object)null, 0);
      }
   }

   class EditAction extends AbstractAction implements Validator {
      private String type;

      public EditAction(String type) {
         this.putValue("Name", "Edit " + type);
         this.type = type;
      }

      public void actionPerformed(ActionEvent evt) {
         BTreeNode tn = (BTreeNode)GBPopupMenu.this.browser.getLastSelectedPathComponent();
         if (!this.type.equals("Object") && !this.type.equals("LinkObject")) {
            if (this.type.equals("Stereotype")) {
               JDialog d = new CreateStereotypeDialog(GApplication.frame, (Stereotype)tn.getUserObject());
               d.setLocation(300, 300);
               d.setVisible(true);
            }
         } else {
            CreateObjectDialog.editObject((ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Object)tn.getUserObject());
         }

      }

      public void refreshValidation() {
      }
   }

   class NewElementAction extends AbstractAction implements Validator {
      private String type;

      public NewElementAction(String type) {
         this.putValue("Name", type);
         this.type = type;
      }

      public void actionPerformed(ActionEvent e) {
         BTreeNode tn = (BTreeNode)GBPopupMenu.this.browser.getLastSelectedPathComponent();
         Object var3;
         if (this.type.equals("Object")) {
            var3 = CreateObjectDialog.createInstance((Collaboration)tn.getUserObject());
         } else if (this.type.equals("TaggedValue")) {
            var3 = CreateTaggedValueDialog.createTaggedValue((ModelElement)tn.getUserObject());
         } else if (this.type.equals("Stereotype")) {
            JDialog d = new CreateStereotypeDialog(GApplication.frame, (Stereotype)null);
            d.setLocation(300, 300);
            d.setVisible(true);
         } else {
            var3 = ModelFactory.createNewElement(tn.getUserObject(), this.type);
         }

      }

      public void refreshValidation() {
         Object obj = ((BTreeNode)GBPopupMenu.this.browser.getLastSelectedPathComponent()).getUserObject();
         if (obj instanceof Classifier) {
            Classifier cls = (Classifier)obj;
            if (!this.type.equals("Instance") || !cls.isAbstract() && !(cls instanceof AssociationClass)) {
               this.setEnabled(true);
            } else {
               this.setEnabled(false);
            }
         }

      }
   }

   class NewDiagramAction extends AbstractAction implements Validator {
      private String type;

      public NewDiagramAction(String type) {
         String name = GBPopupMenu.resources.getResourceString(type + "Label");
         this.putValue("Name", name);
         this.type = type;
      }

      public void actionPerformed(ActionEvent e) {
         GUMLModel gmodel;
         Object context;
         if (this.type.startsWith("Class")) {
            gmodel = GBPopupMenu.this.gbrowser.getModel();
            context = ((BTreeNode)GBPopupMenu.this.browser.getSelectionPath().getLastPathComponent()).getUserObject();
            gmodel.addDiagram(context, "Class");
         } else if (this.type.startsWith("Object")) {
            gmodel = GBPopupMenu.this.gbrowser.getModel();
            context = ((BTreeNode)GBPopupMenu.this.browser.getSelectionPath().getLastPathComponent()).getUserObject();
            gmodel.addDiagram(context, "Object");
         } else if (this.type.startsWith("UseCase")) {
            gmodel = GBPopupMenu.this.gbrowser.getModel();
            TreePath path = GBPopupMenu.this.browser.getSelectionPath();
            BTreeNode last = (BTreeNode)path.getLastPathComponent();
            Model current = (Model)last.getUserObject();
            if (ModelFactory.hasStereotype(current, "useCaseModel", "Model")) {
               gmodel.addDiagram(current, "UseCase");
               return;
            }

            Enumeration enum = current.getOwnedElementList();

            while(true) {
               ModelElement me;
               BTreeNode bn;
               Object[] pc;
               Object[] npc;
               int i;
               do {
                  do {
                     if (!enum.hasMoreElements()) {
                        Model useCaseModel = (Model)ModelFactory.createNewElement(gmodel.getModel(), "Model");
                        ModelFactory.setAttribute(useCaseModel, "Name", "UseCaseView");
                        Stereotype stereotype = (Stereotype)ModelFactory.createNewElement(gmodel.getModel(), "Stereotype");
                        String baseClass = stereotype.getBaseClass();
                        stereotype.setBaseClass(baseClass != null ? baseClass + " Model" : "Model");
                        ModelFactory.setAttribute(stereotype, "Name", "useCaseModel");
                        useCaseModel.addStereotype(stereotype);
                        bn = GBPopupMenu.this.gbrowser.getNodeFor(useCaseModel);
                        pc = path.getPath();
                        npc = new Object[pc.length + 1];

                        for(i = 0; i < pc.length; ++i) {
                           npc[i] = pc[i];
                        }

                        npc[npc.length - 1] = bn;
                        new TreePath(npc);
                        gmodel.addDiagram(useCaseModel, "UseCase");
                        return;
                     }

                     ElementOwnership eo = (ElementOwnership)enum.nextElement();
                     me = eo.getOwnedElement();
                  } while(!(me instanceof Model));
               } while(!ModelFactory.hasStereotype(me, "useCaseModel", "Model"));

               Enumeration enum2 = last.children();

               while(enum2.hasMoreElements()) {
                  bn = (BTreeNode)enum2.nextElement();
                  if (me == bn.getUserObject()) {
                     pc = path.getPath();
                     npc = new Object[pc.length + 1];

                     for(i = 0; i < pc.length; ++i) {
                        npc[i] = pc[i];
                     }

                     npc[npc.length - 1] = bn;
                     new TreePath(npc);
                     gmodel.addDiagram(me, "UseCase");
                     return;
                  }
               }
            }
         }

      }

      public void refreshValidation() {
      }
   }

   class DeleteAction extends AbstractAction implements Validator {
      public DeleteAction() {
         String name = GBPopupMenu.resources.getResourceString("DeleteDiagramLabel");
         this.putValue("Name", name);
      }

      public void actionPerformed(ActionEvent a) {
         GUMLModel gmodel = GBPopupMenu.this.gbrowser.getModel();
         BTreeNode node = (BTreeNode)GBPopupMenu.this.browser.getLastSelectedPathComponent();
         TreePath path = GBPopupMenu.this.browser.getSelectionPath();
         if (node.getUserObject() instanceof GAbstractDiagram) {
            GAbstractDiagram diag = (GAbstractDiagram)node.getUserObject();
            ModelFactory.fireModelEvent(diag, diag.getContext(), 30);
            gmodel.removeDiagram(diag);
         } else if (node.getUserObject() instanceof ModelElement) {
            ModelElement toRemove = (ModelElement)((BTreeNode)node.getParent()).getUserObject();
            ModelFactory.removeElement((ModelElement)node.getUserObject(), toRemove);
         }

      }

      public void refreshValidation() {
         BTreeNode tn = (BTreeNode)GBPopupMenu.this.browser.getLastSelectedPathComponent();
         this.setEnabled(true);
         if (tn.getParent() == null) {
            this.setEnabled(false);
         }

         if (tn.getUserObject() instanceof AssociationEnd) {
            this.setEnabled(false);
         }

         if (tn.getUserObject() instanceof AttributeLink) {
            this.setEnabled(false);
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
            TreePath to_select = GBPopupMenu.this.browser.getClosestPathForLocation(e.getX(), e.getY());
            GBPopupMenu.this.browser.setSelectionPath(to_select);
            BTreeNode obj = (BTreeNode)GBPopupMenu.this.browser.getLastSelectedPathComponent();
            Object uo = obj.getUserObject();
            JPopupMenu pum = GBPopupMenu.this.createPopup(uo);
            GBPopupMenu.this.refreshValidation(pum);
            pum.show(e.getComponent(), e.getX(), e.getY());
         }

      }
   }
}
