package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.attributeActions;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class AddAttributeValueActionImpl extends WriteAttributeActionImpl implements AddAttributeValueAction {
   protected boolean isReplaceAll;

   public AddAttributeValueActionImpl() {
   }

   public boolean isReplaceAll() {
      return this.isReplaceAll;
   }

   public void setReplaceAll(boolean isReplaceAll) {
      this.isReplaceAll = isReplaceAll;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isReplaceAll", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
