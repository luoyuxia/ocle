package ro.ubbcluj.lci.xmi.custom.xmi.xmi1_1;

import ro.ubbcluj.lci.xmi.structure.FieldDescriptor;
import ro.ubbcluj.lci.xmi.structure.XMLFieldMapping;

public class XMIFieldMapping extends XMLFieldMapping {
   private static final String XML_NAMESPACE = "UML";

   public XMIFieldMapping() {
      this.xmlNamespace = "UML";
   }

   public XMIFieldMapping(FieldDescriptor fd) {
      this.setMappedField(fd);
   }

   public void setMappedField(FieldDescriptor fd) {
      this.mappedField = fd;
      String className = this.mappedField.getDeclaringClass().getName();
      this.xmlName = className.substring(className.lastIndexOf(".") + 1);
      this.xmlName = this.xmlName + "." + fd.getFieldName();
      this.xmlNamespace = "UML";
      this.xmlType = 6;
   }
}
