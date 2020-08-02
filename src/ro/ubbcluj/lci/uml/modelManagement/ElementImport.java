package ro.ubbcluj.lci.uml.modelManagement;

import ro.ubbcluj.lci.uml.foundation.core.Element;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public interface ElementImport extends Element {
   int getVisibility();

   void setVisibility(int var1);

   String getAlias();

   void setAlias(String var1);

   boolean isSpecification();

   void setSpecification(boolean var1);

   ModelElement getImportedElement();

   void setImportedElement(ModelElement var1);

   Package getPackage();

   void setPackage(Package var1);
}
