package ro.ubbcluj.lci.uml.foundation.core;

import java.util.Set;

public interface Node extends Classifier {
   java.util.Enumeration getDeployedComponentList();

   Set getCollectionDeployedComponentList();

   void addDeployedComponent(Component var1);

   void removeDeployedComponent(Component var1);
}
