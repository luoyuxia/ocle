package ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.Action;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.OutputPin;

public interface LoopAction extends Action {
   Enumeration getLoopVariableList();

   Set getCollectionLoopVariableList();

   void addLoopVariable(OutputPin var1);

   void removeLoopVariable(OutputPin var1);

   Clause getClause();

   void setClause(Clause var1);
}
