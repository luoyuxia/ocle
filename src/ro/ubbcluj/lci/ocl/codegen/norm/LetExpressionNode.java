package ro.ubbcluj.lci.ocl.codegen.norm;

import ro.ubbcluj.lci.ocl.OclType;

public class LetExpressionNode extends SyntaxTreeNode {
   private SyntaxTreeNode realExpression;

   public LetExpressionNode() {
      super("Let");
   }

   public DefinitionConstraintNode[] getLocalDefinitions() {
      int n = this.getChildCount();
      DefinitionConstraintNode[] ret = new DefinitionConstraintNode[n];

      for(int i = 0; i < n; ++i) {
         ret[i] = (DefinitionConstraintNode)this.getChildAt(i);
      }

      return ret;
   }

   public void addLocalDefinition(DefinitionConstraintNode ld) {
      this.add(ld);
   }

   public void setRealExpression(SyntaxTreeNode re) {
      this.realExpression = re;
   }

   public SyntaxTreeNode getRealExpression() {
      return this.realExpression;
   }

   public void accept(SyntaxTreeVisitor stv) {
      stv.visitLetExpression(this);
   }

   public OclType getType() {
      return this.realExpression != null ? this.realExpression.getType() : null;
   }
}
