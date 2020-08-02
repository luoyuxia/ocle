package ro.ubbcluj.lci.uml.foundation.core;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class ClassImpl extends ClassifierImpl implements Class {
   protected boolean isActive;

   public ClassImpl() {
   }

   public boolean isActive() {
      return this.isActive;
   }

   public void setActive(boolean isActive) {
      this.isActive = isActive;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isActive", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
