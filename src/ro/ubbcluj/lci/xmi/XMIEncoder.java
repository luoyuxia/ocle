package ro.ubbcluj.lci.xmi;

import java.beans.ExceptionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import ro.ubbcluj.lci.xmi.behavior.XMIIdGenerator;
import ro.ubbcluj.lci.xmi.config.XMIConfigurator;
import ro.ubbcluj.lci.xmi.structure.FieldDescriptor;
import ro.ubbcluj.lci.xmi.structure.XMLClassMapping;
import ro.ubbcluj.lci.xmi.structure.XMLFieldMapping;
import ro.ubbcluj.lci.xmi.structure.XMLMapping;

public class XMIEncoder implements XMIEncoderConstants {
   private Map supportedMappings;
   private XMLMapping xmlMapping;
   private List externalMappings;
   private String method;
   private String version;
   private String metaname;
   private String metaver;
   private String filename;
   private String baseDirectory;
   private ExceptionListener exceptionListener;
   private boolean isSessionOpen;
   private Transformer transformer;
   private DOMImplementation di;
   private Document document;
   private Element root;
   private Map idMap;
   private Map hrefMap;
   private XMIIdGenerator xig;
   private HashSet exportedElements;
   private String tempClassName;
   private String tempFieldName;

   public XMIEncoder() {
      try {
         TransformerFactory tf = TransformerFactory.newInstance();
         this.transformer = tf.newTransformer();
         this.transformer.setOutputProperty("method", "xml");
         this.transformer.setOutputProperty("version", "1.0");
         this.transformer.setOutputProperty("indent", "yes");
         this.transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "1");
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         dbf.setNamespaceAware(true);
         DocumentBuilder db = dbf.newDocumentBuilder();
         this.di = db.getDOMImplementation();
         this.xig = new XMIIdGenerator();
         this.isSessionOpen = false;
      } catch (TransformerConfigurationException var4) {
         System.err.println("Initialization exception: " + var4.getMessage());
      } catch (ParserConfigurationException var5) {
         System.err.println("Initialization exception: " + var5.getMessage());
      }

   }

   public void parseConfiguration(String filename) {
      this.supportedMappings = (new XMIConfigurator()).parseConfigFile(filename);
   }

   public boolean isSessionOpen() {
      return this.isSessionOpen;
   }

   public void setExceptionListener(ExceptionListener el) {
      this.exceptionListener = el;
   }

   public void setSupportedMappings(Map supportedMappings) {
      this.supportedMappings = supportedMappings;
   }

   public void setExternalMappings(List externalMappings) {
      this.externalMappings = externalMappings;
   }

   public void addExternalMapping(String method, String version, String metaname, String metaver) {
      XMLMapping xm = this.detectXMIMapping(method, version, metaname, metaver);
      this.externalMappings.add(xm);
   }

   public void addExternalMapping(XMLMapping xm) {
      this.externalMappings.add(xm);
   }

   protected void createDocument() {
      this.document = this.di.createDocument((String)null, this.method, (DocumentType)null);
      this.root = this.document.getDocumentElement();
      this.root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:UML", "//org.omg/UML/1.3");
      if (this.metaname.equals("GXAPI")) {
         this.root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:GXML", "//lci.cs.ubbcluj.ro/gxml");
      }

      this.root.setAttribute("xmi.version", this.version);
      Element xmiHeader = this.document.createElement("XMI.header");
      Element xmiDocumentation = this.document.createElement("XMI.documentation");
      Element xmiExporter = this.document.createElement("XMI.exporter");
      xmiExporter.appendChild(this.document.createTextNode("ocle 2.0"));
      Element xmiExporterVersion = this.document.createElement("XMI.exporterVersion");
      xmiExporterVersion.appendChild(this.document.createTextNode("1.0"));
      xmiDocumentation.appendChild(xmiExporter);
      xmiDocumentation.appendChild(xmiExporterVersion);
      Element xmiMetamodel = this.document.createElement("XMI.metamodel");
      xmiMetamodel.setAttribute("xmi.name", this.metaname);
      xmiMetamodel.setAttribute("xmi.version", this.metaver);
      xmiHeader.appendChild(xmiDocumentation);
      xmiHeader.appendChild(xmiMetamodel);
      this.root.appendChild(xmiHeader);
      Element xmiContent = this.document.createElement("XMI.content");
      this.root.appendChild(xmiContent);
      this.root = xmiContent;
   }

   protected void initialize() {
      this.xig.resetIdCounter();
      this.idMap = new HashMap();
      this.exportedElements = new HashSet();
      this.createDocument();
   }

   protected void cleanup() {
      this.idMap = null;
      this.exportedElements = null;
      this.xmlMapping = null;
      this.document = null;
      this.root = null;
   }

   public void newSession() {
      this.filename = null;
      this.baseDirectory = "";
      this.hrefMap = new HashMap();
      this.externalMappings = new ArrayList();
      this.isSessionOpen = true;
   //   System.out.println("New encoding session .. ");
   }

   public void newSession(File directory) {
      try {
         this.filename = null;
         this.baseDirectory = directory.getCanonicalPath();
         this.hrefMap = new HashMap();
         this.externalMappings = new ArrayList();
      } catch (IOException var3) {
         this.simulateException("** IO Error for " + this.baseDirectory + "\n      " + var3.getMessage());
      }

      this.isSessionOpen = true;
   //   System.out.println("New encoding session .. ");
   }

   public void closeSession() {
      if (this.isSessionOpen) {
         this.hrefMap = null;
         this.externalMappings = null;
         this.cleanup();
         this.isSessionOpen = false;
      //   System.out.println("Encoding session closed..");
      }

   }

   public void encode(Object ro, String filename, String method, String version, String metaname, String metaver) {
      this.encode(ro, new File(filename), method, version, metaname, metaver);
   }

   public void encode(Object ro, File file, String method, String version, String metaname, String metaver) {
      try {
         if (!file.exists()) {
            file.createNewFile();
         }

         String tempfile = file.getCanonicalPath();
         int index = tempfile.indexOf(this.baseDirectory);
         if (index == -1) {
            this.simulateException("Wrong session prepared for file " + tempfile + " and base directory " + this.baseDirectory);
         } else {
            this.filename = tempfile.substring(index + this.baseDirectory.length() + 1);
         }

         this.xmlMapping = this.detectXMIMapping(method, version, metaname, metaver);
         this.method = method;
         this.version = version;
         this.metaname = metaname;
         this.metaver = metaver;
         this.initialize();
         this.objectToXML(ro, this.root, false);
         this.exportLeftObjects(this.root);
         this.transformer.transform(new DOMSource(this.document), new StreamResult(file));
      } catch (TransformerException var9) {
         var9.printStackTrace();
         this.simulateException("Transformer exception while writing output file: " + var9.getMessage());
      } catch (IOException var10) {
         var10.printStackTrace();
         this.simulateException("IO exception while encoding: " + var10.getMessage());
      }

   }

   protected void objectToXML(Object object, Element parent, boolean justAttrs) {
      if (this.exportedElements.contains(object)) {
         System.err.println("Trying to export more than once: " + object.toString());
      } else {
         this.exportedElements.add(object);
         Class type = object.getClass();
         XMLClassMapping xcm = this.xmlMapping.getClass(type);
         if (xcm != null) {
            this.tempClassName = xcm.getXmlName();
            Element objElem = this.document.createElementNS("http://lci/xml", xcm.getQXmlName());
            objElem.setAttribute("xmi.id", this.getId(object));
            this.hrefMap.put(object, this.filename + "|" + this.getId(object));
            parent.appendChild(objElem);
            Enumeration xfields = xcm.getFieldList();

            label114:
            while(xfields.hasMoreElements()) {
               try {
                  XMLFieldMapping xfm = (XMLFieldMapping)xfields.nextElement();
                  if (xfm.getXmlType() != 6) {
                     Object value = xfm.getMappedFieldValue(object);
                     FieldDescriptor fm;
                     switch(xfm.getXmlType()) {
                     case 0:
                        fm = xfm.getMappedField();
                        this.tempFieldName = fm != null ? fm.getFieldName() : "";
                        String xmlValue = this.valueToString(value);
                        if (xmlValue != null) {
                           objElem.setAttribute(xfm.getXmlName(), xmlValue);
                        }
                     case 1:
                     case 5:
                     case 6:
                     default:
                        break;
                     case 2:
                        fm = xfm.getMappedField();
                        this.tempFieldName = fm != null ? fm.getFieldName() : "";
                        this.tempFieldName = xfm.getMappedField().getFieldName();
                        String xmlCdata = this.valueToString(value);
                        if (xmlCdata != null) {
                           Element fieldElem = this.document.createElementNS("http://lci/xml", xfm.getQXmlName());
                           fieldElem.appendChild(this.document.createTextNode(xmlCdata));
                           objElem.appendChild(fieldElem);
                        }
                        break;
                     case 3:
                        ArrayList valueObjects = this.valueToObjects(value);
                        if (valueObjects.size() <= 0 || justAttrs) {
                           break;
                        }

                        Element fieldElem = this.document.createElementNS("http://lci/xml", xfm.getQXmlName());
                        objElem.appendChild(fieldElem);
                        Iterator it = valueObjects.iterator();

                        while(true) {
                           if (!it.hasNext()) {
                              continue label114;
                           }

                           Object next = it.next();
                           this.objectToXML(next, fieldElem, justAttrs);
                        }
                     case 4:
                        ArrayList valueObjectsHref = this.valueToObjects(value);
                        if (valueObjectsHref.size() <= 0 || justAttrs) {
                           break;
                        }

                         fieldElem = this.document.createElementNS("http://lci/xml", xfm.getQXmlName());
                        objElem.appendChild(fieldElem);
                         it = valueObjectsHref.iterator();

                        while(it.hasNext()) {
                           Object voh = it.next();
                           Class vohType = voh.getClass();
                           XMLClassMapping vohXcm = this.xmlMapping.getClass(vohType);
                           if (vohXcm == null) {
                              boolean found = false;
                              Iterator itx = this.externalMappings.iterator();

                              while(itx.hasNext()) {
                                 XMLMapping temp = (XMLMapping)itx.next();
                                 vohXcm = temp.getClass(vohType);
                                 if (vohXcm != null) {
                                    found = true;
                                    break;
                                 }
                              }

                              if (!found) {
                                 this.simulateException("Trying to link unspecified XMI element: " + voh);
                              }
                           }

                           Element fieldChildElem = this.document.createElementNS("http://lci/xml", vohXcm.getQXmlName());
                           fieldElem.appendChild(fieldChildElem);
                           fieldChildElem.setAttribute("xml:link", "simple");
                           String xmlHref = (String)this.hrefMap.get(voh);
                           fieldChildElem.setAttribute("href", "../" + xmlHref);
                        }
                        break;
                     case 7:
                        ArrayList valueObjectsIdref = this.valueToObjects(value);
                        if (valueObjectsIdref.size() > 0 && !justAttrs) {
                            fieldElem = this.document.createElementNS("http://lci/xml", xfm.getQXmlName());
                           objElem.appendChild(fieldElem);
                            it = valueObjectsIdref.iterator();

                           while(it.hasNext()) {
                              Object idrefObj = it.next();
                              Class idrefType = idrefObj.getClass();
                              XMLClassMapping idrefXcm = this.xmlMapping.getClass(idrefType);
                              Element fieldChildElem = this.document.createElementNS("http://lci/xml", idrefXcm.getQXmlName());
                              fieldElem.appendChild(fieldChildElem);
                              fieldChildElem.setAttribute("xmi.idref", this.getId(idrefObj));
                           }
                        }
                     }
                  }
               } catch (IllegalAccessException var22) {
                  var22.printStackTrace();
                  this.simulateException("Exception getting value of " + object.toString() + "\n" + var22.getMessage());
               } catch (InvocationTargetException var23) {
                  var23.printStackTrace();
                  this.simulateException("Exception getting value of " + object.toString() + "\n" + var23.getMessage());
               }
            }

         }
      }
   }

   protected void exportLeftObjects(Element parent) {
      boolean toBeContinued = true;

      do {
         HashMap cloneIdMap = new HashMap(this.idMap);
         Iterator it = cloneIdMap.keySet().iterator();

         while(it.hasNext()) {
            Object object = it.next();
            if (!this.exportedElements.contains(object)) {
               this.objectToXML(object, parent, true);
            }
         }

         toBeContinued = cloneIdMap.keySet().size() != this.idMap.keySet().size();
      } while(toBeContinued);

   }

   protected String valueToString(Object value) {
      if (value == null) {
         return null;
      } else if (!(value instanceof String) && !(value instanceof Number) && !(value instanceof Boolean)) {
         if (!(value instanceof Enumeration)) {
            String id = this.getId(value);
            return id;
         } else {
            boolean flagExists = false;
            StringBuffer sb = new StringBuffer();
            Enumeration valueAsEnum = (Enumeration)value;

            while(valueAsEnum.hasMoreElements()) {
               Object next = valueAsEnum.nextElement();
               String nextAsString = null;
               if (!(next instanceof String) && !(value instanceof Number) && !(value instanceof Boolean)) {
                  flagExists = true;
                  nextAsString = this.getId(next);
               } else {
                  flagExists = true;
                  nextAsString = next.toString();
               }

               if (sb.length() > 0) {
                  sb.append(" " + nextAsString);
               } else {
                  sb.append(nextAsString);
               }
            }

            return flagExists ? sb.toString() : null;
         }
      } else {
         if (value instanceof Integer) {
            if (this.tempFieldName.equals("visibility")) {
               return visibilityToString(((Integer)value).intValue());
            }

            if (this.tempFieldName.equals("aggregation")) {
               return aggregationToString(((Integer)value).intValue());
            }

            if (this.tempFieldName.equals("ordering")) {
               return orderingToString(((Integer)value).intValue());
            }

            if (this.tempFieldName.endsWith("Scope")) {
               return scopeToString(((Integer)value).intValue());
            }

            if (this.tempFieldName.equals("concurrency")) {
               return concurrencyToString(((Integer)value).intValue());
            }

            if (this.tempFieldName.equals("changeability")) {
               return changeabilityToString(((Integer)value).intValue());
            }

            if (this.tempFieldName.equals("kind")) {
               if (this.tempClassName.equals("Parameter")) {
                  return parameterDirectionToString(((Integer)value).intValue());
               }

               if (this.tempClassName.equals("Pseudostate")) {
                  return pseudostateToString(((Integer)value).intValue());
               }
            }
         }

         return value.toString();
      }
   }

   protected ArrayList valueToObjects(Object value) {
      ArrayList array = new ArrayList();
      if (value != null) {
         if (value instanceof Enumeration) {
            Enumeration valueAsEnum = (Enumeration)value;

            while(valueAsEnum.hasMoreElements()) {
               array.add(valueAsEnum.nextElement());
            }
         } else {
            array.add(value);
         }
      }

      return array;
   }

   private String getId(Object o) {
      String id = (String)this.idMap.get(o);
      if (id == null) {
         id = this.xig.getNewID();
         this.idMap.put(o, id);
      }

      return id;
   }

   private XMLMapping detectXMIMapping(String method, String version, String mname, String mver) {
      Hashtable ht = (Hashtable)this.supportedMappings.get(method);
      if (ht == null) {
         this.simulateException("Invalid export method!!! " + method);
         return null;
      } else {
         Hashtable ht1 = (Hashtable)ht.get(version);
         if (ht1 == null) {
            this.simulateException("Invalid export version!!! " + version);
            return null;
         } else {
            XMLMapping xm = (XMLMapping)ht1.get(mname + mver);
            if (xm == null) {
               this.simulateException("Invalid export metamodel name and version!!! " + mname + " " + mver);
               return null;
            } else {
               return xm;
            }
         }
      }
   }

   private void simulateException(String msg) {
      System.err.println(msg);
      if (this.exceptionListener != null) {
         this.exceptionListener.exceptionThrown(new Exception(msg));
      } else {
         System.err.println(msg);
      }

   }

   private static String visibilityToString(int code) {
      switch(code) {
      case 0:
         return "private";
      case 1:
         return "package";
      case 2:
         return "protected";
      default:
         return "public";
      }
   }

   private static String aggregationToString(int code) {
      switch(code) {
      case 0:
         return "composite";
      case 2:
         return "aggregate";
      default:
         return "none";
      }
   }

   private static String concurrencyToString(int code) {
      switch(code) {
      case 0:
         return "concurrent";
      case 2:
         return "guarded";
      default:
         return "sequential";
      }
   }

   private static String changeabilityToString(int code) {
      switch(code) {
      case 0:
         return "frozen";
      case 1:
         return "addOnly";
      default:
         return "changeable";
      }
   }

   private static String orderingToString(int code) {
      switch(code) {
      case 0:
         return "ordered";
      default:
         return "unordered";
      }
   }

   private static String parameterDirectionToString(int code) {
      switch(code) {
      case 0:
         return "inout";
      case 1:
         return "out";
      case 2:
      default:
         return "in";
      case 3:
         return "return";
      }
   }

   private static String pseudostateToString(int code) {
      switch(code) {
      case 0:
         return "fork";
      case 1:
         return "shallowHistory";
      case 2:
         return "join";
      case 3:
         return "initial";
      case 4:
      default:
         return "choice";
      case 5:
         return "deepHistory";
      case 6:
         return "junction";
      }
   }

   private static String scopeToString(int code) {
      switch(code) {
      case 0:
         return "classifier";
      default:
         return "instance";
      }
   }
}
