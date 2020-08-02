package ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.Action;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.OutputPin;
import ro.ubbcluj.lci.uml.foundation.core.ElementImpl;

public class ClauseImpl extends ElementImpl implements Clause {
   protected Set theSuccessorClauseList;
   protected Set thePredecessorClauseList;
   protected OutputPin theTestOutput;
   protected Action theBody;
   protected Set theBodyOutputList;
   protected Action theTest;

   public ClauseImpl() {
   }

   public Set getCollectionSuccessorClauseList() {
      return this.theSuccessorClauseList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theSuccessorClauseList);
   }

   public Enumeration getSuccessorClauseList() {
      return Collections.enumeration(this.getCollectionSuccessorClauseList());
   }

   public void addSuccessorClause(Clause arg) {
      if (arg != null) {
         if (this.theSuccessorClauseList == null) {
            this.theSuccessorClauseList = new LinkedHashSet();
         }

         if (this.theSuccessorClauseList.add(arg)) {
            arg.addPredecessorClause(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "successorClause", 1));
            }
         }
      }

   }

   public void removeSuccessorClause(Clause arg) {
      if (this.theSuccessorClauseList != null && arg != null && this.theSuccessorClauseList.remove(arg)) {
         arg.removePredecessorClause(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "successorClause", 2));
         }
      }

   }

   public Set getCollectionPredecessorClauseList() {
      return this.thePredecessorClauseList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.thePredecessorClauseList);
   }

   public Enumeration getPredecessorClauseList() {
      return Collections.enumeration(this.getCollectionPredecessorClauseList());
   }

   public void addPredecessorClause(Clause arg) {
      if (arg != null) {
         if (this.thePredecessorClauseList == null) {
            this.thePredecessorClauseList = new LinkedHashSet();
         }

         if (this.thePredecessorClauseList.add(arg)) {
            arg.addSuccessorClause(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "predecessorClause", 1));
            }
         }
      }

   }

   public void removePredecessorClause(Clause arg) {
      if (this.thePredecessorClauseList != null && arg != null && this.thePredecessorClauseList.remove(arg)) {
         arg.removeSuccessorClause(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "predecessorClause", 2));
         }
      }

   }

   public OutputPin getTestOutput() {
      return this.theTestOutput;
   }

   public void setTestOutput(OutputPin arg) {
      this.theTestOutput = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "testOutput", 0));
      }

   }

   public Action getBody() {
      return this.theBody;
   }

   public void setBody(Action arg) {
      this.theBody = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "body", 0));
      }

   }

   public Set getCollectionBodyOutputList() {
      return this.theBodyOutputList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theBodyOutputList);
   }

   public Enumeration getBodyOutputList() {
      return Collections.enumeration(this.getCollectionBodyOutputList());
   }

   public void addBodyOutput(OutputPin arg) {
      if (arg != null) {
         if (this.theBodyOutputList == null) {
            this.theBodyOutputList = new LinkedHashSet();
         }

         this.theBodyOutputList.add(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "bodyOutput", 1));
         }
      }

   }

   public void removeBodyOutput(OutputPin arg) {
      if (this.theBodyOutputList != null && arg != null) {
         this.theBodyOutputList.remove(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "bodyOutput", 2));
         }
      }

   }

   public Action getTest() {
      return this.theTest;
   }

   public void setTest(Action arg) {
      this.theTest = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "test", 0));
      }

   }

   protected void internalRemove() {
      Enumeration tmpSuccessorClauseEnum = this.getSuccessorClauseList();
      ArrayList tmpSuccessorClauseList = new ArrayList();

      while(tmpSuccessorClauseEnum.hasMoreElements()) {
         tmpSuccessorClauseList.add(tmpSuccessorClauseEnum.nextElement());
      }

      Iterator it = tmpSuccessorClauseList.iterator();

      while(it.hasNext()) {
         Clause tmpSuccessorClause = (Clause)it.next();
         tmpSuccessorClause.removePredecessorClause(this);
      }

      Enumeration tmpPredecessorClauseEnum = this.getPredecessorClauseList();
      ArrayList tmpPredecessorClauseList = new ArrayList();

      while(tmpPredecessorClauseEnum.hasMoreElements()) {
         tmpPredecessorClauseList.add(tmpPredecessorClauseEnum.nextElement());
      }

       it = tmpPredecessorClauseList.iterator();

      while(it.hasNext()) {
         Clause tmpPredecessorClause = (Clause)it.next();
         tmpPredecessorClause.removeSuccessorClause(this);
      }

      super.internalRemove();
   }
}
