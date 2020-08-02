package ro.ubbcluj.lci.ocl.codegen.norm;

public class TypeNode extends SyntaxTreeNode {
   public TypeNode() {
   }

   public void accept(SyntaxTreeVisitor stv) {
      stv.visitType(this);
   }
}
