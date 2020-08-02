package ro.ubbcluj.lci.uml.foundation.dataTypes;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Procedure;
import ro.ubbcluj.lci.uml.foundation.core.ElementImpl;

public class ExpressionImpl extends ElementImpl implements Expression {
   protected String theLanguage;
   protected String theBody;
   protected Procedure theProcedure;

   public ExpressionImpl() {
   }

   public String getLanguage() {
      return this.theLanguage;
   }

   public void setLanguage(String language) {
      this.theLanguage = language;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "language", 0));
      }

   }

   public String getBody() {
      return this.theBody;
   }

   public void setBody(String body) {
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
            temp.removeExpression(this);
         }

         if (arg != null) {
            this.theProcedure = arg;
            arg.addExpression(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "procedure", 0));
         }
      }

   }

   protected void internalRemove() {
      Procedure tmpProcedure = this.getProcedure();
      if (tmpProcedure != null) {
         tmpProcedure.removeExpression(this);
      }

      super.internalRemove();
   }
}
