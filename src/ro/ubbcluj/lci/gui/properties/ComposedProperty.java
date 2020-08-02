package ro.ubbcluj.lci.gui.properties;

import java.util.ArrayList;

public class ComposedProperty extends SimpleProperty implements Property {
   protected ArrayList childProperties = new ArrayList();

   public ComposedProperty(String name) {
      this.setName(name);
      this.setValue("");
   }

   public void addProperty(Property childProperty) {
      this.childProperties.add(childProperty);
   }

   public void removeProperty(Property childProperty) {
      this.childProperties.remove(childProperty);
   }

   public int getPropertiesCount() {
      return this.childProperties.size();
   }

   public Property getPropertyAt(int index) {
      return (Property)this.childProperties.get(index);
   }
}
