package ro.ubbcluj.lci.uml.foundation.core;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class ElementOwnershipImpl extends ElementImpl implements ElementOwnership {
   protected int theVisibility;
   protected boolean isSpecification;
   protected ModelElement theOwnedElement;
   protected Namespace theNamespace;

   public ElementOwnershipImpl() {
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

   public boolean isSpecification() {
      return this.isSpecification;
   }

   public void setSpecification(boolean isSpecification) {
      this.isSpecification = isSpecification;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isSpecification", 0));
      }

   }

   public ModelElement getOwnedElement() {
      return this.theOwnedElement;
   }

   public void setOwnedElement(ModelElement arg) {
      if (this.theOwnedElement != arg) {
         ModelElement temp = this.theOwnedElement;
         this.theOwnedElement = null;
         if (temp != null) {
            temp.setNamespace((ElementOwnership)null);
         }

         if (arg != null) {
            this.theOwnedElement = arg;
            arg.setNamespace(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "ownedElement", 0));
         }
      }

   }

   public Namespace getNamespace() {
      return this.theNamespace;
   }

   public void setNamespace(Namespace arg) {
      if (this.theNamespace != arg) {
         Namespace temp = this.theNamespace;
         this.theNamespace = null;
         if (temp != null) {
            temp.removeOwnedElement(this);
         }

         if (arg != null) {
            this.theNamespace = arg;
            arg.addOwnedElement(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "namespace", 0));
         }
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
