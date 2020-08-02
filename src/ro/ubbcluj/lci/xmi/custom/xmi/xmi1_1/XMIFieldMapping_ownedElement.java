package ro.ubbcluj.lci.xmi.custom.xmi.xmi1_1;

import java.lang.reflect.InvocationTargetException;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;
import ro.ubbcluj.lci.xmi.behavior.StatementObserver;
import ro.ubbcluj.lci.xmi.behavior.TemporaryObjectReplacer;
import ro.ubbcluj.lci.xmi.structure.FieldDescriptor;
import ro.ubbcluj.lci.xmi.structure.XMLFieldMapping;

public class XMIFieldMapping_ownedElement extends XMLFieldMapping {
   public XMIFieldMapping_ownedElement() {
   }

   public XMIFieldMapping_ownedElement(FieldDescriptor fd) {
      super(fd);
   }

   public Object getMappedFieldValue(Object ownerObject) throws IllegalAccessException, InvocationTargetException {
      return ((Namespace)ownerObject).directGetOwnedElementList();
   }

   public void setMappedFieldValue(Object ownerObject, Object[] newValue) throws IllegalAccessException, InvocationTargetException {
      Object[] params = new Object[1];

      for(int i = 0; i < newValue.length; ++i) {
         if (newValue[i] != null) {
            if (newValue[i] instanceof TemporaryObjectReplacer) {
               TemporaryObjectReplacer tor = (TemporaryObjectReplacer)newValue[i];
               tor.addObserver(new StatementObserver(this, ownerObject));
            } else {
               params[0] = ((ModelElement)newValue[i]).getNamespace();
               this.mappedField.setFieldValue(ownerObject, params);
            }
         }
      }

   }
}
