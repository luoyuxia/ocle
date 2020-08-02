package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions;

import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveAction;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;

public interface ReadLinkObjectQualifierAction extends PrimitiveAction {
   Attribute getQualifier();

   void setQualifier(Attribute var1);
}
