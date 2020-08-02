package ro.ubbcluj.lci.utils.exceptions;

public abstract class ModelException extends Exception {
   protected Object context;

   public ModelException(Object context) {
      this.context = context;
   }

   public Object getContext() {
      return this.context;
   }
}
