package ro.ubbcluj.lci.ocl.codegen.norm;

public class OperatorNode extends SyntaxTreeNode {
   public OperatorNode(String text) {
      super(text);
   }

   public String getText() {
      return (String)this.userObject;
   }

   public void accept(SyntaxTreeVisitor stv) {
      stv.visitOperator(this);
   }
}
