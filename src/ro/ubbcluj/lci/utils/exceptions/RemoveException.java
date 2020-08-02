package ro.ubbcluj.lci.utils.exceptions;

public class RemoveException extends ModelException {
   protected Object toDelete;

   public RemoveException(Object context, Object toDelete) {
      super(context);
      this.toDelete = toDelete;
   }

   public String toString() {
      return this.toDelete.toString() + " could not be deleted from context " + this.context;
   }
}
