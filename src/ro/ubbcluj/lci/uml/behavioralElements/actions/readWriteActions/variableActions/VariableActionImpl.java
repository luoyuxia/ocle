package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.variableActions;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions.Variable;

public class VariableActionImpl extends PrimitiveActionImpl implements VariableAction {
   protected Variable theVariable;

   public VariableActionImpl() {
   }

   public Variable getVariable() {
      return this.theVariable;
   }

   public void setVariable(Variable arg) {
      this.theVariable = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "variable", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
