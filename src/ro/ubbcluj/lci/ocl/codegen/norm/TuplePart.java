package ro.ubbcluj.lci.ocl.codegen.norm;

public class TuplePart extends SyntaxTreeNode {
   public TuplePart(String name, SyntaxTreeNode body) {
      super(name);
      this.add(body);
   }

   public String getName() {
      return (String)this.userObject;
   }

   public SyntaxTreeNode getBody() {
      return (SyntaxTreeNode)this.getChildAt(0);
   }

   public void accept(SyntaxTreeVisitor stv) {
      stv.visitTuplePart(this);
   }
}
