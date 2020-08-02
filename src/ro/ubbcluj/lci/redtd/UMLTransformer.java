package ro.ubbcluj.lci.redtd;

import java.io.File;
import ro.ubbcluj.lci.uml.modelManagement.Model;

public abstract class UMLTransformer {
   public static final int DTD_MODE = 0;
   public static final int XML_MODE = 1;

   public UMLTransformer() {
   }

   public static UMLTransformer getInstance(int transformingMode) {
      if (transformingMode == 0) {
         return new UMLToDTDTransformer();
      } else {
         return transformingMode == 1 ? new UMLToXMLTransformer() : null;
      }
   }

   public abstract void transformModel(Model var1, File var2);
}
