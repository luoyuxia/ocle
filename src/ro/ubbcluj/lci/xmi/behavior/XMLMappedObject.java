package ro.ubbcluj.lci.xmi.behavior;

import ro.ubbcluj.lci.xmi.structure.XMLClassMapping;

public class XMLMappedObject {
   private XMLClassMapping mappedClass;
   private Object mappedObject;

   public XMLMappedObject() {
   }

   public XMLMappedObject(XMLClassMapping xcm) {
      this.mappedClass = xcm;
   }

   public XMLMappedObject(XMLClassMapping xcm, Object o) {
      this.mappedClass = xcm;
      this.mappedObject = o;
   }

   public void setMappedClass(XMLClassMapping mappedClass) {
      this.mappedClass = mappedClass;
   }

   public XMLClassMapping getMappedClass() {
      return this.mappedClass;
   }

   public void setMappedObject(Object mappedObject) {
      this.mappedObject = mappedObject;
   }

   public Object getMappedObject() {
      return this.mappedObject;
   }
}
