package ro.ubbcluj.lci.ocl.batcheval;

import java.util.EventObject;

public class EvaluationEvent extends EventObject {
   private long errors;
   private long total;
   private long performed;

   public EvaluationEvent(Object src, long total, long performed, long err) {
      super(src);
      this.total = total;
      this.performed = performed;
      this.errors = err;
   }

   public long getTotal() {
      return this.total;
   }

   public long getCount() {
      return this.performed;
   }

   public long getErrorCount() {
      return this.errors;
   }
}
