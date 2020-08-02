package ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;

public class VariableImpl extends ModelElementImpl implements Variable {
   protected int theOrdering;
   protected Multiplicity theMultiplicity;
   protected GroupAction theScope;
   protected Classifier theType;

   public VariableImpl() {
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

   public GroupAction getScope() {
      return this.theScope;
   }

   public void setScope(GroupAction arg) {
      if (this.theScope != arg) {
         GroupAction temp = this.theScope;
         this.theScope = null;
         if (temp != null) {
            temp.removeVariable(this);
         }

         if (arg != null) {
            this.theScope = arg;
            arg.addVariable(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "scope", 0));
         }
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
      GroupAction tmpScope = this.getScope();
      if (tmpScope != null) {
         tmpScope.removeVariable(this);
      }

      super.internalRemove();
   }
}
