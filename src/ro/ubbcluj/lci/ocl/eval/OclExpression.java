package ro.ubbcluj.lci.ocl.eval;

import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.ocl.OclUmlApi;
import ro.ubbcluj.lci.ocl.OclUtil;

public abstract class OclExpression {
   public OclNode nod = null;
   protected OclUmlApi umlapi;

   public OclExpression() {
      this.umlapi = OclUtil.umlapi;
   }

   public OclExpression(OclNode p_node) {
      this.umlapi = OclUtil.umlapi;
      this.nod = p_node;
   }

   public Object evaluate() throws ExceptionEvaluate {
      OclUtil.umlapi = this.umlapi;
      return null;
   }
}
