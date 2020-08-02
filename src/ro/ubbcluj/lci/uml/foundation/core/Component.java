package ro.ubbcluj.lci.uml.foundation.core;

import java.util.Set;

public interface Component extends Classifier {
   java.util.Enumeration getResidentList();

   Set getCollectionResidentList();

   java.util.Enumeration directGetResidentList();

   Set directGetCollectionResidentList();

   void addResident(ElementResidence var1);

   void removeResident(ElementResidence var1);

   java.util.Enumeration getDeploymentLocationList();

   Set getCollectionDeploymentLocationList();

   void addDeploymentLocation(Node var1);

   void removeDeploymentLocation(Node var1);

   java.util.Enumeration getImplementationList();

   Set getCollectionImplementationList();

   void addImplementation(Artifact var1);

   void removeImplementation(Artifact var1);

   Set allResidentElements();
}
