package ro.ubbcluj.lci.ocl.codegen.norm;

public final class Qualifier {
   private boolean simple;
   private SyntaxTreeNode expression;
   private String endName;

   public Qualifier() {
      this(false);
   }

   public Qualifier(boolean isSimple) {
      this.simple = isSimple;
   }

   public Qualifier(String name) {
      this(false);
      this.endName = name;
   }

   public void setExpression(SyntaxTreeNode exprRoot) {
      if (this.simple) {
         throw new InternalError("Operation not supported for simple qualifiers");
      } else {
         this.expression = exprRoot;
      }
   }

   public void setEndName(String name) {
      if (!this.simple) {
         throw new InternalError("Operation not supported");
      } else {
         this.endName = name;
      }
   }

   public boolean isSimple() {
      return this.simple;
   }

   public SyntaxTreeNode getExpression() {
      return this.simple ? null : this.expression;
   }

   public String getEndName() {
      return this.endName;
   }
}
