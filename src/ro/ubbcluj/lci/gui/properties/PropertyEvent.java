package ro.ubbcluj.lci.gui.properties;

import java.util.EventObject;

public class PropertyEvent extends EventObject {
   protected Object subject;
   protected Object newValue;
   protected String property;

   public PropertyEvent(Object source, Object subject, String property, Object newValue) {
      super(source);
      this.subject = subject;
      this.property = property;
      this.newValue = newValue;
   }

   public Object getSubject() {
      return this.subject;
   }

   public Object getNewValue() {
      return this.newValue;
   }

   public String getProperty() {
      return this.property;
   }
}
