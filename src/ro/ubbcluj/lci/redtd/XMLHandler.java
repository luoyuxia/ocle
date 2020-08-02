package ro.ubbcluj.lci.redtd;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler {
   private XMLObjectModelParser xmlParser = new XMLObjectModelParser();
   private StringBuffer tempBuffer = new StringBuffer();

   public XMLHandler() {
   }

   public void endDocument() throws SAXException {
   }

   public void startDocument() throws SAXException {
      this.xmlParser.parseXMLDocument();
   }

   public void characters(char[] ch, int start, int length) throws SAXException {
      String data = new String(ch, start, length);
      data = data.replaceAll("\\n", "");
      data = data.replaceAll("\\r", "");
      if (!data.equals("")) {
         this.tempBuffer.append(data.trim());
      }
   }

   public void endElement(String uri, String localName, String qName) throws SAXException {
      if (this.tempBuffer.length() > 0) {
         this.xmlParser.parseCharacterData(this.tempBuffer.toString());
         this.tempBuffer = new StringBuffer();
      }

      this.xmlParser.parseClosingXMLElement(qName);
   }

   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      if (this.tempBuffer.length() > 0) {
         this.xmlParser.parseCharacterData(this.tempBuffer.toString());
         this.tempBuffer = new StringBuffer();
      }

      this.xmlParser.parseStartingXMLElement(qName, attributes);
   }

   public void fatalError(SAXParseException e) throws SAXException {
      if (e.getMessage().startsWith("Relative URI")) {
         System.out.println("The DTD file is specified in XML file !DOCTYPE declaration with a relative URI. Please provide an absolute \nURI in order to locate this file or comment this declaration if no external or global entities are defined in the DTD file.\n");
      }

   }
}
