package ro.ubbcluj.lci.uml.foundation.core;

import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;

public interface Enumeration extends DataType {
   java.util.Enumeration getLiteralList();

   OrderedSet getCollectionLiteralList();

   void addLiteral(EnumerationLiteral var1);

   void removeLiteral(EnumerationLiteral var1);
}
