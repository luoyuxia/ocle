package ro.ubbcluj.lci.uml.behavioralElements.useCases;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;

public interface UseCase extends Classifier {
   Enumeration getExtensionPointList();

   Set getCollectionExtensionPointList();

   void addExtensionPoint(ExtensionPoint var1);

   void removeExtensionPoint(ExtensionPoint var1);

   Enumeration getExtendList();

   Set getCollectionExtendList();

   void addExtend(Extend var1);

   void removeExtend(Extend var1);

   Enumeration getExtenderList();

   Set getCollectionExtenderList();

   void addExtender(Extend var1);

   void removeExtender(Extend var1);

   Enumeration getIncluderList();

   Set getCollectionIncluderList();

   void addIncluder(Include var1);

   void removeIncluder(Include var1);

   Enumeration getIncludeList();

   Set getCollectionIncludeList();

   void addInclude(Include var1);

   void removeInclude(Include var1);

   Set specificationPath();

   Set allExtensionPoints();
}
