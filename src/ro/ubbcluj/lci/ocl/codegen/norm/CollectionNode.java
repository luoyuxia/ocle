package ro.ubbcluj.lci.ocl.codegen.norm;

public class CollectionNode extends SyntaxTreeNode {
   public CollectionNode(String collKind) {
      super(collKind);
   }

   public void accept(SyntaxTreeVisitor stv) {
      stv.visitCollection(this);
   }
}
