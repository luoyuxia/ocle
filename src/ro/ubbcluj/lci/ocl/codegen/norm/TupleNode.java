package ro.ubbcluj.lci.ocl.codegen.norm;

import javax.swing.tree.DefaultMutableTreeNode;

public class TupleNode extends SyntaxTreeNode {
   public TupleNode() {
      super("Tuple literal");
   }

   public void add(DefaultMutableTreeNode c) {
      if (c instanceof TuplePart) {
         super.add(c);
      } else {
         throw new RuntimeException("TupleNodes can only accept TuplePart children!");
      }
   }

   public void accept(SyntaxTreeVisitor stv) {
      stv.visitTuple(this);
   }
}
