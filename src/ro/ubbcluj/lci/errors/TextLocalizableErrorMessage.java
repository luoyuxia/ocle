package ro.ubbcluj.lci.errors;

public class TextLocalizableErrorMessage extends LocalizableErrorMessage {
   protected int start = -1;
   protected int stop = -2;

   public TextLocalizableErrorMessage(Object src) {
      super(src);
   }

   public int getStop() {
      return this.stop;
   }

   public int getStart() {
      return this.start;
   }

   public void accept(ErrorVisitor v) {
      v.visitTextLocalizableErrorMessage(this);
   }
}
