package ro.ubbcluj.lci.uml.behavioralElements.useCases;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.RelationshipImpl;

public class IncludeImpl extends RelationshipImpl implements Include {
   protected UseCase theAddition;
   protected UseCase theBase;

   public IncludeImpl() {
   }

   public UseCase getAddition() {
      return this.theAddition;
   }

   public void setAddition(UseCase arg) {
      if (this.theAddition != arg) {
         UseCase temp = this.theAddition;
         this.theAddition = null;
         if (temp != null) {
            temp.removeIncluder(this);
         }

         if (arg != null) {
            this.theAddition = arg;
            arg.addIncluder(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "addition", 0));
         }
      }

   }

   public UseCase getBase() {
      return this.theBase;
   }

   public void setBase(UseCase arg) {
      if (this.theBase != arg) {
         UseCase temp = this.theBase;
         this.theBase = null;
         if (temp != null) {
            temp.removeInclude(this);
         }

         if (arg != null) {
            this.theBase = arg;
            arg.addInclude(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "base", 0));
         }
      }

   }

   protected void internalRemove() {
      UseCase tmpAddition = this.getAddition();
      if (tmpAddition != null) {
         tmpAddition.removeIncluder(this);
      }

      UseCase tmpBase = this.getBase();
      if (tmpBase != null) {
         tmpBase.removeInclude(this);
      }

      super.internalRemove();
   }
}
