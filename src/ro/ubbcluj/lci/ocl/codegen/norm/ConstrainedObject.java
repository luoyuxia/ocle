package ro.ubbcluj.lci.ocl.codegen.norm;

import java.util.ArrayList;

public class ConstrainedObject {
   private ArrayList constraints = new ArrayList();
   private IntegrationCodeInfo codeInfo;

   public ConstrainedObject() {
   }

   public void addConstraint(AbstractConstraintNode constraint) {
      this.constraints.add(constraint);
   }

   public int getConstraintCount() {
      return this.constraints.size();
   }

   public AbstractConstraintNode[] getConstraints() {
      int n = this.constraints.size();
      AbstractConstraintNode[] ret = new AbstractConstraintNode[n];

      for(int i = 0; i < n; ++i) {
         ret[i] = (AbstractConstraintNode)this.constraints.get(i);
      }

      return ret;
   }

   public void setCodeInfo(IntegrationCodeInfo ci) {
      this.codeInfo = ci;
   }

   public IntegrationCodeInfo getCodeInfo() {
      return this.codeInfo;
   }
}
