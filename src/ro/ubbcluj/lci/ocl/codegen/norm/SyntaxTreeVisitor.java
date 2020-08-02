package ro.ubbcluj.lci.ocl.codegen.norm;

public abstract class SyntaxTreeVisitor {
   public SyntaxTreeVisitor() {
   }

   public abstract void visitCollection(CollectionNode var1);

   public abstract void visitIfExpression(IfExpressionNode var1);

   public abstract void visitLetExpression(LetExpressionNode var1);

   public abstract void visitLiteral(LiteralNode var1);

   public abstract void visitOperator(OperatorNode var1);

   public abstract void visitPostfixExpression(PostfixExpressionNode var1);

   public abstract void visitPropertyCall(PropertyCallNode var1);

   public abstract void visitTuple(TupleNode var1);

   public abstract void visitTuplePart(TuplePart var1);

   public abstract void visitType(TypeNode var1);
}
