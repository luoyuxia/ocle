package ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.ActionImpl;

public class ConditionalActionImpl extends ActionImpl implements ConditionalAction {
   protected boolean isDeterminate;
   protected Set theClauseList;

   public ConditionalActionImpl() {
   }

   public boolean isDeterminate() {
      return this.isDeterminate;
   }

   public void setDeterminate(boolean isDeterminate) {
      this.isDeterminate = isDeterminate;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isDeterminate", 0));
      }

   }

   public Set getCollectionClauseList() {
      return this.theClauseList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theClauseList);
   }

   public Enumeration getClauseList() {
      return Collections.enumeration(this.getCollectionClauseList());
   }

   public void addClause(Clause arg) {
      if (arg != null) {
         if (this.theClauseList == null) {
            this.theClauseList = new LinkedHashSet();
         }

         this.theClauseList.add(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "clause", 1));
         }
      }

   }

   public void removeClause(Clause arg) {
      if (this.theClauseList != null && arg != null) {
         this.theClauseList.remove(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "clause", 2));
         }
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
