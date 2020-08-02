package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions;

import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveAction;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;

public interface ReadLinkObjectEndAction extends PrimitiveAction {
   AssociationEnd getEnd();

   void setEnd(AssociationEnd var1);
}
