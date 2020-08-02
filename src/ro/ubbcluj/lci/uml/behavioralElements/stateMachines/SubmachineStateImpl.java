package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class SubmachineStateImpl extends CompositeStateImpl implements SubmachineState {
   protected StateMachine theSubmachine;

   public SubmachineStateImpl() {
   }

   public StateMachine getSubmachine() {
      return this.theSubmachine;
   }

   public void setSubmachine(StateMachine arg) {
      if (this.theSubmachine != arg) {
         StateMachine temp = this.theSubmachine;
         this.theSubmachine = null;
         if (temp != null) {
            temp.removeSubmachineState(this);
         }

         if (arg != null) {
            this.theSubmachine = arg;
            arg.addSubmachineState(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "submachine", 0));
         }
      }

   }

   protected void internalRemove() {
      StateMachine tmpSubmachine = this.getSubmachine();
      if (tmpSubmachine != null) {
         tmpSubmachine.removeSubmachineState(this);
      }

      super.internalRemove();
   }
}
