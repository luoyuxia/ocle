package ro.ubbcluj.lci.xmi.structure;

import java.lang.reflect.InvocationTargetException;
import ro.ubbcluj.lci.xmi.behavior.StatementObserver;
import ro.ubbcluj.lci.xmi.behavior.TemporaryObjectReplacer;

public class XMLFieldMapping {
   protected FieldDescriptor mappedField;
   protected String xmlName;
   protected String xmlNamespace;
   protected int xmlType = 0;

   public XMLFieldMapping() {
   }

   public XMLFieldMapping(FieldDescriptor fd) {
      this.setMappedField(fd);
   }

   public String getXmlName() {
      return this.xmlName;
   }

   public String getXmlNamespace() {
      return this.xmlNamespace;
   }

   public String getQXmlName() {
      return this.xmlNamespace != null && this.xmlNamespace.length() > 0 ? this.xmlNamespace + ":" + this.xmlName : this.xmlName;
   }

   public int getXmlType() {
      return this.xmlType;
   }

   public Class getMappedFieldType() {
      return this.mappedField.getFieldType();
   }

   public FieldDescriptor getMappedField() {
      return this.mappedField;
   }

   public void setMappedField(FieldDescriptor fd) {
      this.mappedField = fd;
      this.xmlName = fd.getFieldName();
      this.xmlNamespace = "";
   }

   public void setXmlName(String x) {
      this.xmlName = x;
   }

   public void setXmlNamespace(String x) {
      this.xmlNamespace = x;
   }

   public void setXmlType(int x) {
      this.xmlType = x;
   }

   public Object getMappedFieldValue(Object ownerObject) throws IllegalAccessException, InvocationTargetException {
      return this.mappedField.getFieldValue(ownerObject);
   }

   public void setMappedFieldValue(Object ownerObject, Object[] newValue) throws IllegalAccessException, InvocationTargetException {
      Object[] params = new Object[1];

      for(int i = 0; i < newValue.length; ++i) {
         if (newValue[i] != null) {
            if (newValue[i] instanceof TemporaryObjectReplacer) {
               TemporaryObjectReplacer tor = (TemporaryObjectReplacer)newValue[i];
               tor.addObserver(new StatementObserver(this, ownerObject));
            } else {
               params[0] = newValue[i];
               this.mappedField.setFieldValue(ownerObject, params);
            }
         }
      }

   }
}
