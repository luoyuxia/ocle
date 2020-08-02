package ro.ubbcluj.lci.uml.foundation.core;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Procedure;
import ro.ubbcluj.lci.uml.foundation.dataTypes.ProcedureExpression;

public class MethodImpl extends BehavioralFeatureImpl implements Method {
   protected ProcedureExpression theBody;
   protected Procedure theProcedure;
   protected Operation theSpecification;

   public MethodImpl() {
   }

   public ProcedureExpression getBody() {
      return this.theBody;
   }

   public void setBody(ProcedureExpression body) {
      this.theBody = body;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "body", 0));
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
            temp.removeMethod(this);
         }

         if (arg != null) {
            this.theProcedure = arg;
            arg.addMethod(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "procedure", 0));
         }
      }

   }

   public Operation getSpecification() {
      return this.theSpecification;
   }

   public void setSpecification(Operation arg) {
      if (this.theSpecification != arg) {
         Operation temp = this.theSpecification;
         this.theSpecification = null;
         if (temp != null) {
            temp.removeMethod(this);
         }

         if (arg != null) {
            this.theSpecification = arg;
            arg.addMethod(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "specification", 0));
         }
      }

   }

   protected void internalRemove() {
      Procedure tmpProcedure = this.getProcedure();
      if (tmpProcedure != null) {
         tmpProcedure.removeMethod(this);
      }

      Operation tmpSpecification = this.getSpecification();
      if (tmpSpecification != null) {
         tmpSpecification.removeMethod(this);
      }

      super.internalRemove();
   }
}
