package ro.ubbcluj.lci.xmi;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public abstract class XMIException extends Exception {
   private String publicId;
   private String systemId;
   private int linNumber = -1;
   private int colNumber = -1;

   public XMIException() {
   }

   public XMIException(String message) {
      super(message);
   }

   public XMIException(String message, Throwable cause) {
      super(message, cause);
      if (cause instanceof Exception) {
         this.extractAdditionalInformation((Exception)cause);
      }

   }

   public XMIException(Throwable cause) {
      super(cause);
      if (cause instanceof Exception) {
         this.extractAdditionalInformation((Exception)cause);
      }

   }

   private void extractAdditionalInformation(Exception e) {
      if (e instanceof SAXException) {
         Exception ie = ((SAXException)e).getException();
         if (ie instanceof SAXParseException) {
            e = ie;
         }
      }

      if (e instanceof SAXParseException) {
         SAXParseException saxpe = (SAXParseException)e;
         this.publicId = saxpe.getPublicId();
         this.systemId = saxpe.getSystemId();
         this.linNumber = saxpe.getLineNumber();
         this.colNumber = saxpe.getColumnNumber();
      }

   }

   public String getPublicId() {
      return this.publicId;
   }

   public String getSystemId() {
      return this.systemId;
   }

   public int getColNumber() {
      return this.colNumber;
   }

   public int getLinNumber() {
      return this.linNumber;
   }

   public String getMessage() {
      return this.getCause() instanceof SAXException ? this.getCause().getMessage() : super.getMessage();
   }
}
