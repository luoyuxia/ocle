package ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Procedure;

public class InputPinImpl extends PinImpl implements InputPin {
   protected DataFlow theFlow;
   protected Action theAction;
   protected Procedure theProcedure;

   public InputPinImpl() {
   }

   public DataFlow getFlow() {
      return this.theFlow;
   }

   public void setFlow(DataFlow arg) {
      if (this.theFlow != arg) {
         DataFlow temp = this.theFlow;
         this.theFlow = null;
         if (temp != null) {
            temp.setDestination((InputPin)null);
         }

         if (arg != null) {
            this.theFlow = arg;
            arg.setDestination(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "flow", 0));
         }
      }

   }

   public Action getAction() {
      return this.theAction;
   }

   public void setAction(Action arg) {
      if (this.theAction != arg) {
         Action temp = this.theAction;
         this.theAction = null;
         if (temp != null) {
            temp.removeInputPin(this);
         }

         if (arg != null) {
            this.theAction = arg;
            arg.addInputPin(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "action", 0));
         }
      }

   }

   public Procedure getProcedure() {
      return this.theProcedure;
   }

   public void setProcedure(Procedure arg) {
      if (this.theProcedure != arg) {
         Procedure temp = this.theProcedure;
         this.theProcedure = null;
         if (temp != null) {
            temp.removeResult(this);
         }

         if (arg != null) {
            this.theProcedure = arg;
            arg.addResult(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "procedure", 0));
         }
      }

   }

   protected void internalRemove() {
      DataFlow tmpFlow = this.getFlow();
      if (tmpFlow != null) {
         tmpFlow.setDestination((InputPin)null);
      }

      Action tmpAction = this.getAction();
      if (tmpAction != null) {
         tmpAction.removeInputPin(this);
      }

      Procedure tmpProcedure = this.getProcedure();
      if (tmpProcedure != null) {
         tmpProcedure.removeResult(this);
      }

      super.internalRemove();
   }
}
