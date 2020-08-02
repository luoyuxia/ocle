package ro.ubbcluj.lci.redtd;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class DTDToUMLFacade {
   private ReDTDHandler dtdHandler = new ReDTDHandler();
   private XMLHandler xmlHandler = new XMLHandler();

   public DTDToUMLFacade() {
   }

   private InputStream getInputStreamForDTDFile(File dtdFile) throws IOException {
      String url = dtdFile.toURL().toString();
      String xmlContent = "<!DOCTYPE ROOT SYSTEM \"" + url + "\"> <ROOT></ROOT>";
      return new ByteArrayInputStream(xmlContent.getBytes());
   }

   public void reDTD(File dtdFile) {
      try {
         if (dtdFile.getName().toLowerCase().endsWith(".dtd")) {
            this.parseDTD(this.getInputStreamForDTDFile(dtdFile));
         } else {
            this.parseDTD(new FileInputStream(dtdFile));
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void importXMLObjects(File xmlFile) {
      try {
         this.parseXML(new FileInputStream(xmlFile));
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   private static SAXParser initSAXParser() {
      SAXParserFactory parserFactory = SAXParserFactory.newInstance();
      parserFactory.setNamespaceAware(true);
      parserFactory.setValidating(false);
      SAXParser parser = null;

      try {
         parser = parserFactory.newSAXParser();
      } catch (ParserConfigurationException var3) {
         var3.printStackTrace();
      } catch (SAXException var4) {
         var4.printStackTrace();
      }

      return parser;
   }

   private void parseDTD(InputStream in) {
      SAXParser saxParser = initSAXParser();

      try {
         XMLReader xmlReader = saxParser.getXMLReader();
         xmlReader.setProperty("http://xml.org/sax/properties/declaration-handler", this.dtdHandler);
         xmlReader.setErrorHandler(this.dtdHandler);
         xmlReader.setContentHandler(this.dtdHandler);
         xmlReader.parse(new InputSource(in));
      } catch (SAXException var4) {
         var4.printStackTrace();
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   private void parseXML(InputStream in) {
      SAXParser saxParser = initSAXParser();

      try {
         XMLReader xmlReader = saxParser.getXMLReader();
         xmlReader.setErrorHandler(this.xmlHandler);
         xmlReader.setContentHandler(this.xmlHandler);
         xmlReader.parse(new InputSource(in));
      } catch (SAXException var4) {
         var4.printStackTrace();
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }
}
