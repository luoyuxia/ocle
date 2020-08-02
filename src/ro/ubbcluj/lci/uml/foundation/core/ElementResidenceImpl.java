package ro.ubbcluj.lci.uml.foundation.core;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class ElementResidenceImpl extends ElementImpl implements ElementResidence {
   protected int theVisibility;
   protected ModelElement theResident;
   protected Component theContainer;

   public ElementResidenceImpl() {
   }

   public int getVisibility() {
      return this.theVisibility;
   }

   public void setVisibility(int visibility) {
      this.theVisibility = visibility;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "visibility", 0));
      }

   }

   public ModelElement getResident() {
      return this.theResident;
   }

   public void setResident(ModelElement arg) {
      if (this.theResident != arg) {
         ModelElement temp = this.theResident;
         this.theResident = null;
         if (temp != null) {
            temp.removeContainer(this);
         }

         if (arg != null) {
            this.theResident = arg;
            arg.addContainer(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "resident", 0));
         }
      }

   }

   public Component getContainer() {
      return this.theContainer;
   }

   public void setContainer(Component arg) {
      if (this.theContainer != arg) {
         Component temp = this.theContainer;
         this.theContainer = null;
         if (temp != null) {
            temp.removeResident(this);
         }

         if (arg != null) {
            this.theContainer = arg;
            arg.addResident(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "container", 0));
         }
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
