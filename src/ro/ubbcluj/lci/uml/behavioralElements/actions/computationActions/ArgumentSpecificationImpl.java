package ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.DataType;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;

public class ArgumentSpecificationImpl extends ModelElementImpl implements ArgumentSpecification {
   protected int theOrdering;
   protected Multiplicity theMultiplicity;
   protected DataType theType;

   public ArgumentSpecificationImpl() {
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

   public DataType getType() {
      return this.theType;
   }

   public void setType(DataType arg) {
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
