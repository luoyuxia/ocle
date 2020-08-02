package ro.ubbcluj.lci.uml.foundation.core;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.dataTypes.MappingExpression;

public class AbstractionImpl extends DependencyImpl implements Abstraction {
   protected MappingExpression theMapping;

   public AbstractionImpl() {
   }

   public MappingExpression getMapping() {
      return this.theMapping;
   }

   public void setMapping(MappingExpression mapping) {
      this.theMapping = mapping;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "mapping", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
