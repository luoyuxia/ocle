package ro.ubbcluj.lci.xmi.custom.xmi.xmi1_0;

import java.lang.reflect.InvocationTargetException;
import ro.ubbcluj.lci.uml.foundation.core.ElementOwnership;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;
import ro.ubbcluj.lci.xmi.behavior.StatementObserver;
import ro.ubbcluj.lci.xmi.behavior.TemporaryObjectReplacer;
import ro.ubbcluj.lci.xmi.structure.FieldDescriptor;

public class XMIFieldMapping_namespace extends XMIFieldMapping {
   public XMIFieldMapping_namespace() {
   }

   public XMIFieldMapping_namespace(FieldDescriptor fd) {
      super(fd);
   }

   public Object getMappedFieldValue(Object ownerObject) throws IllegalAccessException, InvocationTargetException {
      return ((ModelElement)ownerObject).directGetNamespace();
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
               ownerObject = ((ModelElement)ownerObject).getNamespace();
               ((ElementOwnership)ownerObject).setNamespace((Namespace)params[0]);
            }
         }
      }

   }
}
