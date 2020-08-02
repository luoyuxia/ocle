package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveActionImpl;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;

public class ReadLinkObjectEndActionImpl extends PrimitiveActionImpl implements ReadLinkObjectEndAction {
   protected AssociationEnd theEnd;

   public ReadLinkObjectEndActionImpl() {
   }

   public AssociationEnd getEnd() {
      return this.theEnd;
   }

   public void setEnd(AssociationEnd arg) {
      this.theEnd = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "end", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
