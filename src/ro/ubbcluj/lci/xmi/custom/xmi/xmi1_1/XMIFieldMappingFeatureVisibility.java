package ro.ubbcluj.lci.xmi.custom.xmi.xmi1_1;

import java.lang.reflect.InvocationTargetException;
import ro.ubbcluj.lci.uml.foundation.core.Feature;
import ro.ubbcluj.lci.xmi.behavior.StatementObserver;
import ro.ubbcluj.lci.xmi.behavior.TemporaryObjectReplacer;
import ro.ubbcluj.lci.xmi.structure.FieldDescriptor;
import ro.ubbcluj.lci.xmi.structure.XMLFieldMapping;

public class XMIFieldMappingFeatureVisibility extends XMLFieldMapping {
   public XMIFieldMappingFeatureVisibility() {
   }

   public XMIFieldMappingFeatureVisibility(FieldDescriptor fd) {
      super(fd);
   }

   public Class getMappedFieldType() {
      return Integer.TYPE;
   }

   public Object getMappedFieldValue(Object ownerObject) throws IllegalAccessException, InvocationTargetException {
      return new Integer(((Feature)ownerObject).getVisibility());
   }

   public void setMappedFieldValue(Object ownerObject, Object[] newValue) throws IllegalAccessException, InvocationTargetException {
      for(int i = 0; i < newValue.length; ++i) {
         if (newValue[i] != null) {
            if (newValue[i] instanceof TemporaryObjectReplacer) {
               TemporaryObjectReplacer tor = (TemporaryObjectReplacer)newValue[i];
               tor.addObserver(new StatementObserver(this, ownerObject));
            } else {
               int param = ((Integer)newValue[i]).intValue();
               ((Feature)ownerObject).setVisibility(param);
            }
         }
      }

   }
}
