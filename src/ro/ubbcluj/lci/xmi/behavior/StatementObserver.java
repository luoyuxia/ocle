package ro.ubbcluj.lci.xmi.behavior;

import java.util.Observable;
import java.util.Observer;
import ro.ubbcluj.lci.xmi.structure.XMLFieldMapping;

public class StatementObserver implements Observer {
   private XMLFieldMapping field;
   private Object owner;

   public StatementObserver() {
   }

   public StatementObserver(XMLFieldMapping xfm, Object o) {
      this.field = xfm;
      this.owner = o;
   }

   public void update(Observable o, Object arg) {
      Object[] params = new Object[]{arg};

      try {
         this.field.setMappedFieldValue(this.owner, params);
      } catch (Exception var5) {
         System.err.println("Observer error in setting field " + this.field.getMappedField().getFieldName());
         var5.printStackTrace();
      }

   }
}
