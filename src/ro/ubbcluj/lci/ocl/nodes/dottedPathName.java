package ro.ubbcluj.lci.ocl.nodes;

import ro.ubbcluj.lci.ocl.ExceptionChecker;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.ocl.OclVisitor;

public class dottedPathName extends OclNode {
   public dottedPathName() {
   }

   public void acceptVisitor(OclVisitor visitor) throws ExceptionChecker {
      if (visitor.visitPre(this)) {
         this.visitChildren(visitor);
         visitor.visitPost(this);
      }

   }
}
