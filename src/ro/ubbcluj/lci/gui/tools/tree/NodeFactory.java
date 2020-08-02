package ro.ubbcluj.lci.gui.tools.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.modelManagement.Package;

public interface NodeFactory {
   DefaultMutableTreeNode createNode(Classifier var1);

   DefaultMutableTreeNode createNode(Package var1);
}
