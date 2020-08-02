package ro.ubbcluj.lci.xmi.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import ro.ubbcluj.lci.xmi.structure.ClassDescriptor;
import ro.ubbcluj.lci.xmi.structure.FieldDescriptor;
import ro.ubbcluj.lci.xmi.structure.XMIConstants;
import ro.ubbcluj.lci.xmi.structure.XMLClassMapping;
import ro.ubbcluj.lci.xmi.structure.XMLFieldMapping;
import ro.ubbcluj.lci.xmi.structure.XMLMapping;

public class XMIConfigurator extends DefaultHandler {
   private Map rootVersionsMap;
   private boolean defaultState;
   private XMLMapping currentMapping;
   private Class defaultXCM;
   private List defaultXFM;
   private List classes;
   private String extension;
   private boolean di = true;
   private List problemList;
   private ClassIntrospector classIntrospector = new AccessorsClassIntrospector();
   private SAXParser saxNonValidatingParser;
   private Locator loc;

   public XMIConfigurator() {
      try {
         SAXParserFactory spf = SAXParserFactory.newInstance();
         spf.setNamespaceAware(true);
         spf.setValidating(false);
         this.saxNonValidatingParser = spf.newSAXParser();
      } catch (FactoryConfigurationError var2) {
         System.err.println("Unrecoverable error - cannot create SAXParserFactory\n" + var2.getMessage());
         System.exit(-1);
      } catch (ParserConfigurationException var3) {
         System.err.println("Parser configuration exception\n" + var3.getMessage());
      } catch (SAXException var4) {
         System.err.println("Initialization exception\n" + var4.getMessage());
      }

   }

   private void reset() {
      this.classes = new ArrayList();
      this.rootVersionsMap = new HashMap();
      this.extension = new String("");
      this.defaultState = false;
      this.problemList = new ArrayList();
   }

   public Map parseConfigFile(String filename) {
      try {
         this.reset();
         this.saxNonValidatingParser.parse(new InputSource(filename), this);
      } catch (SAXParseException var4) {
         System.err.println("Parsing exception at line: " + var4.getLineNumber() + " in file " + var4.getSystemId());
         System.err.println(var4.getMessage());
      } catch (IOException var5) {
         System.err.println("IO Error in file " + filename + "\n" + var5.getMessage());
      } catch (SAXException var6) {
         Exception e = var6.getException() != null ? var6.getException() : var6;
         System.err.println("Application error " + var6.getLocalizedMessage());
         System.err.println("      " + ((Exception)e).getMessage());
      }

      return this.rootVersionsMap;
   }

   public void startElement(String uri, String localName, String qname, Attributes attributes) throws SAXException {
      String ext;
      if (qname.equals("MSX:Class")) {
         try {
            ext = attributes.getValue("name");
            ClassDescriptor tempCD = this.classIntrospector.introspect(ext);
            tempCD.setObjectInstantiateClass(Class.forName(attributes.getValue("name") + this.extension));
            this.classes.add(tempCD);
         } catch (ClassNotFoundException var21) {
            System.err.println("Class not found - " + attributes.getValue("name"));
            System.err.println(" >> " + var21.getMessage());
         }
      } else {
         String oldNamespace;
         String value;
         if (qname.equals("MSX:XMLMapping")) {
            ext = attributes.getValue("root");
            value = attributes.getValue("version");
            String metamodel = attributes.getValue("metamodel");
            oldNamespace = attributes.getValue("mversion");
            this.currentMapping = new XMLMapping();
            Hashtable vmm = (Hashtable)this.rootVersionsMap.get(ext);
            if (vmm == null) {
               vmm = new Hashtable();
               this.rootVersionsMap.put(ext, vmm);
            }

            Hashtable vmmv = (Hashtable)vmm.get(value);
            if (vmmv == null) {
               vmmv = new Hashtable();
               vmm.put(value, vmmv);
            }

            vmmv.put(metamodel + oldNamespace, this.currentMapping);
         } else {
            Class c;
            Class declClass;
            if (qname.equals("MSX:XMLClassMapping")) {
               try {
                  c = Class.forName(attributes.getValue("name"));
                  if (this.defaultState) {
                     this.defaultXCM = c;
                  } else {
                     declClass = Class.forName(attributes.getValue("owner"));
                     Iterator it = this.classes.iterator();

                     while(it.hasNext()) {
                        ClassDescriptor cd = (ClassDescriptor)it.next();
                        if (declClass.isAssignableFrom(cd.getDescriptedClass())) {
                           XMLClassMapping xcm = this.currentMapping.remove(cd, this.di);
                           XMLClassMapping newxcm = (XMLClassMapping)c.newInstance();
                           newxcm.clone(xcm);
                           int xmlType = XMIConstants.getIntValue(attributes.getValue("xmlType"));
                           if (xmlType > -1) {
                              newxcm.setXmlType(xmlType);
                           }

                           this.currentMapping.putClass(newxcm, this.di);
                        }
                     }
                  }
               } catch (ClassNotFoundException var25) {
                  System.err.println("Class not found - " + attributes.getValue("name"));
                  System.err.println(" >> " + var25.getMessage());
               } catch (InstantiationException var26) {
                  System.err.println("Instantiation exception for " + attributes.getValue("name"));
                  System.err.println(" >> " + var26.getMessage());
               } catch (IllegalAccessException var27) {
                  System.err.println("Illegal access for " + attributes.getValue("name"));
                  System.err.println(" >> " + var27.getMessage());
               }
            } else if (qname.equals("MSX:XMLFieldMapping")) {
               try {
                  c = Class.forName(attributes.getValue("name"));
                  if (this.defaultState) {
                     if (this.defaultXFM == null) {
                        this.defaultXFM = new ArrayList();
                     }

                     this.defaultXFM.add(c);
                  } else {
                     declClass = Class.forName(attributes.getValue("owner"));
                     int type = XMIConstants.getIntValue(attributes.getValue("xmlType"));
                     oldNamespace = attributes.getValue("oldXmlNamespace");
                     String oldName = attributes.getValue("oldXmlName");
                     String namespace = attributes.getValue("xmlNamespace");
                     String name = attributes.getValue("xmlName");
                     String oldQName = oldNamespace.length() > 0 ? oldNamespace + ":" + oldName : oldName;
                     String newQName = namespace.length() > 0 ? namespace + ":" + name : name;
                     Iterator it = this.classes.iterator();

                     while(it.hasNext()) {
                        ClassDescriptor cd = (ClassDescriptor)it.next();
                        if (declClass.isAssignableFrom(cd.getDescriptedClass())) {
                           int[] index = new int[1];
                           XMLClassMapping xcm = this.currentMapping.getClass(cd);
                           XMLFieldMapping oldField = xcm.remove(oldQName, index);
                           XMLFieldMapping xfm = (XMLFieldMapping)c.newInstance();
                           if (oldField != null) {
                              xfm.setMappedField(oldField.getMappedField());
                           }

                           xfm.setXmlName(name);
                           xfm.setXmlNamespace(namespace);
                           if (type != -1) {
                              xfm.setXmlType(type);
                           } else if (oldField != null) {
                              xfm.setXmlType(oldField.getXmlType());
                           } else {
                              xfm.setXmlType(0);
                           }

                           xcm.remove(newQName, new int[]{-1});
                           xcm.addField(index[0], xfm);
                        }
                     }
                  }
               } catch (ClassNotFoundException var22) {
                  System.err.println("Class not found - " + attributes.getValue("name"));
                  System.err.println(" >> " + var22.getMessage());
               } catch (InstantiationException var23) {
                  System.err.println("Instantiation exception for " + attributes.getValue("name"));
                  System.err.println(" >> " + var23.getMessage());
               } catch (IllegalAccessException var24) {
                  System.err.println("Illegal access for " + attributes.getValue("name"));
                  System.err.println(" >> " + var24.getMessage());
               }
            } else if (qname.equals("MSX:Constant")) {
               ext = attributes.getValue("name");
               value = attributes.getValue("value");
               this.currentMapping.putConstant(ext, value);
            } else if (qname.equals("MSX:XMLMapping.default")) {
               this.defaultState = true;
               this.problemList = new ArrayList();
               ext = attributes.getValue("declInterf");
               if (ext != null && !ext.equals("true")) {
                  this.di = false;
               } else {
                  this.di = true;
               }
            } else if (qname.equals("MSX:Metamodel")) {
               ext = attributes.getValue("ext");
               if (ext != null) {
                  this.extension = ext;
               } else {
                  this.extension = new String("");
               }
            } else if (qname.equals("MSX:XMLFieldMapping.problem")) {
               try {
                  XMIConfigurator.FieldProblem fp = new XMIConfigurator.FieldProblem(attributes.getValue("name"), attributes.getValue("newName"), Class.forName(attributes.getValue("owner")));
                  this.problemList.add(fp);
               } catch (ClassNotFoundException var20) {
                  System.err.println("Exception in XMLFieldMapping.problem" + var20.getMessage());
               }
            }
         }
      }

   }

   public void endElement(String uri, String localName, String qname) throws SAXException {
      if (qname.equals("MSX:XMLMapping.default")) {
         this.defaultState = false;
         Iterator it = this.classes.iterator();

         while(it.hasNext()) {
            try {
               ClassDescriptor cd = (ClassDescriptor)it.next();
               XMLClassMapping cm = (XMLClassMapping)this.defaultXCM.newInstance();
               cm.setMappedClass(cd);
               Enumeration en2 = cd.getFieldList();

               while(en2.hasMoreElements()) {
                  FieldDescriptor fd = (FieldDescriptor)en2.nextElement();
                  Iterator itx = this.defaultXFM.iterator();

                  while(itx.hasNext()) {
                     Class xfm = (Class)itx.next();
                     XMLFieldMapping oo = (XMLFieldMapping)xfm.newInstance();

                     for(int i = 0; i < this.problemList.size(); ++i) {
                        XMIConfigurator.FieldProblem fp = (XMIConfigurator.FieldProblem)this.problemList.get(i);
                        if (fp.name.equals(fd.getFieldName()) && fp.owner.isAssignableFrom(fd.getDeclaringClass())) {
                           fd.setFieldName(fp.newName);
                        }
                     }

                     oo.setMappedField(fd);
                     cm.addField(oo);
                  }
               }

               this.currentMapping.putClass(cm, this.di);
            } catch (InstantiationException var14) {
               System.err.println("Problems instantiating XMLClassMapping " + var14.getMessage());
            } catch (IllegalAccessException var15) {
               System.err.println("Problems instantiating XMLClassMapping " + var15.getMessage());
            }
         }

         this.defaultXFM = null;
         this.defaultXCM = null;
      } else if (qname.equals("MSX:Metamodel")) {
         this.classes.clear();
      }

   }

   public void setDocumentLocator(Locator loc) {
      this.loc = loc;
   }

   private class FieldProblem {
      String name;
      String newName;
      Class owner;

      public FieldProblem(String n, String nn, Class o) {
         this.name = n;
         this.newName = nn;
         this.owner = o;
      }
   }
}
