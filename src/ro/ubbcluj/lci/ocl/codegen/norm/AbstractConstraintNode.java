package ro.ubbcluj.lci.ocl.codegen.norm;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class AbstractConstraintNode extends DefaultMutableTreeNode {
   public static final int CK_INV = 0;
   public static final int CK_PRE = 1;
   public static final int CK_POST = 2;
   public static final int CK_DEF = 3;
   protected String constraintName = null;

   public AbstractConstraintNode() {
   }

   public abstract int getKind();

   public final String getStereotypeText() {
      String ret = "definition";
      switch(this.getKind()) {
      case 0:
         ret = "invariant";
         break;
      case 1:
         ret = "precondition";
         break;
      case 2:
         ret = "postcondition";
      }

      return ret;
   }

   public final void setName(String n) {
      this.constraintName = n;
   }

   public String getName() {
      return this.constraintName;
   }

   public void setExpression(SyntaxTreeNode expr) {
      this.removeAllChildren();
      if (expr != null) {
         this.add(expr);
      }

   }

   public SyntaxTreeNode getExpression() {
      return (SyntaxTreeNode)this.getChildAt(0);
   }
}
