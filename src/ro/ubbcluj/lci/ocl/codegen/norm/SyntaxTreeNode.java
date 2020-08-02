package ro.ubbcluj.lci.ocl.codegen.norm;

import javax.swing.tree.DefaultMutableTreeNode;
import ro.ubbcluj.lci.ocl.OclType;

public abstract class SyntaxTreeNode extends DefaultMutableTreeNode {
   protected OclType type;
   private SyntaxCodeInfo codeInfo;

   public SyntaxTreeNode() {
   }

   public SyntaxTreeNode(Object uo) {
      super(uo);
   }

   public void setType(OclType t) {
      this.type = t;
   }

   public OclType getType() {
      return this.type;
   }

   public void setCodeInfo(SyntaxCodeInfo ci) {
      this.codeInfo = ci;
   }

   public SyntaxCodeInfo getCodeInfo() {
      return this.codeInfo;
   }

   public abstract void accept(SyntaxTreeVisitor var1);
}
