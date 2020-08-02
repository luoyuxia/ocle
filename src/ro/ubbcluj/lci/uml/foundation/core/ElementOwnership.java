package ro.ubbcluj.lci.uml.foundation.core;

public interface ElementOwnership extends Element {
   int getVisibility();

   void setVisibility(int var1);

   boolean isSpecification();

   void setSpecification(boolean var1);

   ModelElement getOwnedElement();

   void setOwnedElement(ModelElement var1);

   Namespace getNamespace();

   void setNamespace(Namespace var1);
}
