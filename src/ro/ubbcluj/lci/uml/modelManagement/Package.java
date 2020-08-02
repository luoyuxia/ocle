package ro.ubbcluj.lci.uml.modelManagement;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.foundation.core.Dependency;
import ro.ubbcluj.lci.uml.foundation.core.GeneralizableElement;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;

public interface Package extends GeneralizableElement, Namespace {
   Enumeration getImportedElementList();

   Set getCollectionImportedElementList();

   Enumeration directGetImportedElementList();

   Set directGetCollectionImportedElementList();

   void addImportedElement(ElementImport var1);

   void removeImportedElement(ElementImport var1);

   Set contents();

   Set allContents();

   Set clDepSuplElem(Dependency var1);

   Set allVisibleElements();

   Set allImportedElements();
}
