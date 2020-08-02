package ro.ubbcluj.lci.errors;

public abstract class LocalizableErrorMessage extends ErrorMessage {
   public LocalizableErrorMessage(Object src) {
      super(src);
   }

   public abstract int getStop();

   public abstract int getStart();
}
