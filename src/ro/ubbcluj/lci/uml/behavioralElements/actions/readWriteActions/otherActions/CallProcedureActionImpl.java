package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.otherActions;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Procedure;

public class CallProcedureActionImpl extends PrimitiveActionImpl implements CallProcedureAction {
   protected boolean isSynchronous;
   protected Procedure theProcedure;

   public CallProcedureActionImpl() {
   }

   public boolean isSynchronous() {
      return this.isSynchronous;
   }

   public void setSynchronous(boolean isSynchronous) {
      this.isSynchronous = isSynchronous;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isSynchronous", 0));
      }

   }

   public Procedure getProcedure() {
      return this.theProcedure;
   }

   public void setProcedure(Procedure arg) {
      this.theProcedure = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "procedure", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
