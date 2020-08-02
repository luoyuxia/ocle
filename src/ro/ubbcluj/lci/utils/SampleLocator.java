package ro.ubbcluj.lci.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ro.ubbcluj.lci.gui.Actions.CompilerInvoker;
import ro.ubbcluj.lci.gui.mainframe.GUMLModel;
import ro.ubbcluj.lci.ocl.OclCompiler;
import ro.ubbcluj.lci.ocl.OclNode;
import ro.ubbcluj.lci.ocl.nodes.name;
import ro.ubbcluj.lci.ocl.nodes.stereotype;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Operation;

public class SampleLocator extends DefaultHandler {
   private static SampleLocator locator;
   private GUMLModel sampleModel;
   private OclNode compiledForm;
   private HashMap locationCache = new HashMap();
   private HashMap nameMap = null;
   private File exampleDir = null;
   private String operationOwner;
   private StringBuffer currentData = null;
   private String currentOperation;

   public static SampleLocator getLocator() {
      if (locator == null) {
         locator = new SampleLocator();
      }

      return locator;
   }

   public GUMLModel getSampleModel() {
      if (this.sampleModel == null) {
         this.loadSampleModel();
      }

      return this.sampleModel;
   }

   public OclNode getLocation(Operation op) throws Exception {
      if (this.compiledForm == null) {
         this.compileSamples();
      }

      if (this.nameMap == null) {
         this.parseNameMapFile();
      }

      String stName = this.getOCLStereotypeName(op);
      OclNode result = (OclNode)this.locationCache.get(stName);
      if (result == null) {
         result = this.getLocationImpl(stName);
         this.locationCache.put(stName, result);
      }

      return result;
   }

   public void startElement(String namespaceURI, String simpleName, String qualifiedName, Attributes attrs) throws SAXException {
      if (this.currentData != null) {
         this.currentData = null;
      }

   }

   public void endElement(String namespaceURI, String simpleName, String qualifiedName) throws SAXException {
      String elementName = simpleName;
      if (simpleName == null || "".equals(simpleName)) {
         elementName = qualifiedName;
      }

      if ("operation-owner-name".equals(elementName)) {
         this.operationOwner = this.currentData.toString();
      } else if ("operation-name".equals(elementName)) {
         this.currentOperation = this.currentData.toString();
      } else if ("stereotype-name".equals(elementName)) {
         String key = this.operationOwner + this.currentOperation;
         this.nameMap.put(key, this.currentData.toString());
      }

   }

   public void characters(char[] buffer, int offset, int length) throws SAXException {
      String data = new String(buffer, offset, length);
      if (this.currentData == null) {
         this.currentData = new StringBuffer(data);
      } else {
         this.currentData.append(data);
      }

   }

   private SampleLocator() {
      URL start = (Integer.class).getResource("/.");
      File f1 = new File(start.getFile());
      f1 = f1.getParentFile();
      this.exampleDir = new File(f1, "help");
   }

   private void parseNameMapFile() throws Exception {
      File fileToParse = new File(this.exampleDir, "examples.xml");

      try {
         this.nameMap = new HashMap();
         SAXParserFactory parserFactory = SAXParserFactory.newInstance();
         SAXParser parser = parserFactory.newSAXParser();
         parser.parse(fileToParse, this);
      } catch (SAXException var4) {
         this.nameMap = null;
         throw new Exception("Exception while parsing file:" + var4.getMessage(), var4);
      } catch (ParserConfigurationException var5) {
         this.nameMap = null;
         throw new Exception("Could not create parser:" + var5.getMessage(), var5);
      } catch (IOException var6) {
         this.nameMap = null;
         throw new Exception("Could not read configuration file:" + var6.getMessage(), var6);
      }
   }

   private void compileSamples() throws Exception {
      OclCompiler comp = new OclCompiler(this.getSampleModel().getModel(), false);
      File[] sampleFiles = this.exampleDir.listFiles();
      ArrayList fileNames = new ArrayList();

      for(int i = 0; i < sampleFiles.length; ++i) {
         String fileName = sampleFiles[i].getAbsolutePath();
         if (fileName.toLowerCase().endsWith(".bcr")) {
            fileNames.add(sampleFiles[i].getAbsolutePath());
         }
      }

      try {
         String[] files = (String[])fileNames.toArray(new String[0]);
         char[][] compileData = CompilerInvoker.getCompileData(files);
         comp.compile(compileData, files);
      } catch (IOException var9) {
         throw new Exception("Cannot read sample file(s)");
      } finally {
         this.compiledForm = comp.getRoot();
      }

   }

   private String getOCLStereotypeName(Operation selectedOperation) {
      ModelElement owner = selectedOperation.getOwner();
      String key = owner.getName() + selectedOperation.getName();
      return (String)this.nameMap.get(key);
   }

   private OclNode getLocationImpl(String stereotypeName) {
      Stack nodes = new Stack();
      nodes.push(this.compiledForm);
      OclNode result = null;

      while(!nodes.isEmpty() && result == null) {
         OclNode node = (OclNode)nodes.pop();
         OclNode parentNode = node.getParent();
         int cc;
         if (node instanceof stereotype || node.getValueAsString().equals("def")) {
            cc = parentNode.indexOfChild(node);
            OclNode possibleNameNode = parentNode.getChild(cc + 1);
            if (possibleNameNode instanceof name && possibleNameNode.getValueAsString().equals(stereotypeName)) {
               result = node;
            }
         }

         cc = node.getChildCount() - 1;

         for(int i = cc; i >= 0; --i) {
            nodes.push(node.getChild(i));
         }
      }

      return result;
   }

   private void loadSampleModel() {
      File modelFile = new File(this.exampleDir, "example.xml.zip");
      this.sampleModel = new GUMLModel(modelFile, true);
   }
}
