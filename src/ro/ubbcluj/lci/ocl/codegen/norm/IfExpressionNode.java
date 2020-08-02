package ro.ubbcluj.lci.ocl.codegen.norm;

public class IfExpressionNode extends SyntaxTreeNode {
   public IfExpressionNode() {
      super("If");
   }

   public void accept(SyntaxTreeVisitor stv) {
      stv.visitIfExpression(this);
   }
}
