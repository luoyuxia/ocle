package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions;

import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.Element;

public interface QualifierValue extends Element {
   Attribute getQualifier();

   void setQualifier(Attribute var1);
}
