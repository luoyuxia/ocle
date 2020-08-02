package ro.ubbcluj.lci.uml.behavioralElements.useCases;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.foundation.core.Relationship;
import ro.ubbcluj.lci.uml.foundation.dataTypes.BooleanExpression;

public interface Extend extends Relationship {
   BooleanExpression getCondition();

   void setCondition(BooleanExpression var1);

   UseCase getBase();

   void setBase(UseCase var1);

   Enumeration getExtensionPointList();

   Set getCollectionExtensionPointList();

   void addExtensionPoint(ExtensionPoint var1);

   void removeExtensionPoint(ExtensionPoint var1);

   UseCase getExtension();

   void setExtension(UseCase var1);
}
