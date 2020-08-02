package ro.ubbcluj.lci.uml.foundation.core;

import java.util.Set;

public interface GeneralizableElement extends ModelElement {
   boolean isRoot();

   void setRoot(boolean var1);

   boolean isLeaf();

   void setLeaf(boolean var1);

   boolean isAbstract();

   void setAbstract(boolean var1);

   java.util.Enumeration getGeneralizationList();

   Set getCollectionGeneralizationList();

   void addGeneralization(Generalization var1);

   void removeGeneralization(Generalization var1);

   java.util.Enumeration getSpecializationList();

   Set getCollectionSpecializationList();

   void addSpecialization(Generalization var1);

   void removeSpecialization(Generalization var1);

   Set parent();

   Set allParents();
}
