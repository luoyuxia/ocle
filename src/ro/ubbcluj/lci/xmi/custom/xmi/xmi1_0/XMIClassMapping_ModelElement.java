package ro.ubbcluj.lci.xmi.custom.xmi.xmi1_0;

import ro.ubbcluj.lci.uml.foundation.core.ElementOwnershipImpl;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.xmi.behavior.XMLMappedObject;
import ro.ubbcluj.lci.xmi.structure.ClassDescriptor;

public class XMIClassMapping_ModelElement extends XMIClassMapping {
   public XMIClassMapping_ModelElement() {
   }

   public XMIClassMapping_ModelElement(ClassDescriptor cd) {
      super(cd);
   }

   public XMLMappedObject createMappedObject() throws InstantiationException, IllegalAccessException {
      if (this.mappedClass != null) {
         Object o = this.mappedClass.newInstance();
         ((ModelElement)o).setNamespace(new ElementOwnershipImpl());
         XMLMappedObject xmo = new XMLMappedObject(this, o);
         return xmo;
      } else {
         return null;
      }
   }
}
