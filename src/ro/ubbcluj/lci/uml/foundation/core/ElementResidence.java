package ro.ubbcluj.lci.uml.foundation.core;

public interface ElementResidence extends Element {
   int getVisibility();

   void setVisibility(int var1);

   ModelElement getResident();

   void setResident(ModelElement var1);

   Component getContainer();

   void setContainer(Component var1);
}
