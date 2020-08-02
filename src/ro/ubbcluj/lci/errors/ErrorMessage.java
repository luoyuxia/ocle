package ro.ubbcluj.lci.errors;

public abstract class ErrorMessage {
   private Object source;
   protected String description = "";

   public ErrorMessage() {
   }

   public ErrorMessage(Object src) {
      this.source = src;
   }

   public final Object getSource() {
      return this.source;
   }

   public abstract void accept(ErrorVisitor var1);

   public final String getDescription() {
      return this.description;
   }

   public String toString() {
      return this.getDescription();
   }
}
