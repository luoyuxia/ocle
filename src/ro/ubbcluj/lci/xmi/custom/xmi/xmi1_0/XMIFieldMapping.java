package ro.ubbcluj.lci.xmi.custom.xmi.xmi1_0;

import ro.ubbcluj.lci.xmi.custom.xmi.XMIUtilities;
import ro.ubbcluj.lci.xmi.structure.FieldDescriptor;
import ro.ubbcluj.lci.xmi.structure.XMLFieldMapping;

public class XMIFieldMapping extends XMLFieldMapping {
   public XMIFieldMapping() {
   }

   public XMIFieldMapping(FieldDescriptor fd) {
      this.setMappedField(fd);
   }

   public void setMappedField(FieldDescriptor fd) {
      this.mappedField = fd;
      this.xmlName = fd.getFieldName();
      String className = this.mappedField.getDeclaringClass().getName();
      this.xmlName = XMIUtilities.renamePackages(className) + "." + this.xmlName;
      this.xmlNamespace = "";
   }
}
