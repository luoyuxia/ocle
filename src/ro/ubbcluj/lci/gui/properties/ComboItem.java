package ro.ubbcluj.lci.gui.properties;

public class ComboItem {
   protected Object theValue;
   protected String theText;

   public ComboItem(Object value, String text) {
      this.theText = text;
      this.theValue = value;
   }

   public Object getValue() {
      return this.theValue;
   }

   public String toString() {
      return this.theText;
   }
}
