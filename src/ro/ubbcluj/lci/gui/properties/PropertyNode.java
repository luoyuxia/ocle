package ro.ubbcluj.lci.gui.properties;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

public class PropertyNode {
   protected Object theElement;
   protected Property theProperty;
   protected Object[] children;
   protected boolean readOnly;

   public PropertyNode(Object element) {
      this.theElement = element;
      IProperties iElement;
      if (element instanceof IProperties) {
         iElement = (IProperties)element;
         this.theProperty = new ComposedProperty(element.getClass().getName());
         Iterator it = ((IProperties)element).getProperties().iterator();

         while(it.hasNext()) {
            this.theProperty.addProperty((Property)it.next());
         }

         this.setChildren((ComposedProperty)this.theProperty);
      } else {
         iElement = null;

         try {
            Class clazz = this.theElement.getClass();
            String name = clazz.getName();
            Class pClazz = Class.forName(name + "Properties");
            ElementProperties pElement = (ElementProperties)pClazz.newInstance();
            Method m = pClazz.getMethod("getProperties", new Class[0]);
            Collection props = (Collection)m.invoke(pElement, new Object[0]);
            this.theProperty = new ComposedProperty(name);
            Iterator it = props.iterator();

            while(it.hasNext()) {
               this.theProperty.addProperty((Property)it.next());
            }

            this.setChildren((ComposedProperty)this.theProperty);
         } catch (Exception var9) {
            new SimpleElementProperties();
            this.theProperty = new SimpleProperty();
         }
      }

   }

   protected PropertyNode(Property prop) {
      this.theProperty = prop;
      if (this.isEditable()) {
         this.theProperty.getEditor().setTarget(this);
      }

      if (prop instanceof ComposedProperty) {
         this.setChildren((ComposedProperty)prop);
      }

   }

   private void setChildren(ComposedProperty prop) {
      int n = prop.getPropertiesCount();
      this.children = new Object[n];

      for(int i = 0; i < n; ++i) {
         this.children[i] = new PropertyNode(prop.getPropertyAt(i));
      }

   }

   public Property getProperty() {
      return this.theProperty;
   }

   public void setProperty(SimpleProperty prop) {
      this.theProperty = prop;
   }

   public boolean isEditable() {
      if (this.theProperty.getEditor() != null) {
         return this.theProperty.getEditor().getComponent() instanceof JumpButton ? true : true;
      } else {
         return false;
      }
   }

   public boolean isReadOnly() {
      return this.readOnly;
   }

   public String toString() {
      return this.theProperty.getName();
   }

   public String getValueString() {
      Object value = this.theProperty.getValue();
      if (value == null && this.theProperty.getEditor() == null) {
         return "N/A";
      } else if (this.theProperty instanceof ComposedProperty && this.theProperty.getEditor() == null) {
         return "";
      } else {
         return value == null ? "" : value.toString();
      }
   }

   public Object getValue() {
      return this.theProperty.getValue();
   }

   public Object[] getChildren() {
      return this.children != null ? this.children : new Object[0];
   }

   public int getChildCount() {
      return this.children == null ? 0 : this.children.length;
   }

   public boolean isAllowingChildren() {
      return this.theProperty instanceof ComposedProperty;
   }

   public boolean isLeaf() {
      return this.getChildCount() == 0;
   }
}
