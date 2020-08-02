package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveActionImpl;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;

public class ReadLinkObjectQualifierActionImpl extends PrimitiveActionImpl implements ReadLinkObjectQualifierAction {
   protected Attribute theQualifier;

   public ReadLinkObjectQualifierActionImpl() {
   }

   public Attribute getQualifier() {
      return this.theQualifier;
   }

   public void setQualifier(Attribute arg) {
      this.theQualifier = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "qualifier", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
