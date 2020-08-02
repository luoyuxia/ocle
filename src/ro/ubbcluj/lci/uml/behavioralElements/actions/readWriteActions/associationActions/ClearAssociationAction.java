package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions;

import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveAction;
import ro.ubbcluj.lci.uml.foundation.core.Association;

public interface ClearAssociationAction extends PrimitiveAction {
   Association getAssociation();

   void setAssociation(Association var1);
}
