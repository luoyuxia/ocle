package ro.ubbcluj.lci.ocl.batcheval;

import ro.ubbcluj.lci.gui.tools.tree.SelectionTreeNode;
import ro.ubbcluj.lci.uml.modelManagement.Package;

public class PackageNode extends SelectionTreeNode {
   public PackageNode(Package p) {
      super(p);
   }

   public Package getPackage() {
      return (Package)this.getUserObject();
   }
}
