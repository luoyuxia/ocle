package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.dataTypes.TimeExpression;

public class TimeEventImpl extends EventImpl implements TimeEvent {
   protected TimeExpression theWhen;

   public TimeEventImpl() {
   }

   public TimeExpression getWhen() {
      return this.theWhen;
   }

   public void setWhen(TimeExpression when) {
      this.theWhen = when;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "when", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
