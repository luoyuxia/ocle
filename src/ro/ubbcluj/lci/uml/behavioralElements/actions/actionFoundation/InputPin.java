package ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation;

import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Procedure;

public interface InputPin extends Pin {
   DataFlow getFlow();

   void setFlow(DataFlow var1);

   Action getAction();

   void setAction(Action var1);

   Procedure getProcedure();

   void setProcedure(Procedure var1);
}
