package ro.ubbcluj.lci.uml.foundation.core;

import java.util.Set;

public interface Namespace extends ModelElement {
   java.util.Enumeration getOwnedElementList();

   Set getCollectionOwnedElementList();

   java.util.Enumeration directGetOwnedElementList();

   Set directGetCollectionOwnedElementList();

   void addOwnedElement(ElementOwnership var1);

   void removeOwnedElement(ElementOwnership var1);

   Set contentsAndImports();

   Set contentsAndImportsD();

   Set contents();

   Set contentsD();

   Set allContents();

   Set allContentsD();

   Set clDepSuplElem(Dependency var1);

   Set clDepStName(Dependency var1);

   Set depElemVisibleInNamespace();

   Set allVisibleElements();

   Set allSurroundingNamespaces();
}
