package ro.ubbcluj.lci.ocl.codegen.norm;

import ro.ubbcluj.lci.uml.foundation.core.Classifier;

public class ConstrainedClass extends ConstrainedObject {
   private Classifier constrainedClass = null;

   public ConstrainedClass(Classifier cc) {
      this.constrainedClass = cc;
   }

   public Classifier getConstrainedClass() {
      return this.constrainedClass;
   }
}
