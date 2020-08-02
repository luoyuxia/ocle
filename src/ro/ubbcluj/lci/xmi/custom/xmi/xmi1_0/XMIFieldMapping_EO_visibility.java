package ro.ubbcluj.lci.xmi.custom.xmi.xmi1_0;

import java.lang.reflect.InvocationTargetException;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Feature;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.xmi.behavior.StatementObserver;
import ro.ubbcluj.lci.xmi.behavior.TemporaryObjectReplacer;
import ro.ubbcluj.lci.xmi.custom.xmi.XMIUtilities;
import ro.ubbcluj.lci.xmi.structure.FieldDescriptor;

public class XMIFieldMapping_EO_visibility extends XMIFieldMapping {
   public XMIFieldMapping_EO_visibility() {
   }

   public XMIFieldMapping_EO_visibility(FieldDescriptor fd) {
      this.setMappedField(fd);
   }

   public Class getMappedFieldType() {
      return Integer.TYPE;
   }

   public void setMappedField(FieldDescriptor fd) {
      this.mappedField = fd;
      this.xmlName = fd.getFieldName();
      String className = "ro.ubbcluj.lci.uml.repository.foundation.core.ModelElement";
      this.xmlName = XMIUtilities.renamePackages(className) + "." + this.xmlName;
      this.xmlNamespace = "";
   }

   public Object getMappedFieldValue(Object ownerObject) throws IllegalAccessException, InvocationTargetException {
      if (((ModelElement)ownerObject).getNamespace() == null) {
         return null;
      } else {
         int visibility = ((ModelElement)ownerObject).getNamespace().getVisibility();
         switch(visibility) {
         case 0:
            return "private";
         case 1:
            return "package";
         case 2:
            return "protected";
         default:
            return "public";
         }
      }
   }

   public void setMappedFieldValue(Object ownerObject, Object[] newValue) throws IllegalAccessException, InvocationTargetException {
      for(int i = 0; i < newValue.length; ++i) {
         if (newValue[i] != null) {
            if (newValue[i] instanceof TemporaryObjectReplacer) {
               TemporaryObjectReplacer tor = (TemporaryObjectReplacer)newValue[i];
               tor.addObserver(new StatementObserver(this, ownerObject));
            } else {
               int param = ((Integer)newValue[i]).intValue();
               ((ModelElement)ownerObject).getNamespace().setVisibility(param);
               if (ownerObject instanceof Feature) {
                  ((Feature)ownerObject).setVisibility(param);
               }

               if (ownerObject instanceof AssociationEnd) {
                  ((AssociationEnd)ownerObject).setVisibility(param);
               }
            }
         }
      }

   }
}
