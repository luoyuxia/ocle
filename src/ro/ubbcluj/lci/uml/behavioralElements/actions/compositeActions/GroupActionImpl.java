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
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.ActionImpl;

public class GroupActionImpl extends ActionImpl implements GroupAction {
   protected boolean theMustIsolate;
   protected Set theVariableList;
   protected Set theSubactionList;

   public GroupActionImpl() {
   }

   public boolean getMustIsolate() {
      return this.theMustIsolate;
   }

   public void setMustIsolate(boolean mustIsolate) {
      this.theMustIsolate = mustIsolate;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "mustIsolate", 0));
      }

   }

   public Set getCollectionVariableList() {
      return this.theVariableList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theVariableList);
   }

   public Enumeration getVariableList() {
      return Collections.enumeration(this.getCollectionVariableList());
   }

   public void addVariable(Variable arg) {
      if (arg != null) {
         if (this.theVariableList == null) {
            this.theVariableList = new LinkedHashSet();
         }

         if (this.theVariableList.add(arg)) {
            arg.setScope(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "variable", 1));
            }
         }
      }

   }

   public void removeVariable(Variable arg) {
      if (this.theVariableList != null && arg != null && this.theVariableList.remove(arg)) {
         arg.setScope((GroupAction)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "variable", 2));
         }
      }

   }

   public Set getCollectionSubactionList() {
      return this.theSubactionList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theSubactionList);
   }

   public Enumeration getSubactionList() {
      return Collections.enumeration(this.getCollectionSubactionList());
   }

   public void addSubaction(Action arg) {
      if (arg != null) {
         if (this.theSubactionList == null) {
            this.theSubactionList = new LinkedHashSet();
         }

         if (this.theSubactionList.add(arg)) {
            arg.setGroup(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "subaction", 1));
            }
         }
      }

   }

   public void removeSubaction(Action arg) {
      if (this.theSubactionList != null && arg != null && this.theSubactionList.remove(arg)) {
         arg.setGroup((GroupAction)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "subaction", 2));
         }
      }

   }

   protected void internalRemove() {
      Enumeration tmpVariableEnum = this.getVariableList();
      ArrayList tmpVariableList = new ArrayList();

      while(tmpVariableEnum.hasMoreElements()) {
         tmpVariableList.add(tmpVariableEnum.nextElement());
      }

      Iterator it = tmpVariableList.iterator();

      while(it.hasNext()) {
         ((Variable)it.next()).remove();
      }

      Enumeration tmpSubactionEnum = this.getSubactionList();
      ArrayList tmpSubactionList = new ArrayList();

      while(tmpSubactionEnum.hasMoreElements()) {
         tmpSubactionList.add(tmpSubactionEnum.nextElement());
      }

       it = tmpSubactionList.iterator();

      while(it.hasNext()) {
         ((Action)it.next()).remove();
      }

      super.internalRemove();
   }
}
