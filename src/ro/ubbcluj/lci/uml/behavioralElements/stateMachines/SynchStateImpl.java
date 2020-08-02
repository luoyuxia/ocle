package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import java.math.BigInteger;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class SynchStateImpl extends StateVertexImpl implements SynchState {
   protected BigInteger theBound;

   public SynchStateImpl() {
   }

   public BigInteger getBound() {
      return this.theBound;
   }

   public void setBound(BigInteger bound) {
      this.theBound = bound;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "bound", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
