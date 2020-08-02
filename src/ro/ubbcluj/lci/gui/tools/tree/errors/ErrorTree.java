package ro.ubbcluj.lci.gui.tools.tree.errors;

import java.util.Enumeration;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import ro.ubbcluj.lci.errors.AbstractEvaluationError;
import ro.ubbcluj.lci.gui.tools.tree.AbstractModelCache;
import ro.ubbcluj.lci.gui.tools.tree.NodeFactory;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.ocl.OclUtil;
import ro.ubbcluj.lci.uml.foundation.core.Class;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.modelManagement.Model;
import ro.ubbcluj.lci.uml.modelManagement.Package;

public class ErrorTree implements NodeFactory {
   private AbstractModelCache userModelCache;
   private AbstractModelCache metamodelCache;
   private JTree tree;
   private boolean clearedErrors = true;

   public ErrorTree(Model umlapi) {
      DefaultTreeModel model;
      this.tree = new JTree(model = new DefaultTreeModel(new DefaultMutableTreeNode("Errors")));
      this.tree.setCellRenderer(new ErrorTreeCellRenderer());
      this.tree.setSelectionPath(this.tree.getPathForRow(0));
      this.metamodelCache = new AbstractModelCache();
      this.metamodelCache.nodeFactory = this;
      this.userModelCache = new AbstractModelCache();
      this.userModelCache.nodeFactory = this;
      DefaultMutableTreeNode metamodelNode = this.metamodelCache.loadTree(umlapi);
      ((DefaultMutableTreeNode)model.getRoot()).add(metamodelNode);
      model.reload();
   }

   public JComponent getComponent() {
      return this.tree;
   }

   public Object getSelectedObject() {
      TreePath sp = this.tree.getSelectionPath();
      if (sp != null) {
         DefaultMutableTreeNode sn = (DefaultMutableTreeNode)sp.getLastPathComponent();
         return sn.getUserObject();
      } else {
         return null;
      }
   }

   public void clearErrors() {
      if (!this.clearedErrors) {
         this.userModelCache.clear();
         this.clearedErrors = true;
         this.clearMetamodelErrors();
         ((DefaultMutableTreeNode)this.tree.getModel().getRoot()).remove(1);
         ((DefaultTreeModel)this.tree.getModel()).reload();
      }
   }

   private void clearMetamodelErrors() {
      DefaultMutableTreeNode nd = (DefaultMutableTreeNode)((DefaultMutableTreeNode)this.tree.getModel().getRoot()).getChildAt(0);
      Stack st = new Stack();
      st.push(nd);

      while(!st.isEmpty()) {
         nd = (DefaultMutableTreeNode)st.pop();
         Object o = nd.getUserObject();
         if (o instanceof Class) {
            nd.removeFromParent();
         } else if (o instanceof OclNode) {
            nd.removeFromParent();
            nd.removeAllChildren();
         }

         Enumeration en = nd.children();

         while(en.hasMoreElements()) {
            st.push(en.nextElement());
         }
      }

   }

   public void setUserModel(Model um) {
      this.clearErrors();
      DefaultMutableTreeNode umn = this.userModelCache.loadTree(um);
      this.clearedErrors = false;
      DefaultTreeModel model;
      ((DefaultMutableTreeNode)(model = (DefaultTreeModel)this.tree.getModel()).getRoot()).add(umn);
      model.reload();
   }

   private void resetErrors(List errors, AbstractModelCache cache) {
      ListIterator it = errors.listIterator();

      while(it.hasNext()) {
         AbstractEvaluationError err = (AbstractEvaluationError)it.next();
         Classifier context = OclUtil.getContext(err.getRule());
         ClassNode nd = (ClassNode)cache.addNode(context);
         OclNode rule;
         DefaultMutableTreeNode rn = nd.getNodeForRule(rule = err.getRule());
         if (rn == null) {
            rn = new DefaultMutableTreeNode(rule);
            nd.add(rn);
         }

         rn.add(new DefaultMutableTreeNode(err));
         boolean finish = false;

         while(!finish && it.hasNext()) {
            AbstractEvaluationError err2 = (AbstractEvaluationError)it.next();
            if (err2.getRule() != err.getRule()) {
               finish = true;
               it.previous();
            } else {
               rn.add(new DefaultMutableTreeNode(err2));
            }
         }
      }

      ((DefaultTreeModel)this.tree.getModel()).reload();
   }

   public DefaultMutableTreeNode createNode(Classifier c) {
      return new ClassNode(c);
   }

   public DefaultMutableTreeNode createNode(Package p) {
      return new DefaultMutableTreeNode(p);
   }

   public void setMetamodelErrors(List errors) {
      this.resetErrors(errors, this.metamodelCache);
   }

   public void setUserModelErrors(List errors) {
      this.resetErrors(errors, this.userModelCache);
   }
}
