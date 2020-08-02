package ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;

public class PinImpl extends ModelElementImpl implements Pin {
   protected int theOrdering;
   protected Multiplicity theMultiplicity;
   protected Classifier theType;

   public PinImpl() {
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
      this.theType = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "type", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
