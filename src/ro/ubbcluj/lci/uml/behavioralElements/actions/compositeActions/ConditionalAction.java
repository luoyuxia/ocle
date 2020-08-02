package ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.Action;

public interface ConditionalAction extends Action {
   boolean isDeterminate();

   void setDeterminate(boolean var1);

   Enumeration getClauseList();

   Set getCollectionClauseList();

   void addClause(Clause var1);

   void removeClause(Clause var1);
}
