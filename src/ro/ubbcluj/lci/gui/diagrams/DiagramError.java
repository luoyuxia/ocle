package ro.ubbcluj.lci.gui.diagrams;

import ro.ubbcluj.lci.gui.tools.Error;

public class DiagramError implements Error {
   private static String msg;

   public DiagramError(String errorMsg) {
      msg = errorMsg;
   }

   public String toString() {
      return msg;
   }
}
