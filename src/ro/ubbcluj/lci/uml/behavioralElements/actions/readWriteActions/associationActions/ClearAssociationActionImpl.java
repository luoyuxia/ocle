package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveActionImpl;
import ro.ubbcluj.lci.uml.foundation.core.Association;

public class ClearAssociationActionImpl extends PrimitiveActionImpl implements ClearAssociationAction {
   protected Association theAssociation;

   public ClearAssociationActionImpl() {
   }

   public Association getAssociation() {
      return this.theAssociation;
   }

   public void setAssociation(Association arg) {
      this.theAssociation = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "association", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
