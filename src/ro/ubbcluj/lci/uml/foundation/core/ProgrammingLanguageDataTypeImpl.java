package ro.ubbcluj.lci.uml.foundation.core;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.dataTypes.TypeExpression;

public class ProgrammingLanguageDataTypeImpl extends DataTypeImpl implements ProgrammingLanguageDataType {
   protected TypeExpression theExpression;

   public ProgrammingLanguageDataTypeImpl() {
   }

   public TypeExpression getExpression() {
      return this.theExpression;
   }

   public void setExpression(TypeExpression expression) {
      this.theExpression = expression;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "expression", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
