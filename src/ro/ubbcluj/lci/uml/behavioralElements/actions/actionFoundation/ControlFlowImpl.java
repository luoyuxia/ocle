package ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;

public class ControlFlowImpl extends ModelElementImpl implements ControlFlow {
   protected Action theSuccessor;
   protected Action thePredecessor;

   public ControlFlowImpl() {
   }

   public Action getSuccessor() {
      return this.theSuccessor;
   }

   public void setSuccessor(Action arg) {
      if (this.theSuccessor != arg) {
         Action temp = this.theSuccessor;
         this.theSuccessor = null;
         if (temp != null) {
            temp.removeAntecedent(this);
         }

         if (arg != null) {
            this.theSuccessor = arg;
            arg.addAntecedent(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "successor", 0));
         }
      }

   }

   public Action getPredecessor() {
      return this.thePredecessor;
   }

   public void setPredecessor(Action arg) {
      if (this.thePredecessor != arg) {
         Action temp = this.thePredecessor;
         this.thePredecessor = null;
         if (temp != null) {
            temp.removeConsequent(this);
         }

         if (arg != null) {
            this.thePredecessor = arg;
            arg.addConsequent(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "predecessor", 0));
         }
      }

   }

   protected void internalRemove() {
      Action tmpSuccessor = this.getSuccessor();
      if (tmpSuccessor != null) {
         tmpSuccessor.removeAntecedent(this);
      }

      Action tmpPredecessor = this.getPredecessor();
      if (tmpPredecessor != null) {
         tmpPredecessor.removeConsequent(this);
      }

      super.internalRemove();
   }
}
