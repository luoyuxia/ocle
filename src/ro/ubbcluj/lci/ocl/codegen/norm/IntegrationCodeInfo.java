package ro.ubbcluj.lci.ocl.codegen.norm;

import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.utils.uml.UMLUtilities;

public class IntegrationCodeInfo {
   private String innerClassName;
   private ConstrainedClass baseClass;
   private String implementationMethodName;

   public IntegrationCodeInfo() {
   }

   public void setInnerClassName(String innerClassName) {
      this.innerClassName = innerClassName;
   }

   public String getInnerClassName() {
      return this.innerClassName;
   }

   public void setBaseClass(ConstrainedClass baseClass) {
      this.baseClass = baseClass;
   }

   public ConstrainedClass getBaseClass() {
      return this.baseClass;
   }

   public void setImplementationMethodName(String implementationMethodName) {
      this.implementationMethodName = implementationMethodName;
   }

   public String getImplementationMethodName() {
      return this.implementationMethodName;
   }

   public String toString() {
      StringBuffer bf = new StringBuffer();
      if (this.innerClassName != null) {
         bf.append("ConstraintCheckerClass=").append(this.innerClassName);
      }

      if (this.baseClass != null) {
         bf.append(" BaseClass=").append(UMLUtilities.getFullyQualifiedName((ModelElement)this.baseClass.getConstrainedClass()));
      }

      if (this.implementationMethodName != null) {
         bf.append(" Implementation goes to:").append(this.implementationMethodName);
      }

      return bf.toString();
   }
}
