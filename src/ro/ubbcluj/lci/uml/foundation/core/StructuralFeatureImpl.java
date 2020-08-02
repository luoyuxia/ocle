package ro.ubbcluj.lci.uml.foundation.core;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;

public class StructuralFeatureImpl extends FeatureImpl implements StructuralFeature {
   protected int theChangeability;
   protected int theTargetScope;
   protected int theOrdering;
   protected Multiplicity theMultiplicity;
   protected Classifier theType;

   public StructuralFeatureImpl() {
   }

   public int getChangeability() {
      return this.theChangeability;
   }

   public void setChangeability(int changeability) {
      this.theChangeability = changeability;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "changeability", 0));
      }

   }

   public int getTargetScope() {
      return this.theTargetScope;
   }

   public void setTargetScope(int targetScope) {
      this.theTargetScope = targetScope;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "targetScope", 0));
      }

   }

   public int getOrdering() {
      return this.theOrdering;
   }

   public void setOrdering(int ordering) {
      this.theOrdering = ordering;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "ordering", 0));
      }

   }

   public Multiplicity getMultiplicity() {
      return this.theMultiplicity;
   }

   public void setMultiplicity(Multiplicity multiplicity) {
      this.theMultiplicity = multiplicity;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "multiplicity", 0));
      }

   }

   public Classifier getType() {
      return this.theType;
   }

   public void setType(Classifier arg) {
      if (this.theType != arg) {
         Classifier temp = this.theType;
         this.theType = null;
         if (temp != null) {
            temp.removeTypedFeature(this);
         }

         if (arg != null) {
            this.theType = arg;
            arg.addTypedFeature(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "type", 0));
         }
      }

   }

   protected void internalRemove() {
      Classifier tmpType = this.getType();
      if (tmpType != null) {
         tmpType.removeTypedFeature(this);
      }

      super.internalRemove();
   }
}
