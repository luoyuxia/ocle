package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class StubStateImpl extends StateVertexImpl implements StubState {
   protected String theReferenceState;

   public StubStateImpl() {
   }

   public String getReferenceState() {
      return this.theReferenceState;
   }

   public void setReferenceState(String referenceState) {
      this.theReferenceState = referenceState;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "referenceState", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
