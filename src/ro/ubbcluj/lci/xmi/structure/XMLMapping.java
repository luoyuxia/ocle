package ro.ubbcluj.lci.xmi.structure;

import java.util.Hashtable;

public class XMLMapping {
   private Hashtable mappedClassesQXNK = new Hashtable();
   private Hashtable mappedClassesCDK = new Hashtable();
   private Hashtable mappedClassesCK = new Hashtable();
   private Hashtable constants = new Hashtable();

   public XMLMapping() {
   }

   public XMLClassMapping getClass(String qname) {
      return (XMLClassMapping)this.mappedClassesQXNK.get(qname);
   }

   public XMLClassMapping getClass(ClassDescriptor cd) {
      return (XMLClassMapping)this.mappedClassesCDK.get(cd);
   }

   public XMLClassMapping getClass(Class c) {
      return (XMLClassMapping)this.mappedClassesCK.get(c);
   }

   public boolean containsClass(String qname) {
      return this.mappedClassesQXNK.containsKey(qname);
   }

   public boolean containsClass(ClassDescriptor cd) {
      return this.mappedClassesCDK.containsKey(cd);
   }

   public boolean containsClass(Class c) {
      return this.mappedClassesCK.containsKey(c);
   }

   public void putClass(XMLClassMapping xcm, boolean b) {
      String qname = xcm.getXmlNamespace().length() > 0 ? xcm.getXmlNamespace() + ":" + xcm.getXmlName() : xcm.getXmlName();
      this.mappedClassesQXNK.put(qname, xcm);
      this.mappedClassesCDK.put(xcm.getMappedClass(), xcm);
      this.mappedClassesCK.put(b ? xcm.getMappedClass().getDescriptedClass() : xcm.getMappedClass().getObjectInstantiateClass(), xcm);
   }

   public XMLClassMapping remove(ClassDescriptor cd, boolean b) {
      XMLClassMapping xcm = (XMLClassMapping)this.mappedClassesCDK.remove(cd);
      this.mappedClassesQXNK.remove(xcm.getXmlNamespace().length() > 0 ? xcm.getXmlNamespace() + ":" + xcm.getXmlName() : xcm.getXmlName());
      this.mappedClassesCK.remove(b ? xcm.getMappedClass().getDescriptedClass() : xcm.getMappedClass().getObjectInstantiateClass());
      return xcm;
   }

   public String getConstant(String qname) {
      return (String)this.constants.get(qname);
   }

   public void putConstant(String qname, String value) {
      this.constants.put(qname, value);
   }
}
