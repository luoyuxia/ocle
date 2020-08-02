package ro.ubbcluj.lci.uml.behavioralElements.actions.compositeActions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.ActionImpl;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.OutputPin;

public class LoopActionImpl extends ActionImpl implements LoopAction {
   protected Set theLoopVariableList;
   protected Clause theClause;

   public LoopActionImpl() {
   }

   public Set getCollectionLoopVariableList() {
      return this.theLoopVariableList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theLoopVariableList);
   }

   public Enumeration getLoopVariableList() {
      return Collections.enumeration(this.getCollectionLoopVariableList());
   }

   public void addLoopVariable(OutputPin arg) {
      if (arg != null) {
         if (this.theLoopVariableList == null) {
            this.theLoopVariableList = new LinkedHashSet();
         }

         if (this.theLoopVariableList.add(arg)) {
            arg.setLoop(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "loopVariable", 1));
            }
         }
      }

   }

   public void removeLoopVariable(OutputPin arg) {
      if (this.theLoopVariableList != null && arg != null && this.theLoopVariableList.remove(arg)) {
         arg.setLoop((LoopAction)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "loopVariable", 2));
         }
      }

   }

   public Clause getClause() {
      return this.theClause;
   }

   public void setClause(Clause arg) {
      this.theClause = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "clause", 0));
      }

   }

   protected void internalRemove() {
      Enumeration tmpLoopVariableEnum = this.getLoopVariableList();
      ArrayList tmpLoopVariableList = new ArrayList();

      while(tmpLoopVariableEnum.hasMoreElements()) {
         tmpLoopVariableList.add(tmpLoopVariableEnum.nextElement());
      }

      Iterator it = tmpLoopVariableList.iterator();

      while(it.hasNext()) {
         ((OutputPin)it.next()).remove();
      }

      super.internalRemove();
   }
}
