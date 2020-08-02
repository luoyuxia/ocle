package ro.ubbcluj.lci.xmi.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import ro.ubbcluj.lci.xmi.behavior.XMLMappedObject;

public class XMLClassMapping {
   protected ClassDescriptor mappedClass;
   protected Hashtable mappedFields;
   protected List orderedMappedFields;
   protected String xmlName;
   protected String xmlNamespace;
   protected int xmlType;

   public XMLClassMapping() {
      this.mappedFields = new Hashtable();
      this.orderedMappedFields = new ArrayList();
   }

   public XMLClassMapping(ClassDescriptor cd) {
      this.mappedClass = cd;
      this.mappedFields = new Hashtable();
      this.orderedMappedFields = new ArrayList();
      this.xmlNamespace = "";
      this.xmlName = cd.getDescriptedClass().getName();
   }

   public void clone(XMLClassMapping xcm) {
      this.mappedClass = xcm.mappedClass;
      this.xmlName = xcm.xmlName;
      this.xmlNamespace = xcm.xmlNamespace;
      this.mappedFields = xcm.mappedFields;
      this.orderedMappedFields = xcm.orderedMappedFields;
   }

   public String getXmlName() {
      return this.xmlName;
   }

   public String getXmlNamespace() {
      return this.xmlNamespace;
   }

   public String getQXmlName() {
      return this.xmlNamespace != null && this.xmlNamespace.length() > 0 ? this.xmlNamespace + ":" + this.xmlName : this.xmlName;
   }

   public ClassDescriptor getMappedClass() {
      return this.mappedClass;
   }

   public void setMappedClass(ClassDescriptor cd) {
      this.mappedClass = cd;
      this.xmlNamespace = "";
      this.xmlName = cd.getDescriptedClass().getName();
   }

   public void setXmlType(int xmlType) {
      this.xmlType = xmlType;
   }

   public int getXmlType() {
      return this.xmlType;
   }

   public void addField(XMLFieldMapping xfm) {
      this.orderedMappedFields.add(xfm);
      this.mappedFields.put(xfm.getXmlNamespace().length() > 0 ? xfm.getXmlNamespace() + ":" + xfm.getXmlName() : xfm.getXmlName(), xfm);
   }

   public void addField(int index, XMLFieldMapping xfm) {
      if (index > -1 && index < this.orderedMappedFields.size()) {
         this.orderedMappedFields.add(index, xfm);
         this.mappedFields.put(xfm.getXmlNamespace().length() > 0 ? xfm.getXmlNamespace() + ":" + xfm.getXmlName() : xfm.getXmlName(), xfm);
      } else {
         this.addField(xfm);
      }

   }

   public Enumeration getFieldList() {
      return Collections.enumeration(this.orderedMappedFields);
   }

   public XMLFieldMapping get(String qname) {
      return (XMLFieldMapping)this.mappedFields.get(qname);
   }

   public boolean contains(String qname) {
      return this.mappedFields.containsKey(qname);
   }

   public XMLFieldMapping remove(String qname, int[] index) {
      XMLFieldMapping xfm = (XMLFieldMapping)this.mappedFields.remove(qname);
      index[0] = this.orderedMappedFields.indexOf(xfm);
      this.orderedMappedFields.remove(xfm);
      return xfm;
   }

   public XMLFieldMapping getFieldFromXMLLink(String qname) {
      int index;
      if ((index = qname.indexOf(":")) != -1) {
         qname = qname.substring(index + 1);
      }

      int dots = 0;
      int i1 = 0;

      int i;
      for(i = qname.length() - 1; i > -1; --i) {
         if (qname.charAt(i) == '.') {
            ++dots;
            if (dots == 1) {
               i1 = i;
            } else if (dots == 2) {
               break;
            }
         }
      }

      String nqname = qname.substring(i + 1);
      Object o = this.mappedFields.get(nqname);
      if (o == null) {
         o = this.mappedFields.get(qname.substring(i1 + 1));
      }

      return (XMLFieldMapping)o;
   }

   public XMLMappedObject createMappedObject() throws InstantiationException, IllegalAccessException {
      if (this.mappedClass != null) {
         Object o = this.mappedClass.newInstance();
         XMLMappedObject xmo = new XMLMappedObject(this, o);
         return xmo;
      } else {
         return null;
      }
   }
}
