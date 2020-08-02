package ro.ubbcluj.lci.ocl.eval;

import ro.ubbcluj.lci.ocl.OclNode;

public class OclAccumulator extends OclExpression {
   public OclExpression init = null;
   private Object value = null;

   public OclAccumulator(OclNode p_nod, OclExpression p_init) {
      this.nod = p_nod;
      this.init = p_init;
   }

   public Object getValue() {
      return this.value;
   }

   public void setValue(Object _value) {
      this.value = _value;
   }

   public Object evaluate() throws ExceptionEvaluate {
      super.evaluate();
      if (this.value == null) {
         this.value = this.init.evaluate();
      }

      return this.value;
   }
}
