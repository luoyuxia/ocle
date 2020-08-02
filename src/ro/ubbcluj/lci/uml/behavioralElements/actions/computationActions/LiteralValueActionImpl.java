package ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.DataValue;

public class LiteralValueActionImpl extends PrimitiveActionImpl implements LiteralValueAction {
   protected DataValue theValue;

   public LiteralValueActionImpl() {
   }

   public DataValue getValue() {
      return this.theValue;
   }

   public void setValue(DataValue arg) {
      this.theValue = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "value", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
