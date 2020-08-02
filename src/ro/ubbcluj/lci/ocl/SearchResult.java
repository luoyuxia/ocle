package ro.ubbcluj.lci.ocl;

import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.Operation;

public class SearchResult {
   public OclType type = null;
   public int foundType = -1;
   public OclClassInfo foundOwner = null;
   public OclLetItem foundLetItem = null;
   public Operation operation = null;
   public Attribute attr = null;
   public AssociationEnd asend = null;
   public OclLetItem foundImplicitOwner = null;
   public OclLetItem declarator1 = null;
   public OclLetItem declarator2 = null;
   public OclLetItem accumulator = null;

   public SearchResult() {
   }

   public SearchResult(SearchResult arg) {
      if (arg != null) {
         this.type = arg.type;
         this.foundType = arg.foundType;
         this.foundOwner = arg.foundOwner;
         this.foundLetItem = arg.foundLetItem;
         this.foundImplicitOwner = arg.foundImplicitOwner;
         this.declarator1 = arg.declarator1;
         this.declarator2 = arg.declarator2;
         this.accumulator = arg.accumulator;
         this.attr = arg.attr;
         this.asend = arg.asend;
         this.operation = arg.operation;
      }
   }

   SearchResult(OclType type, int foundType) {
      this.type = type;
      this.foundType = foundType;
   }

   SearchResult(OclType type, int foundType, OclClassInfo foundOwner, OclLetItem foundLetItem) {
      this.type = type;
      this.foundType = foundType;
      this.foundOwner = foundOwner;
      this.foundLetItem = foundLetItem;
   }
}
