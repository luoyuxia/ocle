package ro.ubbcluj.lci.xmi.custom.xmi.xmi1_0;

import java.lang.reflect.InvocationTargetException;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.xmi.behavior.StatementObserver;
import ro.ubbcluj.lci.xmi.behavior.TemporaryObjectReplacer;
import ro.ubbcluj.lci.xmi.custom.xmi.XMIUtilities;
import ro.ubbcluj.lci.xmi.structure.FieldDescriptor;

public class XMIFieldMapping_EO_isSpecification extends XMIFieldMapping {
   public XMIFieldMapping_EO_isSpecification() {
   }

   public XMIFieldMapping_EO_isSpecification(FieldDescriptor fd) {
      this.setMappedField(fd);
   }

   public Class getMappedFieldType() {
      return Boolean.TYPE;
   }

   public void setMappedField(FieldDescriptor fd) {
      this.mappedField = fd;
      this.xmlName = fd.getFieldName();
      String className = "ro.ubbcluj.lci.uml.repository.foundation.core.ModelElement";
      this.xmlName = XMIUtilities.renamePackages(className) + "." + this.xmlName;
      this.xmlNamespace = "";
   }

   public Object getMappedFieldValue(Object ownerObject) throws IllegalAccessException, InvocationTargetException {
      return ((ModelElement)ownerObject).getNamespace() == null ? new Boolean(false) : new Boolean(((ModelElement)ownerObject).getNamespace().isSpecification());
   }

   public void setMappedFieldValue(Object ownerObject, Object[] newValue) throws IllegalAccessException, InvocationTargetException {
      for(int i = 0; i < newValue.length; ++i) {
         if (newValue[i] != null) {
            if (newValue[i] instanceof TemporaryObjectReplacer) {
               TemporaryObjectReplacer tor = (TemporaryObjectReplacer)newValue[i];
               tor.addObserver(new StatementObserver(this, ownerObject));
            } else {
               boolean param = ((Boolean)newValue[i]).booleanValue();
               ((ModelElement)ownerObject).getNamespace().setSpecification(param);
            }
         }
      }

   }
}
