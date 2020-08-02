package ro.ubbcluj.lci.uml.foundation.core;

import ro.ubbcluj.lci.uml.modelManagement.Model;

public interface Element {
   Model getOwnerModel();

   void setOwnerModel(Model var1);

   void remove();

   String getMetaclassName();
}
