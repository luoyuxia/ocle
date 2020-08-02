package ro.ubbcluj.lci.uml.tupleTypes;

import java.util.Set;
import ro.ubbcluj.lci.uml.modelManagement.Package;

public class TupleType1 {
   public Package a;
   public Set b;

   public TupleType1() {
   }

   public boolean equals(Object arg) {
      if (!(arg instanceof TupleType1)) {
         return false;
      } else {
         TupleType1 local = (TupleType1)arg;
         boolean result = true;
         result &= this.a != null ? this.a.equals(local.a) : local.a == null;
         result &= this.b != null ? this.b.equals(local.b) : local.b == null;
         return result;
      }
   }
}
