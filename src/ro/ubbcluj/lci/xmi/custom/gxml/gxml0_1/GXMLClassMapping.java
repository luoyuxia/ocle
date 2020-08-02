package ro.ubbcluj.lci.xmi.custom.gxml.gxml0_1;

import ro.ubbcluj.lci.xmi.structure.ClassDescriptor;
import ro.ubbcluj.lci.xmi.structure.XMLClassMapping;

public class GXMLClassMapping extends XMLClassMapping {
   private static final String XML_NAMESPACE = "GXML";

   public GXMLClassMapping() {
      this.xmlNamespace = "GXML";
   }

   public GXMLClassMapping(ClassDescriptor cd) {
      super(cd);
      this.xmlNamespace = "GXML";
      this.xmlName = this.xmlName.substring(this.xmlName.lastIndexOf(".") + 3);
      this.xmlName = this.xmlName.replace('$', '_');
   }

   public void setMappedClass(ClassDescriptor cd) {
      this.mappedClass = cd;
      this.xmlNamespace = "GXML";
      this.xmlName = cd.getDescriptedClass().getName();
      this.xmlName = this.xmlName.substring(this.xmlName.lastIndexOf(".") + 3);
      this.xmlName = this.xmlName.replace('$', '_');
   }
}
