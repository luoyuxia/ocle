package ro.ubbcluj.lci.uml.behavioralElements.useCases;

import ro.ubbcluj.lci.uml.foundation.core.Relationship;

public interface Include extends Relationship {
   UseCase getAddition();

   void setAddition(UseCase var1);

   UseCase getBase();

   void setBase(UseCase var1);
}
