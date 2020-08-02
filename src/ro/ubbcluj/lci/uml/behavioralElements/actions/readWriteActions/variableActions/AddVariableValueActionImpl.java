package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.variableActions;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class AddVariableValueActionImpl extends WriteVaribleActionImpl implements AddVariableValueAction {
   protected boolean isReplaceAll;

   public AddVariableValueActionImpl() {
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
