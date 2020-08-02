package ro.ubbcluj.lci.xmi.custom.xmi.xmi1_1;

import java.lang.reflect.InvocationTargetException;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TagDefinition;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TagDefinitionImpl;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TaggedValue;
import ro.ubbcluj.lci.xmi.behavior.StatementObserver;
import ro.ubbcluj.lci.xmi.behavior.TemporaryObjectReplacer;
import ro.ubbcluj.lci.xmi.structure.FieldDescriptor;
import ro.ubbcluj.lci.xmi.structure.XMLFieldMapping;

public class XMIFieldMapping_type extends XMLFieldMapping {
   public XMIFieldMapping_type() {
   }

   public XMIFieldMapping_type(FieldDescriptor fd) {
      super(fd);
      fd.setFieldType(String.class);
   }

   public void setMappedField(FieldDescriptor fd) {
      super.setMappedField(fd);
      fd.setFieldType(String.class);
   }

   public Object getMappedFieldValue(Object ownerObject) throws IllegalAccessException, InvocationTargetException {
      TagDefinition t = ((TaggedValue)ownerObject).getType();
      return t != null ? t.getTagType() : null;
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
               TagDefinition t = new TagDefinitionImpl();
               t.setTagType((String)params[0]);
               ((TaggedValue)ownerObject).setType(t);
            }
         }
      }

   }
}
