package ro.ubbcluj.lci.gui.properties;

public class SimpleProperty implements Property {
   protected String theName;
   protected Object theValue;
   protected GCellEditor theEditor;

   public SimpleProperty() {
      this("Unknown");
   }

   public SimpleProperty(String name) {
      this(name, "Undefined");
   }

   public SimpleProperty(String name, Object value) {
      this(name, value, (GCellEditor)null);
   }

   public SimpleProperty(String name, Object value, GCellEditor editor) {
      this.theName = name;
      this.theValue = value;
      this.theEditor = editor;
   }

   public String getName() {
      return this.theName;
   }

   public void setName(String name) {
      this.theName = name;
   }

   public Object getValue() {
      return this.theValue;
   }

   public void setValue(Object value) {
      if (value != null) {
         this.theValue = value;
      } else {
         this.theValue = "Undefined";
      }

   }

   public GCellEditor getEditor() {
      return this.theEditor;
   }

   public void addProperty(Property childProperty) {
   }

   public int getPropertiesCount() {
      return 0;
   }

   public Property getPropertyAt(int index) {
      return null;
   }

   public void removeProperty(Property childProperty) {
   }
}
