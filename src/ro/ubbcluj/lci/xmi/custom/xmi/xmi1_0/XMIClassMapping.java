package ro.ubbcluj.lci.xmi.custom.xmi.xmi1_0;

import ro.ubbcluj.lci.xmi.custom.xmi.XMIUtilities;
import ro.ubbcluj.lci.xmi.structure.ClassDescriptor;
import ro.ubbcluj.lci.xmi.structure.XMLClassMapping;

public class XMIClassMapping extends XMLClassMapping {
   private static final String XML_NAMESPACE = "";

   public XMIClassMapping() {
      this.xmlNamespace = "";
   }

   public XMIClassMapping(ClassDescriptor cd) {
      super(cd);
      this.xmlNamespace = "";
      this.xmlName = XMIUtilities.renamePackages(this.xmlName);
   }

   public void setMappedClass(ClassDescriptor cd) {
      this.mappedClass = cd;
      this.xmlNamespace = "";
      this.xmlName = cd.getDescriptedClass().getName();
      this.xmlName = XMIUtilities.renamePackages(this.xmlName);
   }
}
