package ro.ubbcluj.lci.ocl;

import java.util.Enumeration;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Operation;

class SFindInfo {
   String name;
   Enumeration params;
   int foundType;
   boolean isCollection;
   boolean isBoolean;
   boolean is01;
   Classifier owner;
   Attribute theAttribute;
   AssociationEnd theAssociationEnd;
   OclClassInfo collectionType;
   OclClassInfo elementType;
   OclType attrType;
   Operation theOperation;

   SFindInfo() {
   }

   void clear() {
      this.name = null;
      this.params = null;
      this.foundType = 0;
      this.isCollection = false;
      this.isBoolean = false;
      this.is01 = false;
      this.owner = null;
      this.theAttribute = null;
      this.theAssociationEnd = null;
      this.collectionType = null;
      this.elementType = null;
      this.attrType = null;
      this.theOperation = null;
   }
}
