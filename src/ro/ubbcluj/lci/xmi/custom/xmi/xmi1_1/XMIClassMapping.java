package ro.ubbcluj.lci.xmi.custom.xmi.xmi1_1;

import ro.ubbcluj.lci.xmi.structure.ClassDescriptor;
import ro.ubbcluj.lci.xmi.structure.XMLClassMapping;

public class XMIClassMapping extends XMLClassMapping {
   private static final String XML_NAMESPACE = "UML";

   public XMIClassMapping() {
      this.xmlNamespace = "UML";
   }

   public XMIClassMapping(ClassDescriptor cd) {
      super(cd);
      this.xmlNamespace = "UML";
      this.xmlName = this.xmlName.substring(this.xmlName.lastIndexOf(".") + 1);
   }

   public void setMappedClass(ClassDescriptor cd) {
      this.mappedClass = cd;
      this.xmlNamespace = "UML";
      this.xmlName = cd.getDescriptedClass().getName();
      this.xmlName = this.xmlName.substring(this.xmlName.lastIndexOf(".") + 1);
   }
}
