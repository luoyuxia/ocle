package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.attributeActions;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveActionImpl;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;

public class AttributeActionImpl extends PrimitiveActionImpl implements AttributeAction {
   protected Attribute theAttribute;

   public AttributeActionImpl() {
   }

   public Attribute getAttribute() {
      return this.theAttribute;
   }

   public void setAttribute(Attribute arg) {
      this.theAttribute = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "attribute", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
