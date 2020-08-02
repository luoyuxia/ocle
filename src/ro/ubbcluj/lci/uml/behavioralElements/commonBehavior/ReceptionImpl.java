package ro.ubbcluj.lci.uml.behavioralElements.commonBehavior;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.BehavioralFeatureImpl;

public class ReceptionImpl extends BehavioralFeatureImpl implements Reception {
   protected String theSpecification;
   protected boolean isRoot;
   protected boolean isLeaf;
   protected boolean isAbstract;
   protected Signal theSignal;

   public ReceptionImpl() {
   }

   public String getSpecification() {
      return this.theSpecification;
   }

   public void setSpecification(String specification) {
      this.theSpecification = specification;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "specification", 0));
      }

   }

   public boolean isRoot() {
      return this.isRoot;
   }

   public void setRoot(boolean isRoot) {
      this.isRoot = isRoot;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isRoot", 0));
      }

   }

   public boolean isLeaf() {
      return this.isLeaf;
   }

   public void setLeaf(boolean isLeaf) {
      this.isLeaf = isLeaf;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isLeaf", 0));
      }

   }

   public boolean isAbstract() {
      return this.isAbstract;
   }

   public void setAbstract(boolean isAbstract) {
      this.isAbstract = isAbstract;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isAbstract", 0));
      }

   }

   public Signal getSignal() {
      return this.theSignal;
   }

   public void setSignal(Signal arg) {
      if (this.theSignal != arg) {
         Signal temp = this.theSignal;
         this.theSignal = null;
         if (temp != null) {
            temp.removeReception(this);
         }

         if (arg != null) {
            this.theSignal = arg;
            arg.addReception(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "signal", 0));
         }
      }

   }

   protected void internalRemove() {
      Signal tmpSignal = this.getSignal();
      if (tmpSignal != null) {
         tmpSignal.removeReception(this);
      }

      super.internalRemove();
   }
}
