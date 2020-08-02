package ro.ubbcluj.lci.uml.foundation.core;

public interface Generalization extends Relationship {
   String getDiscriminator();

   void setDiscriminator(String var1);

   GeneralizableElement getChild();

   void setChild(GeneralizableElement var1);

   GeneralizableElement getParent();

   void setParent(GeneralizableElement var1);

   Classifier getPowertype();

   void setPowertype(Classifier var1);
}
