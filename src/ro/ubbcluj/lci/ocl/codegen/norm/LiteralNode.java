package ro.ubbcluj.lci.ocl.codegen.norm;

public class LiteralNode extends SyntaxTreeNode {
   public LiteralNode() {
   }

   public void accept(SyntaxTreeVisitor stv) {
      stv.visitLiteral(this);
   }
}
