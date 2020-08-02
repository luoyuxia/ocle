package ro.ubbcluj.lci.xmi.behavior;

import java.util.Observable;

public class TemporaryObjectReplacer extends Observable {
   private Object replacedObject;

   public TemporaryObjectReplacer() {
   }

   public void setReplacedObject(XMLMappedObject xmo) {
      this.replacedObject = xmo.getMappedObject();
      this.setChanged();
      this.notifyObservers(this.replacedObject);
   }
}
