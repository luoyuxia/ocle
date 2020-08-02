package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.dataTypes.BooleanExpression;

public class ChangeEventImpl extends EventImpl implements ChangeEvent {
   protected BooleanExpression theChangeExpression;

   public ChangeEventImpl() {
   }

   public BooleanExpression getChangeExpression() {
      return this.theChangeExpression;
   }

   public void setChangeExpression(BooleanExpression changeExpression) {
      this.theChangeExpression = changeExpression;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "changeExpression", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
