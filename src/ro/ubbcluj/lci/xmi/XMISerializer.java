package ro.ubbcluj.lci.xmi;

import java.beans.ExceptionListener;

public class XMISerializer {
   private static XMISerializer theInstance;
   private XMIDecoder decoder = new XMIDecoder();
   private XMIEncoder encoder;

   protected XMISerializer() {
      this.decoder.parseConfiguration("metamodel/XMLConfigurationImport.xml");
      this.encoder = new XMIEncoder();
      this.encoder.parseConfiguration("metamodel/XMLConfigurationExport.xml");
   }

   public static XMISerializer getInstance() {
      if (theInstance == null) {
         theInstance = new XMISerializer();
      }

      return theInstance;
   }

   public void setExceptionListener(ExceptionListener el) {
      this.decoder.setExceptionListener(el);
      this.encoder.setExceptionListener(el);
   }

   public XMIDecoder getDecoder() {
      return this.decoder;
   }

   public XMIEncoder getEncoder() {
      return this.encoder;
   }
}
