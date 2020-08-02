package ro.ubbcluj.lci.errors;

public class BasicErrorMessage extends ErrorMessage {
   public BasicErrorMessage(String msg) {
      this.description = msg;
   }

   public boolean equals(Object x) {
      return x instanceof BasicErrorMessage && ((BasicErrorMessage)x).description.equalsIgnoreCase(this.description);
   }

   public void accept(ErrorVisitor errV) {
   }
}
