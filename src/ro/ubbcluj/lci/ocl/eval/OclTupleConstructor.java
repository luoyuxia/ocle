package ro.ubbcluj.lci.ocl.eval;

import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.ocl.datatypes.OclTuple;

public class OclTupleConstructor extends OclExpression {
   private int partCount;
   private String[] partNames;
   private OclExpression[] partValues;
   private OclNode nod;

   public OclTupleConstructor(int partCount, String[] partNames, OclExpression[] partValues, OclNode nod) {
      this.partCount = partCount;
      this.partNames = partNames;
      this.partValues = partValues;
      this.nod = nod;
   }

   public Object evaluate() throws ExceptionEvaluate {
      super.evaluate();
      Object[] rez = new Object[this.partCount];

      for(int i = 0; i < this.partCount; ++i) {
         rez[i] = this.partValues[i].evaluate();
      }

      return new OclTuple(this.partCount, this.partNames, rez, this.nod);
   }
}
