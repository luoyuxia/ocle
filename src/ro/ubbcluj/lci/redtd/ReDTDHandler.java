package ro.ubbcluj.lci.redtd;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DeclHandler;
import ro.ubbcluj.lci.redtd.dtdmetamodel.DTDAttribute;
import ro.ubbcluj.lci.redtd.dtdmetamodel.DTDElement;

public class ReDTDHandler implements DeclHandler, ErrorHandler, ContentHandler {
   private DTDObjectModelParser dtdParser = new DTDObjectModelParser();
   private DTDToUMLTransformer dtdTransformer = new DTDToUMLTransformer();

   public ReDTDHandler() {
   }

   public void attributeDecl(String elementName, String attributeName, String type, String valueDefault, String value) throws SAXException {
      DTDAttribute attr = this.dtdParser.parseDTDAttribute(elementName, attributeName, type, valueDefault, value);
      this.dtdTransformer.transformAttribute(attr);
   }

   public void elementDecl(String name, String model) throws SAXException {
      DTDElement element = this.dtdParser.parseDTDElement(name, model);
      this.dtdTransformer.transformElement(element);
   }

   public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException {
   }

   public void internalEntityDecl(String name, String value) throws SAXException {
   }

   public void error(SAXParseException exception) throws SAXException {
   }

   public void fatalError(SAXParseException exception) throws SAXException {
   }

   public void warning(SAXParseException exception) throws SAXException {
   }

   public void endDocument() throws SAXException {
      this.dtdTransformer.resolveAnyContent();
   }

   public void startDocument() throws SAXException {
   }

   public void characters(char[] ch, int start, int length) throws SAXException {
   }

   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
   }

   public void endPrefixMapping(String prefix) throws SAXException {
   }

   public void skippedEntity(String name) throws SAXException {
   }

   public void setDocumentLocator(Locator locator) {
   }

   public void processingInstruction(String target, String data) throws SAXException {
   }

   public void startPrefixMapping(String prefix, String uri) throws SAXException {
   }

   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
   }

   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
   }
}
