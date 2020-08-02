package ro.ubbcluj.lci.xmi;

import ro.ubbcluj.lci.gui.tools.Error;

public class XMIErrorException extends XMIException implements Error {
   public XMIErrorException() {
   }

   public XMIErrorException(String message) {
      super(message);
   }

   public XMIErrorException(String message, Throwable cause) {
      super(message, cause);
   }

   public XMIErrorException(Throwable cause) {
      super(cause);
   }
}
