package ro.ubbcluj.lci.gui.properties;

public interface Property {
   String getName();

   void setName(String var1);

   Object getValue();

   void setValue(Object var1);

   GCellEditor getEditor();

   void addProperty(Property var1);

   int getPropertiesCount();

   Property getPropertyAt(int var1);

   void removeProperty(Property var1);
}
