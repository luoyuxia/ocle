package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class PseudostateImpl extends StateVertexImpl implements Pseudostate {
   protected int theKind;

   public PseudostateImpl() {
   }

   public int getKind() {
      return this.theKind;
   }

   public void setKind(int kind) {
      this.theKind = kind;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "kind", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
