package ro.ubbcluj.lci.ocl.datatypes;

import java.lang.reflect.Method;
import java.util.Iterator;
import ro.ubbcluj.lci.ocl.OclClassInfo;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Enumeration;
import ro.ubbcluj.lci.uml.foundation.core.EnumerationLiteral;

public class OclEnumLiteral extends OclAny {
   private OclClassInfo enumeration = null;
   private String stringValue = null;
   private EnumerationLiteral literal = null;

   public OclEnumLiteral(OclClassInfo enumeration, String stringValue) {
      this.enumeration = enumeration;
      this.stringValue = stringValue;
   }

   public OclEnumLiteral(OclClassInfo enumeration, EnumerationLiteral literal) {
      this.enumeration = enumeration;
      this.literal = literal;
   }

   public OclEnumLiteral(OclClassInfo enumeration, int intValue) {
      this.enumeration = enumeration;
      this.literal = this.convertIntToEnumerationLiteral(enumeration.getClassifier(), intValue);
   }

   public String printValue() {
      if (this.stringValue != null) {
         return this.stringValue;
      } else {
         return this.literal != null ? this.literal.getName() : " (invalid literal)";
      }
   }

   public boolean equals(Object obj) {
      return this.equal(obj);
   }

   public boolean equal(Object obj) {
      if (!(obj instanceof OclEnumLiteral)) {
         return false;
      } else {
         OclEnumLiteral enum = (OclEnumLiteral)obj;
         if (this.enumeration != enum.enumeration) {
            return false;
         } else {
            return this.stringValue != null && this.stringValue.equals(enum.stringValue);
         }
      }
   }

   public boolean notequal(Object obj) {
      return !this.equal(obj);
   }

   public OclClassInfo getEnumeration() {
      return this.enumeration;
   }

   public EnumerationLiteral getLiteral() {
      return this.literal;
   }

   public String getName() {
      return this.stringValue;
   }

   public String toString() {
      return this.printValue();
   }

   private EnumerationLiteral convertIntToEnumerationLiteral(Classifier classifier, int intValue) {
      String value = null;

      try {
         Class enumClass = Class.forName("ro.ubbcluj.lci.uml.foundation.dataTypes." + classifier.getName());
         Method method = enumClass.getMethod("getStringValue", new Class[]{Integer.TYPE});
         value = (String)method.invoke((Object)null, new Object[]{new Integer(intValue)});
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      if (classifier instanceof Enumeration && value != null) {
         Iterator it = ((Enumeration)classifier).getCollectionLiteralList().iterator();

         while(it.hasNext()) {
            EnumerationLiteral el = (EnumerationLiteral)it.next();
            if (el.getName().equals(value)) {
               return el;
            }
         }
      }

      return null;
   }
}
