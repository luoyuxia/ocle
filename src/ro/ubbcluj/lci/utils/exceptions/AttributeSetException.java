package ro.ubbcluj.lci.utils.exceptions;

public class AttributeSetException extends ModelException {
   protected String attribute;
   protected Object value;

   public AttributeSetException(Object context, String attribute, Object value) {
      super(context);
      this.attribute = attribute;
      this.value = value;
   }

   public String toString() {
      return "Attribute '" + this.attribute + "' could not be set to value '" + this.value + "' for context " + this.context;
   }
}
