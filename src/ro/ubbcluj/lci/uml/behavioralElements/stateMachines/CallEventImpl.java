package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.Operation;

public class CallEventImpl extends EventImpl implements CallEvent {
   protected Operation theOperation;

   public CallEventImpl() {
   }

   public Operation getOperation() {
      return this.theOperation;
   }

   public void setOperation(Operation arg) {
      if (this.theOperation != arg) {
         Operation temp = this.theOperation;
         this.theOperation = null;
         if (temp != null) {
            temp.removeOccurence(this);
         }

         if (arg != null) {
            this.theOperation = arg;
            arg.addOccurence(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "operation", 0));
         }
      }

   }

   protected void internalRemove() {
      Operation tmpOperation = this.getOperation();
      if (tmpOperation != null) {
         tmpOperation.removeOccurence(this);
      }

      super.internalRemove();
   }
}
