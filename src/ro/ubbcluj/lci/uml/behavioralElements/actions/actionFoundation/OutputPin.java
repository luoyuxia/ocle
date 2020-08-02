package ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions.LoopAction;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Procedure;

public interface OutputPin extends Pin {
   Enumeration getFlowList();

   Set getCollectionFlowList();

   void addFlow(DataFlow var1);

   void removeFlow(DataFlow var1);

   LoopAction getLoop();

   void setLoop(LoopAction var1);

   Action getAction();

   void setAction(Action var1);

   Procedure getProcedure();

   void setProcedure(Procedure var1);
}
