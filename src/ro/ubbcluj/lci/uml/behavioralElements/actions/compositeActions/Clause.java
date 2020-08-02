package ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.Action;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.OutputPin;
import ro.ubbcluj.lci.uml.foundation.core.Element;

public interface Clause extends Element {
   Enumeration getSuccessorClauseList();

   Set getCollectionSuccessorClauseList();

   void addSuccessorClause(Clause var1);

   void removeSuccessorClause(Clause var1);

   Enumeration getPredecessorClauseList();

   Set getCollectionPredecessorClauseList();

   void addPredecessorClause(Clause var1);

   void removePredecessorClause(Clause var1);

   OutputPin getTestOutput();

   void setTestOutput(OutputPin var1);

   Action getBody();

   void setBody(Action var1);

   Enumeration getBodyOutputList();

   Set getCollectionBodyOutputList();

   void addBodyOutput(OutputPin var1);

   void removeBodyOutput(OutputPin var1);

   Action getTest();

   void setTest(Action var1);
}
