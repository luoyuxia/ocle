package ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveActionImpl;
import ro.ubbcluj.lci.uml.foundation.core.Class;

public class UnmarshalActionImpl extends PrimitiveActionImpl implements UnmarshalAction {
   protected Class theUnmarshalType;

   public UnmarshalActionImpl() {
   }

   public Class getUnmarshalType() {
      return this.theUnmarshalType;
   }

   public void setUnmarshalType(Class arg) {
      this.theUnmarshalType = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "unmarshalType", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
