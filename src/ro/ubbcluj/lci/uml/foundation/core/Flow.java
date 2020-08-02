package ro.ubbcluj.lci.uml.foundation.core;

import java.util.Set;

public interface Flow extends Relationship {
   java.util.Enumeration getSourceList();

   Set getCollectionSourceList();

   void addSource(ModelElement var1);

   void removeSource(ModelElement var1);

   java.util.Enumeration getTargetList();

   Set getCollectionTargetList();

   void addTarget(ModelElement var1);

   void removeTarget(ModelElement var1);
}
