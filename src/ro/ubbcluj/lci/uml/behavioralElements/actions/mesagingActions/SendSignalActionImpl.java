package ro.ubbcluj.lci.uml.behavioralElements.actions.mesagingActions;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Signal;

public class SendSignalActionImpl extends ExplicitInvocationActionImpl implements SendSignalAction {
   protected Signal theSignal;

   public SendSignalActionImpl() {
   }

   public Signal getSignal() {
      return this.theSignal;
   }

   public void setSignal(Signal arg) {
      this.theSignal = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "signal", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
