package ro.ubbcluj.lci.gui.browser;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import ro.ubbcluj.lci.gui.browser.BTree.BCellRenderer;
import ro.ubbcluj.lci.gui.browser.BTree.BTreeNode;
import ro.ubbcluj.lci.gui.browser.BTree.Filter;
import ro.ubbcluj.lci.gui.browser.BTree.ModelElementOrder;
import ro.ubbcluj.lci.gui.browser.BTree.Order;
import ro.ubbcluj.lci.gui.diagrams.GAbstractDiagram;
import ro.ubbcluj.lci.gui.mainframe.GMainFrame;
import ro.ubbcluj.lci.gui.mainframe.GUMLModel;
import ro.ubbcluj.lci.gui.mainframe.GUMLModelView;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLink;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.AttributeLinkImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.DataValue;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Generalization;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.utils.ModelEvent;
import ro.ubbcluj.lci.utils.ModelFactory;
import ro.ubbcluj.lci.utils.ModelListener;

public class GBrowser extends GUMLModelView implements ModelListener {
   protected JTree browser_tree = new GTree();
   protected transient GBPopupMenu pum;
   private Filter filter = null;
   private static Order order = new ModelElementOrder();

   public GBrowser(GUMLModel gmodel, Filter f) {
      this.filter = f;
      this.setUserObject(gmodel);
      DefaultTreeModel model = this.createTreeModel();
      this.browser_tree.setModel(model);
      this.browser_tree.setCellRenderer(new BCellRenderer());
      this.addTreeListeners();
      this.installPopup();
   }

   protected void installPopup() {
      this.pum = new GBPopupMenu(this);
      this.browser_tree.add(this.pum);
   }

   public JComponent getComponent() {
      this.browser_tree.setMinimumSize(new Dimension(150, 150));
      return this.browser_tree;
   }

   private DefaultTreeModel createTreeModel() {
      GUMLModel gmodel = this.getModel();
      BTreeNode rootNode = new BTreeNode(gmodel.getModel(), this.filter);
      return new DefaultTreeModel(rootNode);
   }

   public void clear() {
      this.browser_tree.setModel((TreeModel)null);
   }

   private void addTreeListeners() {
      this.browser_tree.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent evt) {
            TreePath activePath = GBrowser.this.browser_tree.getClosestPathForLocation(evt.getX(), evt.getY());
            if (activePath != null) {
               TreeNode activeNode = (TreeNode)activePath.getLastPathComponent();
               Object activeUserObject = ((DefaultMutableTreeNode)activeNode).getUserObject();
               if (evt.getClickCount() >= 2 && activeUserObject instanceof GAbstractDiagram) {
                  GAbstractDiagram diagram = (GAbstractDiagram)activeUserObject;
                  GMainFrame.getMainFrame().addNewDiagramFrame(diagram);
               }

            }
         }
      });
      this.browser_tree.addTreeSelectionListener(new TreeSelectionListener() {
         public void valueChanged(TreeSelectionEvent evt) {
            if (GBrowser.this.browser_tree.getSelectionCount() <= 1) {
               BTreeNode selectedNode = (BTreeNode)GBrowser.this.browser_tree.getLastSelectedPathComponent();
               if (selectedNode != null) {
                  Object selectedObject = selectedNode.getUserObject();
                  ModelFactory.fireModelEvent(selectedObject, (Object)null, 0);
               }
            }
         }
      });
   }

   public BTreeNode addDiagram(GAbstractDiagram diagram, ModelElement element, boolean isNew) {
      BTreeNode parent = this.getNodeFor(element);
      String dName = diagram.getName();
      if (isNew) {
         ArrayList names = new ArrayList();

         int suffix;
         for(suffix = 0; suffix < parent.getChildCount(); ++suffix) {
            BTreeNode n = (BTreeNode)parent.getChildAt(suffix);
            if (n.getUserObject() instanceof GAbstractDiagram) {
               GAbstractDiagram d = (GAbstractDiagram)n.getUserObject();
               if (d.getDiagramKind() == diagram.getDiagramKind()) {
                  names.add(d.getName());
               }
            }
         }

         suffix = 0;

         for(dName = "New" + diagram.getDiagramKind() + "Diagram"; names.contains(dName); dName = "New" + diagram.getDiagramKind() + "Diagram" + suffix) {
            ++suffix;
         }

         diagram.setName(dName);
      }

      BTreeNode newNode = new BTreeNode(diagram);
      DefaultTreeModel treeModel = (DefaultTreeModel)this.browser_tree.getModel();
      boolean inserted = false;
      if (parent != null && parent.getChildCount() == 0) {
         treeModel.insertNodeInto(newNode, parent, 0);
         inserted = true;
      }

      int i = 0;
      if (parent != null && parent.getChildCount() > 0) {
         for(; !inserted && i < parent.getChildCount() && ((BTreeNode)parent.getChildAt(i)).getUserObject() instanceof GAbstractDiagram; ++i) {
            GAbstractDiagram tmpDiagram = (GAbstractDiagram)((BTreeNode)parent.getChildAt(i)).getUserObject();
            String tmpName = tmpDiagram.getName();
            if (tmpName.compareTo(dName) >= 0) {
               treeModel.insertNodeInto(newNode, parent, i);
               inserted = true;
            }
         }
      }

      if (parent == null) {
         BTreeNode rootNode = (BTreeNode)treeModel.getRoot();
         treeModel.insertNodeInto(newNode, rootNode, rootNode.getChildCount());
         inserted = true;
      }

      if (!inserted) {
         treeModel.insertNodeInto(newNode, parent, i);
      }

      if (isNew) {
         ModelFactory.fireModelEvent(diagram, element, 0);
      }

      return newNode;
   }

   public BTreeNode addElement(Object parent, ModelElement element) {
      BTreeNode node = this.getNodeFor(parent);
      if (node == null) {
         return null;
      } else {
         BTreeNode newNode = new BTreeNode(element, this.filter);
         if (this.filter.accept((Object)element)) {
            DefaultTreeModel model = (DefaultTreeModel)this.browser_tree.getModel();
            model.insertNodeInto(newNode, node, this.getSmartInsertPosition(node, newNode));
         }

         return newNode;
      }
   }

   public void removeElement(Object element) {
      DefaultTreeModel tModel = (DefaultTreeModel)this.browser_tree.getModel();
      BTreeNode node = this.getNodeFor(element);
      if (node != null) {
         tModel.removeNodeFromParent(node);
      }

   }

   private int getSmartInsertPosition(BTreeNode parent, BTreeNode child) {
      if (child.getUserObject() instanceof GAbstractDiagram) {
         return 0;
      } else if (parent == null) {
         return 0;
      } else {
         Vector children = new Vector();

         int index;
         for(index = 0; index < parent.getChildCount(); ++index) {
            children.add(((BTreeNode)parent.getChildAt(index)).getUserObject());
         }

         order.add(children, (Object)child.getUserObject());
         index = 0;

         for(int i = 0; i < children.size(); ++i) {
            if (children.get(i) == child.getUserObject()) {
               index = i;
            }
         }

         return index;
      }
   }

   public void selectInBrowser(Object obj) {
      GTree tree = (GTree)this.getComponent();
      BTreeNode node = this.getNodeFor(obj);
      if (node == null) {
         tree.clearSelection();
      } else {
         if (node == tree.getModel().getRoot()) {
            TreePath tpath = new TreePath(node);
            tree.setSelectionPath(tpath);
            tree.scrollPathToVisible(tpath);
         } else {
            ArrayList path = node.getFullPath();
            if (path.toArray().length > 0) {
               tree.expandPath(new TreePath(path.toArray()));
               path.add(node);
               TreePath tpath = new TreePath(path.toArray());
               tree.setSelectionPath(tpath);
               tree.scrollPathToVisible(tpath);
            }
         }

         GMainFrame.getMainFrame().focusBrowser(this);
      }
   }

   protected TreePath getPathFor(Object obj) {
      GTree tree = (GTree)this.getComponent();
      TreePath tpath = null;
      BTreeNode node = tree.getNodeFor(obj);
      if (node == tree.getModel().getRoot()) {
         tpath = new TreePath(node);
      } else if (node != null) {
         ArrayList path = node.getFullPath();
         if (path.toArray().length > 0) {
            tree.expandPath(new TreePath(path.toArray()));
            path.add(node);
            tpath = new TreePath(path.toArray());
         }
      }

      return tpath;
   }

   public BTreeNode getNodeFor(Object o) {
      Enumeration nodes = ((BTreeNode)this.browser_tree.getModel().getRoot()).breadthFirstEnumeration();

      BTreeNode node;
      do {
         if (!nodes.hasMoreElements()) {
            return null;
         }

         node = (BTreeNode)nodes.nextElement();
      } while(node.getUserObject() != o);

      return node;
   }

   public void refresh() {
      DefaultTreeModel treeModel = this.createTreeModel();
      this.browser_tree.setModel(treeModel);
      List diags = this.getModel().getDiagrams();

      for(int i = 0; i < diags.size(); ++i) {
         GAbstractDiagram diag = (GAbstractDiagram)diags.get(i);
         this.addDiagram(diag, diag.getContext(), false);
      }

   }

   public Filter getFilter() {
      return this.filter;
   }

   public void setFilter(Filter f) {
      this.filter = f;
      this.refresh();
   }

   public static Order getOrder() {
      return order;
   }

   public void modelChanged(ModelEvent evt) {
      DefaultTreeModel dtm = (DefaultTreeModel)this.browser_tree.getModel();
      switch(evt.getOperation()) {
      case 0:
         this.selectInBrowser(evt.getSubject());
         break;
      case 10:
         if (!(evt.getContext() instanceof Attribute)) {
            this.addElement(evt.getContext(), (ModelElement)evt.getSubject());
         }
         break;
      case 20:
      case 21:
         Object obj = evt.getSubject();
         if (obj instanceof DataValue) {
            obj = ((DataValue)obj).getAttributeLinkList().nextElement();
         }

         BTreeNode node = this.getNodeFor(obj);
         if (node == null) {
            return;
         }

         if (evt.getOperation() == 20) {
            dtm.nodeChanged(node);
         } else {
            node.refresh(this.getFilter());
            dtm.nodeStructureChanged(node);
         }

         if (node != dtm.getRoot()) {
            BTreeNode parent = (BTreeNode)node.getParent();
            dtm.removeNodeFromParent(node);
            boolean added = false;

            for(int i = 0; i < parent.getChildCount(); ++i) {
               BTreeNode tmpNode = (BTreeNode)parent.getChildAt(i);
               if (order.compare(tmpNode.getUserObject(), node.getUserObject()) > 0) {
                  dtm.insertNodeInto(node, parent, i);
                  added = true;
                  break;
               }
            }

            if (!added) {
               dtm.insertNodeInto(node, parent, parent.getChildCount());
            }
         }
         break;
      case 30:
         this.removeElement(evt.getSubject());
         break;
      case 40:
         if (evt.getContext() instanceof Classifier) {
            Classifier tmpClassifier = (Classifier)evt.getContext();
            Collection classifiers = new ArrayList();
            classifiers.add(tmpClassifier);
            Iterator classifierIt = tmpClassifier.getCollectionSpecializationList().iterator();

            while(classifierIt.hasNext()) {
               classifiers.add(((Generalization)classifierIt.next()).getChild());
            }

            classifierIt = classifiers.iterator();

            while(classifierIt.hasNext()) {
               Classifier tmpCls = (Classifier)classifierIt.next();
               Iterator it = tmpCls.getCollectionInstanceList().iterator();

               while(it.hasNext()) {
                  Instance tmpInstance = (Instance)it.next();
                  AttributeLink tmpAttrLink = new AttributeLinkImpl();
                  Attribute tmpAttr = (Attribute)evt.getSubject();
                  tmpAttrLink.setName(tmpAttr.getName());
                  tmpAttrLink.setAttribute(tmpAttr);
                  tmpAttrLink.setInstance(tmpInstance);
                  ModelFactory.fireModelEvent(tmpAttrLink, tmpInstance, 10);
               }
            }
         }
      }

   }
}
