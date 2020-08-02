package ro.ubbcluj.lci.uml.foundation.core;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.modelManagement.Model;

public class ElementImpl implements Element {
   private Model theOwnerModel;

   public ElementImpl() {
   }

   public Model getOwnerModel() {
      return this.theOwnerModel;
   }

   public void setOwnerModel(Model arg) {
      this.theOwnerModel = arg;
   }

   public String getMetaclassName() {
      String name = this.getClass().getName();
      int idx = name.lastIndexOf(46);
      if (idx > 0) {
         name = name.substring(idx + 1);
      }

      if (name.endsWith("Impl")) {
         name = name.substring(0, name.length() - 4);
      }

      return name;
   }

   public void remove() {
      this.internalRemove();
   }

   protected void internalRemove() {
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "", 3));
      }

   }
}
