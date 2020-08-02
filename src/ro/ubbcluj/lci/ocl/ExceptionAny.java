package ro.ubbcluj.lci.ocl;

public class ExceptionAny extends Exception {
   private String msg;

   public ExceptionAny(String _msg) {
      this.msg = _msg;
   }

   public String getMessage() {
      return this.msg;
   }
}
