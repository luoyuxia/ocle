package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Signal;

public class SignalEventImpl extends EventImpl implements SignalEvent {
   protected Signal theSignal;

   public SignalEventImpl() {
   }

   public Signal getSignal() {
      return this.theSignal;
   }

   public void setSignal(Signal arg) {
      if (this.theSignal != arg) {
         Signal temp = this.theSignal;
         this.theSignal = null;
         if (temp != null) {
            temp.removeOccurence(this);
         }

         if (arg != null) {
            this.theSignal = arg;
            arg.addOccurence(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "signal", 0));
         }
      }

   }

   protected void internalRemove() {
      Signal tmpSignal = this.getSignal();
      if (tmpSignal != null) {
         tmpSignal.removeOccurence(this);
      }

      super.internalRemove();
   }
}
