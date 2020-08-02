package ro.ubbcluj.lci.ocl.codegen.norm;

import ro.ubbcluj.lci.ocl.OclType;

public class PostfixExpressionNode extends SyntaxTreeNode {
   public PostfixExpressionNode() {
      super("Postfix");
   }

   public void accept(SyntaxTreeVisitor stv) {
      stv.visitPostfixExpression(this);
   }

   public OclType getType() {
      if (this.type == null) {
         this.type = ((SyntaxTreeNode)this.getLastChild()).getType();
      }

      return this.type;
   }
}
