package ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveActionImpl;

public class ApplyFunctionActionImpl extends PrimitiveActionImpl implements ApplyFunctionAction {
   protected PrimitiveFunction theFunction;

   public ApplyFunctionActionImpl() {
   }

   public PrimitiveFunction getFunction() {
      return this.theFunction;
   }

   public void setFunction(PrimitiveFunction arg) {
      this.theFunction = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "function", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
