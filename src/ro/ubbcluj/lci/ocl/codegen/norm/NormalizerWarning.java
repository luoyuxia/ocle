package ro.ubbcluj.lci.ocl.codegen.norm;

import ro.ubbcluj.lci.errors.TextLocalizableErrorMessage;
import ro.ubbcluj.lci.gui.tools.Warning;

class NormalizerWarning extends TextLocalizableErrorMessage implements Warning {
   public NormalizerWarning(String file, int addrStart, int addrStop, String description) {
      super(file);
      this.description = description;
      this.start = addrStart;
      this.stop = addrStop;
   }
}
