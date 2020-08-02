package ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveActionImpl;
import ro.ubbcluj.lci.uml.foundation.core.Class;

public class MarshalActionImpl extends PrimitiveActionImpl implements MarshalAction {
   protected Class theMarshalType;

   public MarshalActionImpl() {
   }

   public Class getMarshalType() {
      return this.theMarshalType;
   }

   public void setMarshalType(Class arg) {
      this.theMarshalType = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "marshalType", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
