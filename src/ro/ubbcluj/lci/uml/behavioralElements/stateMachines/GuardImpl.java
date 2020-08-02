package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;
import ro.ubbcluj.lci.uml.foundation.dataTypes.BooleanExpression;

public class GuardImpl extends ModelElementImpl implements Guard {
   protected BooleanExpression theExpression;
   protected Transition theTransition;

   public GuardImpl() {
   }

   public BooleanExpression getExpression() {
      return this.theExpression;
   }

   public void setExpression(BooleanExpression expression) {
      this.theExpression = expression;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "expression", 0));
      }

   }

   public Transition getTransition() {
      return this.theTransition;
   }

   public void setTransition(Transition arg) {
      if (this.theTransition != arg) {
         Transition temp = this.theTransition;
         this.theTransition = null;
         if (temp != null) {
            temp.setGuard((Guard)null);
         }

         if (arg != null) {
            this.theTransition = arg;
            arg.setGuard(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "transition", 0));
         }
      }

   }

   protected void internalRemove() {
      Transition tmpTransition = this.getTransition();
      if (tmpTransition != null) {
         tmpTransition.setGuard((Guard)null);
      }

      super.internalRemove();
   }
}
