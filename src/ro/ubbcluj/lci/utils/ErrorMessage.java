package ro.ubbcluj.lci.utils;

public class ErrorMessage {
   protected String errorMessage;

   public ErrorMessage() {
      this("Unknown error.");
   }

   public ErrorMessage(String errMsg) {
      this.errorMessage = errMsg;
   }

   public String getErrorMessage() {
      return this.errorMessage;
   }

   public String toString() {
      return this.errorMessage;
   }
}
