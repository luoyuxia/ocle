package ro.ubbcluj.lci.ocl.codegen.norm;

import ro.ubbcluj.lci.uml.foundation.core.Operation;

public class ConstrainedOperation extends ConstrainedObject {
   private Operation constrainedOperation = null;

   public ConstrainedOperation(Operation co) {
      this.constrainedOperation = co;
   }

   public Operation getConstrainedOperation() {
      return this.constrainedOperation;
   }
}
