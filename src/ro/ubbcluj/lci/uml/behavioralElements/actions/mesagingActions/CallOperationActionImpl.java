package ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.Operation;

public class CallOperationActionImpl extends ExplicitInvocationActionImpl implements CallOperationAction {
   protected boolean isAsynchronous;
   protected Operation theOperation;

   public CallOperationActionImpl() {
   }

   public boolean isAsynchronous() {
      return this.isAsynchronous;
   }

   public void setAsynchronous(boolean isAsynchronous) {
      this.isAsynchronous = isAsynchronous;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isAsynchronous", 0));
      }

   }

   public Operation getOperation() {
      return this.theOperation;
   }

   public void setOperation(Operation arg) {
      this.theOperation = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "operation", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
