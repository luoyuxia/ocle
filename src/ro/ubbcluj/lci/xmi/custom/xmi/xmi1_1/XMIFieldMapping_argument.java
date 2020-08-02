package ro.ubbcluj.lci.xmi.custom.xmi.xmi1_1;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import ro.ubbcluj.lci.uml.foundation.core.Binding;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.TemplateArgument;
import ro.ubbcluj.lci.uml.foundation.core.TemplateArgumentImpl;
import ro.ubbcluj.lci.xmi.behavior.StatementObserver;
import ro.ubbcluj.lci.xmi.behavior.TemporaryObjectReplacer;
import ro.ubbcluj.lci.xmi.structure.FieldDescriptor;
import ro.ubbcluj.lci.xmi.structure.XMLFieldMapping;

public class XMIFieldMapping_argument extends XMLFieldMapping {
   public XMIFieldMapping_argument() {
   }

   public XMIFieldMapping_argument(FieldDescriptor fd) {
      super(fd);
      fd.setFieldType(String.class);
   }

   public Object getMappedFieldValue(Object ownerObject) throws IllegalAccessException, InvocationTargetException {
      ArrayList array = new ArrayList();
      Enumeration en = ((Binding)ownerObject).getArgumentList();

      while(en.hasMoreElements()) {
         TemplateArgument ta = (TemplateArgument)en.nextElement();
         ModelElement me = ta.getModelElement();
         if (me != null) {
            array.add(me);
         }
      }

      return Collections.enumeration(array);
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
               t.setModelElement((ModelElement)params[0]);
               ((Binding)ownerObject).addArgument(t);
            }
         }
      }

   }
}
