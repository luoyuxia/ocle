package ro.ubbcluj.lci.ocl.codegen.norm;

public class BasicConstraintNode extends AbstractConstraintNode {
   private int kind;
   private OperationConstraintGroup operationConstraintGroup = null;

   public BasicConstraintNode() {
   }

   public int getKind() {
      return this.kind;
   }

   public void setKind(int k) {
      this.kind = k;
      if (k == 0 || k == 3) {
         this.operationConstraintGroup = null;
      }

   }

   public OperationConstraintGroup getGroup() {
      return this.operationConstraintGroup;
   }

   public void setGroup(OperationConstraintGroup cg) {
      this.operationConstraintGroup = cg;
      if (cg != null && this.kind != 1 && this.kind != 2) {
         throw new RuntimeException("Non-null groups allowed only for operation constraints");
      }
   }

   public String toString() {
      StringBuffer bf = new StringBuffer(this.constraintName != null ? this.constraintName : "<unnamed constraint>");
      switch(this.kind) {
      case 0:
         bf.append("[invariant]");
         break;
      case 1:
         bf.append("[precondition]");
         break;
      case 2:
         bf.append("[postcondition]");
      }

      if (this.kind == 2 || this.kind == 1) {
         bf.append('(');
         Variable[] p = this.operationConstraintGroup.getFormalParameters();
         int i = 0;

         while(i < p.length) {
            bf.append(p[i].getName()).append(':').append(p[i].getType().getFullName());
            ++i;
            if (i < p.length) {
               bf.append(',');
            }
         }

         bf.append(')');
      }

      return bf.toString();
   }
}
