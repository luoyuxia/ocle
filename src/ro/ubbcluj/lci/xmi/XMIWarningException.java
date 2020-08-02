package ro.ubbcluj.lci.xmi;

import ro.ubbcluj.lci.gui.tools.Warning;

public class XMIWarningException extends XMIException implements Warning {
   public XMIWarningException() {
   }

   public XMIWarningException(String message) {
      super(message);
   }

   public XMIWarningException(String message, Throwable cause) {
      super(message, cause);
   }

   public XMIWarningException(Throwable cause) {
      super(cause);
   }
}
