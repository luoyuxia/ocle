package ro.ubbcluj.lci.uml.foundation.core;

import java.util.Set;

public interface Artifact extends Classifier {
   java.util.Enumeration getImplementationLocationList();

   Set getCollectionImplementationLocationList();

   void addImplementationLocation(Component var1);

   void removeImplementationLocation(Component var1);
}
