package ro.ubbcluj.lci.uml.foundation.core;

import java.util.Set;

public interface Dependency extends Relationship {
   java.util.Enumeration getSupplierList();

   Set getCollectionSupplierList();

   void addSupplier(ModelElement var1);

   void removeSupplier(ModelElement var1);

   java.util.Enumeration getClientList();

   Set getCollectionClientList();

   void addClient(ModelElement var1);

   void removeClient(ModelElement var1);
}
