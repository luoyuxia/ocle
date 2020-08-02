package ro.ubbcluj.lci.ocl.eval;

import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.ocl.nodes.classifierContext;
import ro.ubbcluj.lci.ocl.nodes.operationContext;

public class OclConstant extends OclExpression {
   private static int sID = 0;
   private int ID;
   public Object value = null;
   private String reason = null;
   private boolean dummy = false;

   public OclConstant(OclNode p_nod, Object p_value) {
      ++sID;
      this.ID = sID;
      this.value = p_value;
      this.nod = p_nod;
   }

   public OclConstant(String reason, OclNode nod) {
      this.nod = nod;
      this.dummy = true;
      this.reason = reason;
   }

   public Object evaluate() throws ExceptionEvaluate {
      super.evaluate();
      if (this.value == null) {
         if (this.dummy) {
            throw new ExceptionEvaluate(this.reason, this.nod);
         } else if (!(this.nod instanceof classifierContext) && !(this.nod instanceof operationContext)) {
            throw new ExceptionEvaluate("constant value uninitialized", this.nod);
         } else {
            throw new ExceptionEvaluate("the context " + this.nod.type.getFullName() + " is not associated with a value yet", this.nod);
         }
      } else {
         return this.value;
      }
   }

   public Object getValue() {
      return this.value;
   }

   public void setValue(Object _value) {
      this.value = _value;
   }

   public String toString() {
      return "OclConstant(" + this.ID + "," + this.value + ")";
   }
}
