package ro.ubbcluj.lci.uml.foundation.core;

import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Procedure;
import ro.ubbcluj.lci.uml.foundation.dataTypes.ProcedureExpression;

public interface Method extends BehavioralFeature {
   ProcedureExpression getBody();

   void setBody(ProcedureExpression var1);

   Procedure getProcedure();

   void setProcedure(Procedure var1);

   Operation getSpecification();

   void setSpecification(Operation var1);
}
