package ro.ubbcluj.lci.ocl.codegen.norm;

import java.util.ArrayList;

public class OperationConstraintGroup {
   private ArrayList formalParameters = new ArrayList();

   public OperationConstraintGroup() {
   }

   public void addParameter(Variable p) {
      this.formalParameters.add(p);
   }

   public Variable[] getFormalParameters() {
      int n = this.formalParameters.size();
      Variable[] ret = new Variable[n];

      for(int i = 0; i < n; ++i) {
         ret[i] = (Variable)this.formalParameters.get(i);
      }

      return ret;
   }
}
