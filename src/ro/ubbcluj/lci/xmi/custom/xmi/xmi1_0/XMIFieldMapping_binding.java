package ro.ubbcluj.lci.xmi.custom.xmi.xmi1_0;

import java.lang.reflect.InvocationTargetException;
import ro.ubbcluj.lci.uml.foundation.core.Binding;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.TemplateArgument;
import ro.ubbcluj.lci.uml.foundation.core.TemplateArgumentImpl;
import ro.ubbcluj.lci.xmi.behavior.StatementObserver;
import ro.ubbcluj.lci.xmi.behavior.TemporaryObjectReplacer;
import ro.ubbcluj.lci.xmi.structure.FieldDescriptor;

public class XMIFieldMapping_binding extends XMIFieldMapping {
   public XMIFieldMapping_binding() {
   }

   public XMIFieldMapping_binding(FieldDescriptor fd) {
      super(fd);
      fd.setFieldType(String.class);
   }

   public Object getMappedFieldValue(Object ownerObject) throws IllegalAccessException, InvocationTargetException {
      return null;
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
               TemplateArgument t = new TemplateArgumentImpl();
               t.setModelElement((ModelElement)ownerObject);
               t.setBinding((Binding)params[0]);
            }
         }
      }

   }
}
