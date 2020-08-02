package ro.ubbcluj.lci.uml.foundation.core;

import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.ClassifierRole;

public interface Feature extends ModelElement {
   int getOwnerScope();

   void setOwnerScope(int var1);

   int getVisibility();

   void setVisibility(int var1);

   java.util.Enumeration getClassifierRoleList();

   Set getCollectionClassifierRoleList();

   void addClassifierRole(ClassifierRole var1);

   void removeClassifierRole(ClassifierRole var1);

   Classifier getOwner();

   void setOwner(Classifier var1);
}
