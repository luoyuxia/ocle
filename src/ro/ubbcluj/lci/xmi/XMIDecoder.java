package ro.ubbcluj.lci.xmi;

import java.beans.ExceptionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import ro.ubbcluj.lci.utils.progress.ProgressListener;
import ro.ubbcluj.lci.xmi.behavior.QueueSeparatorStacks;
import ro.ubbcluj.lci.xmi.behavior.TemporaryObjectReplacer;
import ro.ubbcluj.lci.xmi.behavior.XMLMappedObject;
import ro.ubbcluj.lci.xmi.config.XMIConfigurator;
import ro.ubbcluj.lci.xmi.structure.XMLClassMapping;
import ro.ubbcluj.lci.xmi.structure.XMLFieldMapping;
import ro.ubbcluj.lci.xmi.structure.XMLMapping;
import ro.ubbcluj.lci.xmi.utils.ProgressFileInputStream;
import ro.ubbcluj.lci.xmi.utils.XMIUtilities;

public class XMIDecoder {
   static final boolean DEBUG = false;
   static final boolean EXCEPTION_DEBUG = false;
   private Map supportedMappings;
   private SAXParser saxParser;
   private XMIDecoder.XMIDefaultHandler xmiHandler = new XMIDecoder.XMIDefaultHandler();
   private ArrayList filesToParse;
   private Map filesParsed;
   private Map sessionIdMaps;
   private Map sessionUuidMaps;
   private String filename;
   private String baseDirectory;
   private boolean autoFollowLinks = true;
   private ExceptionListener exceptionListener;
   private ProgressListener progressListener;
   private boolean isSessionOpen = false;

   public XMIDecoder() {
      try {
         SAXParserFactory spf = SAXParserFactory.newInstance();
         spf.setNamespaceAware(true);
         spf.setValidating(false);
         this.saxParser = spf.newSAXParser();
      } catch (SAXException var2) {
         this.simulateException(new XMIErrorException(var2));
      } catch (ParserConfigurationException var3) {
         this.simulateException(new XMIErrorException(var3));
      } catch (FactoryConfigurationError var4) {
         this.simulateException(new XMIErrorException(var4.getException()));
      }

   }

   public void parseConfiguration(String filename) {
      this.supportedMappings = (new XMIConfigurator()).parseConfigFile(filename);
   }

   public void newSession(File directory) {
      this.closeSession();

      try {
         this.filename = null;
         this.baseDirectory = directory != null ? XMIUtilities.toURI(directory.getCanonicalPath(), true) : "";
         this.sessionIdMaps = new HashMap();
         this.sessionUuidMaps = new HashMap();
         this.filesToParse = new ArrayList();
         this.filesParsed = new HashMap();
      } catch (IOException var3) {
         this.simulateException(new XMIErrorException(var3));
         return;
      }

      this.isSessionOpen = true;
   //   System.out.println("New decoding session .. ");
   }

   public void closeSession() {
      if (this.isSessionOpen) {
         this.sessionIdMaps = null;
         this.sessionUuidMaps = null;
         this.filesToParse = null;
         this.filesParsed = null;
         this.xmiHandler.cleanup();
         this.isSessionOpen = false;
     //    System.out.println("Decoding session closed..");
      }

   }

   public Object decode(String filename) {
      return this.decode(new File(filename));
   }

   public Object decode(URI fileURI) {
      return this.decode(new File(fileURI));
   }

   public Object decode(File file) {
      Object metadataRoot = this.internalDecode(file);
      if (metadataRoot != null && this.autoFollowLinks) {
         for(boolean moreToParse = this.filesToParse.size() > 0; moreToParse; moreToParse = this.filesToParse.size() > 0) {
            String filename = (String)this.filesToParse.remove(0);
            if (!this.filesParsed.containsKey(filename)) {
               this.internalDecode(new File(this.baseDirectory + filename));
            }
         }
      }

      return metadataRoot;
   }

   public Object internalDecode(File file) {
      long t0 = System.currentTimeMillis();

      try {
         String systemId = XMIUtilities.createLocalURI(file.getCanonicalPath());
         String tempfile = XMIUtilities.toURI(file.getCanonicalPath(), false);
         this.filename = XMIUtilities.resolveRelativePath(this.baseDirectory, tempfile);
         if (this.filename == null) {
            this.simulateException(new XMIErrorException("Wrong session prepared for file " + tempfile + " and base directory " + this.baseDirectory));
         }

     //    System.out.println(">> Decoding: " + this.filename);
         this.xmiHandler.initialize();
         if (!this.sessionIdMaps.containsKey(this.filename)) {
            this.sessionIdMaps.put(this.filename, this.xmiHandler.idMap);
         }

         if (!this.sessionUuidMaps.containsKey(this.filename)) {
            this.sessionUuidMaps.put(this.filename, this.xmiHandler.uuidMap);
         }

         InputSource is = null;
         if (this.progressListener != null) {
            is = new InputSource(new ProgressFileInputStream(tempfile, this.progressListener));
         } else {
            is = new InputSource(new FileInputStream(tempfile));
         }

         is.setSystemId(systemId);
         this.saxParser.parse(is, this.xmiHandler);
     //    System.out.println(" ... " + (System.currentTimeMillis() - t0) + " miliseconds elapsed");
         Object rootObj = this.xmiHandler.getMetadataRoot();
         this.filesParsed.put(this.filename, rootObj);
         this.xmiHandler.cleanup();
         return rootObj;
      } catch (SAXParseException var8) {
         this.simulateException(new XMIErrorException(var8));
      } catch (SAXException var9) {
         this.simulateException(new XMIErrorException(var9));
      } catch (IOException var10) {
         this.simulateException(new XMIErrorException(var10));
      }

      return null;
   }

   public boolean isSessionOpen() {
      return this.isSessionOpen;
   }

   public boolean isAutoFollowLinks() {
      return this.autoFollowLinks;
   }

   public void setSupportedMappings(Map supportedMappings) {
      this.supportedMappings = supportedMappings;
   }

   public void setMetadataRootClass(Class metaclass) {
      this.xmiHandler.setMetadataRootClass(metaclass);
   }

   public void setValidating(boolean validating) {
      if (this.saxParser.isValidating() != validating) {
         try {
            this.saxParser.getXMLReader().setFeature("http://xml.org/sax/features/validation", validating);
         } catch (SAXNotRecognizedException var3) {
            this.simulateException(new XMIErrorException(var3));
         } catch (SAXNotSupportedException var4) {
            this.simulateException(new XMIErrorException(var4));
         } catch (SAXException var5) {
            this.simulateException(new XMIErrorException(var5));
         }
      }

   }

   public void setProgressListener(ProgressListener progressListener) {
      this.progressListener = progressListener;
   }

   public void setExceptionListener(ExceptionListener exceptionListener) {
      this.exceptionListener = exceptionListener;
   }

   public void setAutoFollowLinks(boolean followLinks) {
      this.autoFollowLinks = followLinks;
   }

   void simulateException(Exception e) {
      if (this.exceptionListener != null) {
         this.exceptionListener.exceptionThrown(e);
      } else {
         System.err.println(e.getMessage());
      }

   }

   private int stringToIntKind(XMLFieldMapping mappedField, String value) {
      String tempFieldName = mappedField.getXmlName();
      if (tempFieldName.equals("visibility")) {
         return visibilityToInt(value);
      } else if (tempFieldName.equals("aggregation")) {
         return aggregationToInt(value);
      } else if (tempFieldName.equals("ordering")) {
         return orderingToInt(value);
      } else if (tempFieldName.endsWith("Scope")) {
         return scopeToInt(value);
      } else if (tempFieldName.equals("concurrency")) {
         return concurrencyToInt(value);
      } else if (tempFieldName.equals("changeability")) {
         return changeabilityToInt(value);
      } else {
         if (tempFieldName.equals("kind")) {
            if (value.equals("return") || value.equals("inout") || value.equals("in") || value.equals("out")) {
               return parameterDirectionToInt(value);
            }

            if (value.equals("shallowHistory") || value.equals("junction") || value.equals("join") || value.equals("initial") || value.equals("fork") || value.equals("deepHistory")) {
               return pseudostateToInt(value);
            }
         }

         return -1;
      }
   }

   private static int visibilityToInt(String value) {
      if (value.equals("package")) {
         return 1;
      } else if (value.equals("private")) {
         return 0;
      } else {
         return value.equals("protected") ? 2 : 3;
      }
   }

   private static int aggregationToInt(String value) {
      if (value.equals("composite")) {
         return 0;
      } else {
         return value.equals("aggregate") ? 2 : 1;
      }
   }

   private static int concurrencyToInt(String value) {
      if (value.equals("concurrent")) {
         return 0;
      } else {
         return value.equals("guarded") ? 2 : 1;
      }
   }

   private static int changeabilityToInt(String value) {
      if (value.equals("addOnly")) {
         return 1;
      } else {
         return value.equals("frozen") ? 0 : 2;
      }
   }

   private static int orderingToInt(String value) {
      return value.equals("ordered") ? 0 : 1;
   }

   private static int parameterDirectionToInt(String value) {
      if (value.equals("return")) {
         return 3;
      } else if (value.equals("inout")) {
         return 0;
      } else {
         return value.equals("out") ? 1 : 2;
      }
   }

   private static int pseudostateToInt(String value) {
      if (value.equals("shallowHistory")) {
         return 1;
      } else if (value.equals("junction")) {
         return 6;
      } else if (value.equals("join")) {
         return 2;
      } else if (value.equals("initial")) {
         return 3;
      } else if (value.equals("fork")) {
         return 0;
      } else {
         return value.equals("deepHistory") ? 5 : 4;
      }
   }

   private static int scopeToInt(String value) {
      return value.equals("classifier") ? 0 : 1;
   }

   private class XMIDefaultHandler extends DefaultHandler {
      private boolean ignoreSection;
      private String ignoredQname;
      private int ignoredDepth;
      private static final String DEFAULT_MNAME = "UML";
      private static final String DEFAULT_MVER = "1.3";
      private HashMap idMap;
      private HashMap uuidMap;
      private Stack linksStack;
      private Stack objectsStack;
      private QueueSeparatorStacks objectsQueueSS;
      private Object metadataRoot;
      private Class metadataRootClass;
      private StringBuffer tempBuffer;
      private XMLFieldMapping lastLink;
      private int xmlDepthLevel;
      private boolean wasLink;
      private boolean wasHref;
      private Locator loc;
      private Hashtable tempHash;
      private String mname = null;
      private String mver = null;
      private XMLMapping xmlMapping;

      public XMIDefaultHandler() {
      }

      public void initialize() {
         this.ignoreSection = false;
         this.ignoredQname = null;
         this.ignoredDepth = -1;
         if (XMIDecoder.this.filename != null && XMIDecoder.this.sessionIdMaps.containsKey(XMIDecoder.this.filename)) {
            this.idMap = (HashMap)XMIDecoder.this.sessionIdMaps.get(XMIDecoder.this.filename);
         } else {
            this.idMap = new HashMap();
         }

         if (XMIDecoder.this.filename != null && XMIDecoder.this.sessionUuidMaps.containsKey(XMIDecoder.this.filename)) {
            this.uuidMap = (HashMap)XMIDecoder.this.sessionUuidMaps.get(XMIDecoder.this.filename);
         } else {
            this.uuidMap = new HashMap();
         }

         this.linksStack = new Stack();
         this.objectsStack = new Stack();
         this.objectsQueueSS = new QueueSeparatorStacks();
         this.tempBuffer = new StringBuffer();
         this.xmlDepthLevel = -1;
         this.wasLink = false;
      }

      public void cleanup() {
         this.ignoreSection = false;
         this.ignoredQname = null;
         this.ignoredDepth = -1;
         this.idMap = null;
         this.uuidMap = null;
         this.linksStack = null;
         this.objectsStack = null;
         this.objectsQueueSS = null;
         this.tempBuffer = null;
         this.lastLink = null;
         this.xmlMapping = null;
         this.metadataRoot = null;
         this.metadataRootClass = null;
      }

      public void setMetadataRootClass(Class metaclass) {
         this.metadataRootClass = metaclass;
      }

      public Object getMetadataRoot() {
         return this.metadataRoot;
      }

      public void characters(char[] ch, int start, int length) throws SAXException {
         if (this.xmlMapping != null) {
            if (this.wasLink) {
               this.tempBuffer.append(ch, start, length);
            }

         }
      }

      public void startElement(String uri, String localName, String qname, Attributes attributes) throws SAXException {
         if (this.xmlDepthLevel == -1 && this.xmlMapping == null) {
            this.detectXMITempHash(qname, attributes);
         }

         if ("XMI.metamodel".equals(qname)) {
            this.mname = attributes.getValue("xmi.name");
            this.mver = attributes.getValue("xmi.version");
         }

         ++this.xmlDepthLevel;
         if (!this.ignoreSection && !"XMI".equals(qname) && !"XMI.content".equals(qname)) {
            boolean prevWasHref = this.wasHref;
            boolean prevWasLink = this.wasLink;
            this.wasHref = false;
            this.wasLink = false;
            if (this.xmlMapping != null) {
               String xmiHref = attributes.getValue("href");
               if (xmiHref != null) {
                  this.processAttrHref(xmiHref);
                  this.wasHref = true;
               } else {
                  XMLClassMapping mappedClass = this.xmlMapping.getClass(qname);
                  XMLMappedObject mappedObject;
                  String xmiId;
                  if (mappedClass != null) {
                     mappedObject = null;
                     String xmiIdref = attributes.getValue("xmi.idref");
                     String xmiUuidref = attributes.getValue("xmi.uuidref");
                     if (xmiIdref != null) {
                        this.processAttrXmiIdref(xmiIdref);
                     } else if (xmiUuidref != null) {
                        this.processAttrXmiUuidref(xmiUuidref);
                     } else {
                        try {
                           mappedObject = mappedClass.createMappedObject();
                           xmiId = attributes.getValue("xmi.id");
                           if (xmiId != null) {
                              this.processAttrXmiId(xmiId, mappedObject);
                           }

                           String xmiUuid = attributes.getValue("xmi.uuid");
                           if (xmiUuid != null) {
                              this.processAttrXmiUuid(xmiUuid, mappedObject);
                           }

                           this.setAttributes(mappedObject, attributes);
                           this.objectsStack.push(mappedObject);
                        } catch (Exception var14) {
                           SAXParseException spe = new SAXParseException(var14.getMessage() + " while creating object " + qname, this.loc);
                           throw new SAXException(spe);
                        }
                     }
                  } else if (!this.objectsStack.empty()) {
                     mappedObject = (XMLMappedObject)this.objectsStack.peek();
                     XMLClassMapping lastMappedClass = mappedObject.getMappedClass();
                     XMLFieldMapping mappedField = lastMappedClass.getFieldFromXMLLink(qname);
                     if (mappedField != null) {
                        this.wasLink = true;
                        this.tempBuffer.setLength(0);
                        this.lastLink = mappedField;
                        this.linksStack.push(mappedField);
                        this.objectsQueueSS.pushSeparator();
                        xmiId = attributes.getValue("xmi.value");
                        if (xmiId != null) {
                           Object[] params = this.buildParams(mappedField, xmiId);
                           this.setLink(mappedObject, mappedField, params);
                        }
                     } else {
                        this.ignoredQname = qname;
                        this.ignoredDepth = this.xmlDepthLevel - 1;
                        this.ignoreSection = true;
                        this.wasHref = prevWasHref;
                        this.wasLink = prevWasLink;
                     }
                  } else {
                     this.ignoredQname = qname;
                     this.ignoredDepth = this.xmlDepthLevel - 1;
                     this.ignoreSection = true;
                     this.wasHref = prevWasHref;
                     this.wasLink = prevWasLink;
                  }

               }
            }
         }
      }

      public void endElement(String uri, String localName, String qname) throws SAXException {
         if ("XMI.header".equals(qname)) {
            this.detectXMIMapping();
         }

         this.wasLink = false;
         --this.xmlDepthLevel;
         if (this.ignoreSection) {
            if (qname.equals(this.ignoredQname) && this.xmlDepthLevel == this.ignoredDepth) {
               this.ignoreSection = false;
            }

         } else if (this.wasHref) {
            this.wasHref = false;
         } else if (this.xmlMapping != null) {
            if (this.xmlMapping.containsClass(qname)) {
               Object mappedObject = this.objectsStack.pop();
               if (this.metadataRootClass == null && this.objectsStack.empty() && this.metadataRoot == null) {
                  this.metadataRoot = ((XMLMappedObject)mappedObject).getMappedObject();
               } else if (this.metadataRootClass != null && mappedObject != null && mappedObject instanceof XMLMappedObject) {
                  Object obj = ((XMLMappedObject)mappedObject).getMappedObject();
                  if (obj != null && this.metadataRootClass.isInstance(obj)) {
                     this.metadataRoot = obj;
                  }
               }

               if (!this.linksStack.empty()) {
                  if (mappedObject instanceof XMLMappedObject) {
                     this.objectsQueueSS.push(((XMLMappedObject)mappedObject).getMappedObject());
                  } else {
                     this.objectsQueueSS.push(mappedObject);
                  }
               }
            } else if (!this.objectsStack.empty()) {
               if (this.tempBuffer.length() > 0) {
                  String s = this.tempBuffer.toString();
                  if (s.trim().length() > 0) {
                     Object[] paramsx = this.buildParams(this.lastLink, s);

                     for(int i = 0; i < paramsx.length; ++i) {
                        this.objectsQueueSS.push(paramsx[i]);
                     }
                  }
               }

               this.tempBuffer.setLength(0);
               XMLMappedObject mappedObjectx = (XMLMappedObject)this.objectsStack.peek();
               XMLClassMapping mappedClass = mappedObjectx.getMappedClass();
               if (mappedClass.getFieldFromXMLLink(qname) != null) {
                  XMLFieldMapping mappedField = (XMLFieldMapping)this.linksStack.pop();
                  Object[] params = this.objectsQueueSS.pop();
                  this.setLink(mappedObjectx, mappedField, params);
               }
            }

         }
      }

      public void fatalError(SAXParseException saxpe) throws SAXParseException {
         throw saxpe;
      }

      public void error(SAXParseException saxpe) throws SAXParseException {
         throw saxpe;
      }

      public void warning(SAXParseException saxpe) throws SAXParseException {
         XMIDecoder.this.simulateException(new XMIWarningException(saxpe));
      }

      public void setDocumentLocator(Locator loc) {
         this.loc = loc;
      }

      protected void processAttrXmiId(String attrValue, XMLMappedObject mappedObject) throws SAXException {
         Object tor = this.idMap.get(attrValue);
         if (tor != null && tor instanceof TemporaryObjectReplacer) {
            this.idMap.remove(attrValue);
            ((TemporaryObjectReplacer)tor).setReplacedObject(mappedObject);
         } else if (tor != null) {
            SAXParseException spe = new SAXParseException("Collision between XML ids. See: " + attrValue, this.loc);
            throw new SAXException(spe);
         }

         this.idMap.put(attrValue, mappedObject);
      }

      protected void processAttrXmiUuid(String attrValue, XMLMappedObject mappedObject) throws SAXException {
         Object tor = this.uuidMap.get(attrValue);
         if (tor != null && tor instanceof TemporaryObjectReplacer) {
            this.uuidMap.remove(attrValue);
            ((TemporaryObjectReplacer)tor).setReplacedObject(mappedObject);
         } else if (tor != null) {
            SAXParseException spe = new SAXParseException("Collision between XML ids. See: " + attrValue, this.loc);
            throw new SAXException(spe);
         }

         this.uuidMap.put(attrValue, mappedObject);
      }

      protected void processAttrXmiIdref(String attrValue) throws SAXException {
         Object o = this.idMap.get(attrValue);
         if (o == null) {
            TemporaryObjectReplacer tor = new TemporaryObjectReplacer();
            this.idMap.put(attrValue, tor);
            this.objectsStack.push(tor);
         } else {
            this.objectsStack.push(o);
         }

      }

      protected void processAttrXmiUuidref(String attrValue) throws SAXException {
         Object o = this.uuidMap.get(attrValue);
         if (o == null) {
            TemporaryObjectReplacer tor = new TemporaryObjectReplacer();
            this.uuidMap.put(attrValue, tor);
            this.objectsStack.push(tor);
         } else {
            this.objectsStack.push(o);
         }

      }

      protected void processAttrHref(String attrValue) throws SAXException {
         int breakIndex = attrValue.indexOf("|");
         String xlink = attrValue.substring(0, breakIndex);
         xlink = XMIUtilities.resolveRelativePath(XMIDecoder.this.baseDirectory, XMIDecoder.this.filename, XMIUtilities.toURI(xlink));
         String xpointer = attrValue.substring(breakIndex + 1);
         HashMap tempId = null;
         boolean id = true;
         if (xpointer.indexOf("descendant(1,#element,xmi.uuid,") > -1) {
            xpointer = xpointer.substring(xpointer.lastIndexOf(",") + 1, xpointer.length() - 1);
            tempId = (HashMap)XMIDecoder.this.sessionUuidMaps.get(xlink);
            id = false;
         } else {
            tempId = (HashMap)XMIDecoder.this.sessionIdMaps.get(xlink);
         }

         TemporaryObjectReplacer tor;
         if (tempId == null) {
            XMIDecoder.this.filesToParse.add(xlink);
            HashMap ids = new HashMap();
            tor = new TemporaryObjectReplacer();
            ids.put(xpointer, tor);
            if (id) {
               XMIDecoder.this.sessionIdMaps.put(xlink, ids);
            } else {
               XMIDecoder.this.sessionUuidMaps.put(xlink, ids);
            }

            this.objectsQueueSS.push(tor);
         } else {
            Object o = tempId.get(xpointer);
            if (o == null) {
               tor = new TemporaryObjectReplacer();
               tempId.put(xpointer, tor);
               this.objectsQueueSS.push(tor);
            } else if (o instanceof TemporaryObjectReplacer) {
               this.objectsQueueSS.push(o);
            } else {
               this.objectsQueueSS.push(((XMLMappedObject)o).getMappedObject());
            }
         }

      }

      protected void setAttributes(XMLMappedObject mappedObject, Attributes xmlAttributes) throws SAXException {
         XMLClassMapping mappedClass = mappedObject.getMappedClass();

         for(int i = 0; i < xmlAttributes.getLength(); ++i) {
            String aQname = xmlAttributes.getQName(i);
            XMLFieldMapping mappedField = mappedClass.get(aQname);
            if (mappedField != null) {
               String value = xmlAttributes.getValue(i);
               Object[] params = this.buildParams(mappedField, value);

               try {
                  Object object = mappedObject.getMappedObject();
                  mappedField.setMappedFieldValue(object, params);
               } catch (Exception var11) {
                  SAXParseException spe = new SAXParseException("Exception while setting field " + xmlAttributes.getQName(i) + " with value " + xmlAttributes.getValue(i), this.loc);
                  throw new SAXException(spe);
               }
            }
         }

      }

      protected void setLink(XMLMappedObject mappedObject, XMLFieldMapping mappedField, Object[] params) throws SAXException {
         try {
            Object object = mappedObject.getMappedObject();
            mappedField.setMappedFieldValue(object, params);
         } catch (Exception var6) {
            SAXParseException spe = new SAXParseException("Exception while setting field " + mappedField.getXmlName(), this.loc);
            throw new SAXException(spe);
         }
      }

      protected Object[] buildParams(XMLFieldMapping mappedField, String value) {
         Class type = mappedField.getMappedFieldType();
         String typeName = type.getName();
         String fieldName = mappedField.getXmlName();
         Object[] params = new Object[1];
         String temp;
         if (type.isPrimitive()) {
            if ("int".equals(typeName)) {
               if ("*".equals(value) && fieldName.endsWith("upper")) {
                  params[0] = new Integer(-1);
                  return params;
               }

               if ("*".equals(value) && fieldName.endsWith("lower")) {
                  params[0] = new Integer(0);
                  return params;
               }

               boolean var7 = true;

               int x;
               try {
                  x = Integer.parseInt(value);
               } catch (NumberFormatException var12) {
                  if (!fieldName.equals("visibility") && !fieldName.equals("changeability") && !fieldName.equals("aggregation") && !fieldName.equals("ordering") && !fieldName.endsWith("Scope") && !fieldName.equals("concurrency") && !fieldName.equals("kind")) {
                     temp = this.xmlMapping.getConstant(value);
                     if (temp != null) {
                        x = Integer.parseInt(temp);
                     } else {
                        SAXParseException spe = new SAXParseException("Unknown symbolic name: " + value, this.loc);
                        XMIDecoder.this.simulateException(new XMIWarningException(spe));
                        x = -1;
                     }
                  } else {
                     x = XMIDecoder.this.stringToIntKind(mappedField, value);
                  }
               }

               params[0] = new Integer(x);
            } else if ("boolean".equals(typeName)) {
               params[0] = new Boolean(value);
            } else if ("long".equals(typeName)) {
               params[0] = new Long(value);
            } else if ("float".equals(typeName)) {
               params[0] = new Float(value);
            } else if ("double".equals(typeName)) {
               params[0] = new Double(value);
            } else if ("char".equals(typeName)) {
               params[0] = new Character(value.charAt(0));
            } else if ("byte".equals(typeName)) {
               params[0] = new Byte(value);
            } else if ("short".equals(typeName)) {
               params[0] = new Short(value);
            }
         } else if ("java.lang.String".equals(typeName)) {
            params[0] = value;
         } else if ("java.math.BigInteger".equals(typeName)) {
            if ("*".equals(value) && fieldName.endsWith("upper")) {
               params[0] = new BigInteger("-1");
               return params;
            }

            if ("*".equals(value) && fieldName.endsWith("lower")) {
               params[0] = new BigInteger("0");
               return params;
            }

            params[0] = new BigInteger(value);
         } else {
            StringTokenizer st = new StringTokenizer(value, " ");
            int i = 0;
            params = new Object[st.countTokens()];

            while(st.hasMoreTokens()) {
               temp = st.nextToken();
               Object o = this.idMap.get(temp);
               if (o != null) {
                  if (o instanceof XMLMappedObject) {
                     params[i++] = ((XMLMappedObject)o).getMappedObject();
                  } else {
                     params[i++] = o;
                  }
               } else {
                  TemporaryObjectReplacer tor = new TemporaryObjectReplacer();
                  this.idMap.put(temp, tor);
                  params[i++] = tor;
               }
            }
         }

         return params;
      }

      private void detectXMITempHash(String qname, Attributes attributes) throws SAXException {
         String oldQname = qname;
     //    System.out.println("      > Detecting XML export method and version ...");
         if ("GXML".equals(qname)) {
            qname = "XMI";
            this.mname = "GXAPI";
            this.mver = "0.1";
         }

         Hashtable ht = (Hashtable)XMIDecoder.this.supportedMappings.get(qname);
         if (ht == null) {
            SAXParseException spex = new SAXParseException("Unknown document element: " + qname, this.loc);
            throw new SAXException(spex);
         } else {
      //      System.out.println("        >> export method: " + qname);
            String versAttr = attributes.getValue(qname.toLowerCase() + "." + "version");
            if ("GXML".equals(oldQname)) {
               versAttr = "1.1";
            }

            if (versAttr == null) {
               SAXParseException spexx = new SAXParseException("Attribute specifying " + qname + " version missing", this.loc);
               throw new SAXException(spexx);
            } else {
               String tempVersAttr = versAttr;
               if ("1.2".equals(versAttr)) {
                  tempVersAttr = "1.1";
                  this.mname = "UML";
                  this.mver = "1.4";
               }

               this.tempHash = (Hashtable)ht.get(tempVersAttr);
               if (this.tempHash == null) {
                  SAXParseException spe = new SAXParseException("Unknown " + qname + " version", this.loc);
                  throw new SAXException(spe);
               } else {
                  if ("GXML".equals(oldQname)) {
                     this.detectXMIMapping();
                  }

         //         System.out.println("        >> version: " + versAttr);
               }
            }
         }
      }

      private void detectXMIMapping() throws SAXException {
    //     System.out.println("      > Detecting XML mapped metamodel ...");
     //    System.out.print("        >> metamodel name: ");
         if (this.mname == null) {
            this.mname = "UML";
    //        System.out.println(this.mname + " [accepted default]");
         } else {
            System.out.println(this.mname);
         }

     //    System.out.print("        >> metamodel version: ");
         if (this.mver == null) {
            this.mver = "1.3";
      //      System.out.println(this.mver + " [accepted default]");
         } else {
            System.out.println(this.mver);
         }

         String metanamever = this.mname + this.mver;
         this.xmlMapping = (XMLMapping)this.tempHash.get(metanamever);
         if (this.xmlMapping == null) {
            SAXParseException spe = new SAXParseException("Unknown " + metanamever + " metamodel", this.loc);
            throw new SAXException(spe);
         } else {
            this.tempHash = null;
         }
      }
   }
}
