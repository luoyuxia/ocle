package ro.ubbcluj.lci.ocl.batcheval;

import javax.swing.tree.DefaultMutableTreeNode;
import ro.ubbcluj.lci.gui.tools.tree.NodeFactory;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.modelManagement.Package;

class SelectionTreeNodeFactory implements NodeFactory {
   SelectionTreeNodeFactory() {
   }

   public DefaultMutableTreeNode createNode(Package p) {
      return new PackageNode(p);
   }

   public DefaultMutableTreeNode createNode(Classifier c) {
      return new ClassNode(c);
   }
}
