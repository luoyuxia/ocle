package ro.ubbcluj.lci.uml.behavioralElements.activityGraphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.State;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.ClassifierImpl;

public class ClassifierInStateImpl extends ClassifierImpl implements ClassifierInState {
   protected Classifier theType;
   protected Set theInStateList;

   public ClassifierInStateImpl() {
   }

   public Classifier getType() {
      return this.theType;
   }

   public void setType(Classifier arg) {
      if (this.theType != arg) {
         Classifier temp = this.theType;
         this.theType = null;
         if (temp != null) {
            temp.removeClassifierInState(this);
         }

         if (arg != null) {
            this.theType = arg;
            arg.addClassifierInState(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "type", 0));
         }
      }

   }

   public Set getCollectionInStateList() {
      return this.theInStateList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theInStateList);
   }

   public Enumeration getInStateList() {
      return Collections.enumeration(this.getCollectionInStateList());
   }

   public void addInState(State arg) {
      if (arg != null) {
         if (this.theInStateList == null) {
            this.theInStateList = new LinkedHashSet();
         }

         if (this.theInStateList.add(arg)) {
            arg.addClassifierInState(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "inState", 1));
            }
         }
      }

   }

   public void removeInState(State arg) {
      if (this.theInStateList != null && arg != null && this.theInStateList.remove(arg)) {
         arg.removeClassifierInState(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "inState", 2));
         }
      }

   }

   protected void internalRemove() {
      Classifier tmpType = this.getType();
      if (tmpType != null) {
         tmpType.removeClassifierInState(this);
      }

      Enumeration tmpInStateEnum = this.getInStateList();
      ArrayList tmpInStateList = new ArrayList();

      while(tmpInStateEnum.hasMoreElements()) {
         tmpInStateList.add(tmpInStateEnum.nextElement());
      }

      Iterator it = tmpInStateList.iterator();

      while(it.hasNext()) {
         State tmpInState = (State)it.next();
         tmpInState.removeClassifierInState(this);
      }

      super.internalRemove();
   }
}
