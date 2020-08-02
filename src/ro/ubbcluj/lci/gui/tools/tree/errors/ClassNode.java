package ro.ubbcluj.lci.gui.tools.tree.errors;

import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import ro.ubbcluj.lci.ocl.OclNode;

public class ClassNode extends DefaultMutableTreeNode {
   public ClassNode(Object uo) {
      this.setUserObject(uo);
   }

   public DefaultMutableTreeNode getNodeForRule(OclNode rule) {
      Enumeration en = this.children();

      DefaultMutableTreeNode nd;
      do {
         if (!en.hasMoreElements()) {
            return null;
         }

         nd = (DefaultMutableTreeNode)en.nextElement();
      } while(!nd.getUserObject().equals(rule));

      return nd;
   }
}
