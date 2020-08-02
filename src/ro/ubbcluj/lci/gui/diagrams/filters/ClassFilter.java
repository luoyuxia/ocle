package ro.ubbcluj.lci.gui.diagrams.filters;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import ro.ubbcluj.lci.gui.diagrams.ClassCell;
import ro.ubbcluj.lci.gui.diagrams.Diagram;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.BehavioralFeature;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.Stereotype;

public class ClassFilter implements AbstractFilter, Serializable {
   private HashMap map = new HashMap();

   public ClassFilter() {
      this.map.put("a_pub", new Boolean(true));
      this.map.put("a_pro", new Boolean(true));
      this.map.put("a_pri", new Boolean(true));
      this.map.put("a_pac", new Boolean(true));
      this.map.put("m_pub", new Boolean(true));
      this.map.put("m_pro", new Boolean(true));
      this.map.put("m_pri", new Boolean(true));
      this.map.put("m_pac", new Boolean(true));
      this.map.put("stereotype", new Boolean(true));
      this.map.put("msignature", new Boolean(true));
   }

   public HashMap getMap() {
      return this.map;
   }

   public void setMap(HashMap hm) {
      this.map = hm;
   }

   public void markProperty(String propName, Boolean value) {
      this.map.put(propName, value);
   }

   public boolean getPropertyValue(String propName) {
      Object o = this.map.get(propName);
      if (o == null) {
         System.err.println("Class Filter:Wrong property, can't get");
         return false;
      } else {
         return ((Boolean)o).booleanValue();
      }
   }

   public boolean showMethodSignature() {
      return this.getPropertyValue("msignature");
   }

   public boolean accepts(Object o) {
      if (o instanceof Stereotype) {
         return ((Boolean)this.map.get("stereotype")).booleanValue();
      } else {
         if (o instanceof Attribute) {
            Attribute attr = (Attribute)o;
            switch(attr.getVisibility()) {
            case 0:
               return ((Boolean)this.map.get("a_pri")).booleanValue();
            case 1:
               return ((Boolean)this.map.get("a_pac")).booleanValue();
            case 2:
               return ((Boolean)this.map.get("a_pro")).booleanValue();
            case 3:
               return ((Boolean)this.map.get("a_pub")).booleanValue();
            }
         }

         if (o instanceof BehavioralFeature) {
            BehavioralFeature feat = (BehavioralFeature)o;
            switch(feat.getVisibility()) {
            case 0:
               return ((Boolean)this.map.get("m_pri")).booleanValue();
            case 1:
               return ((Boolean)this.map.get("m_pac")).booleanValue();
            case 2:
               return ((Boolean)this.map.get("m_pro")).booleanValue();
            case 3:
               return ((Boolean)this.map.get("m_pub")).booleanValue();
            }
         }

         return false;
      }
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
   }

   public static void showDialog(Diagram diag, ClassCell cell) {
      if (cell.getUserObject() instanceof Classifier) {
         ClassFilterDialog.show(diag, cell);
      }
   }
}
