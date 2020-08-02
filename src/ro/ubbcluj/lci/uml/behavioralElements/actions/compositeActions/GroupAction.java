package ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.Action;

public interface GroupAction extends Action {
   boolean getMustIsolate();

   void setMustIsolate(boolean var1);

   Enumeration getVariableList();

   Set getCollectionVariableList();

   void addVariable(Variable var1);

   void removeVariable(Variable var1);

   Enumeration getSubactionList();

   Set getCollectionSubactionList();

   void addSubaction(Action var1);

   void removeSubaction(Action var1);
}
