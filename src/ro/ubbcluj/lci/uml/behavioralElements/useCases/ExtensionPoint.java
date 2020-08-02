package ro.ubbcluj.lci.uml.behavioralElements.useCases;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.dataTypes.LocationReference;

public interface ExtensionPoint extends ModelElement {
   LocationReference getLocation();

   void setLocation(LocationReference var1);

   UseCase getUseCase();

   void setUseCase(UseCase var1);

   Enumeration getExtendList();

   Set getCollectionExtendList();

   void addExtend(Extend var1);

   void removeExtend(Extend var1);
}
