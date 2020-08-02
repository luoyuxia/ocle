package ro.ubbcluj.lci.ocl.eval;

import ro.ubbcluj.lci.ocl.OclNode;

public class ExceptionEvaluate extends Exception {
   private String msg;
   private OclNode node;

   public ExceptionEvaluate(String _msg, OclNode _node) {
      this.msg = _msg;
      this.node = _node;
   }

   public String getMessage() {
      return this.msg;
   }

   public OclNode getEvalNode() {
      return this.node;
   }
}
