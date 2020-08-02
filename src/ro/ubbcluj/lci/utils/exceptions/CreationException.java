package ro.ubbcluj.lci.utils.exceptions;

public class CreationException extends ModelException {
   protected String typeToCreate;

   public CreationException(Object context, String typeToCreate) {
      super(context);
      this.typeToCreate = typeToCreate;
   }

   public String toString() {
      return "Could not create new " + this.typeToCreate + " in context " + this.context;
   }
}
