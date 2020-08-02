package ro.ubbcluj.lci.uml.modelManagement;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.ElementImpl;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public class ElementImportImpl extends ElementImpl implements ElementImport {
   protected int theVisibility;
   protected String theAlias;
   protected boolean isSpecification;
   protected ModelElement theImportedElement;
   protected Package thePackage;

   public ElementImportImpl() {
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

   public String getAlias() {
      return this.theAlias;
   }

   public void setAlias(String alias) {
      this.theAlias = alias;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "alias", 0));
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

   public ModelElement getImportedElement() {
      return this.theImportedElement;
   }

   public void setImportedElement(ModelElement arg) {
      if (this.theImportedElement != arg) {
         ModelElement temp = this.theImportedElement;
         this.theImportedElement = null;
         if (temp != null) {
            temp.removePackage(this);
         }

         if (arg != null) {
            this.theImportedElement = arg;
            arg.addPackage(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "importedElement", 0));
         }
      }

   }

   public Package getPackage() {
      return this.thePackage;
   }

   public void setPackage(Package arg) {
      if (this.thePackage != arg) {
         Package temp = this.thePackage;
         this.thePackage = null;
         if (temp != null) {
            temp.removeImportedElement(this);
         }

         if (arg != null) {
            this.thePackage = arg;
            arg.addImportedElement(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "package", 0));
         }
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
