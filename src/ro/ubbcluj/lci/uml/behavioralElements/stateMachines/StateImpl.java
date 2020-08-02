package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.ClassifierInState;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Procedure;

public class StateImpl extends StateVertexImpl implements State {
   protected Procedure theExit;
   protected Procedure theDoActivity;
   protected StateMachine theStateMachine;
   protected Set theInternalTransitionList;
   protected Procedure theEntry;
   protected Set theDeferrableEventList;
   protected Set theClassifierInStateList;

   public StateImpl() {
   }

   public Procedure getExit() {
      return this.theExit;
   }

   public void setExit(Procedure arg) {
      if (this.theExit != arg) {
         Procedure temp = this.theExit;
         this.theExit = null;
         if (temp != null) {
            temp.setState2((State)null);
         }

         if (arg != null) {
            this.theExit = arg;
            arg.setState2(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "exit", 0));
         }
      }

   }

   public Procedure getDoActivity() {
      return this.theDoActivity;
   }

   public void setDoActivity(Procedure arg) {
      if (this.theDoActivity != arg) {
         Procedure temp = this.theDoActivity;
         this.theDoActivity = null;
         if (temp != null) {
            temp.setState3((State)null);
         }

         if (arg != null) {
            this.theDoActivity = arg;
            arg.setState3(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "doActivity", 0));
         }
      }

   }

   public StateMachine getStateMachine() {
      return this.theStateMachine;
   }

   public void setStateMachine(StateMachine arg) {
      if (this.theStateMachine != arg) {
         StateMachine temp = this.theStateMachine;
         this.theStateMachine = null;
         if (temp != null) {
            temp.setTop((State)null);
         }

         if (arg != null) {
            this.theStateMachine = arg;
            arg.setTop(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "stateMachine", 0));
         }
      }

   }

   public Set getCollectionInternalTransitionList() {
      return this.theInternalTransitionList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theInternalTransitionList);
   }

   public Enumeration getInternalTransitionList() {
      return Collections.enumeration(this.getCollectionInternalTransitionList());
   }

   public void addInternalTransition(Transition arg) {
      if (arg != null) {
         if (this.theInternalTransitionList == null) {
            this.theInternalTransitionList = new LinkedHashSet();
         }

         if (this.theInternalTransitionList.add(arg)) {
            arg.setState(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "internalTransition", 1));
            }
         }
      }

   }

   public void removeInternalTransition(Transition arg) {
      if (this.theInternalTransitionList != null && arg != null && this.theInternalTransitionList.remove(arg)) {
         arg.setState((State)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "internalTransition", 2));
         }
      }

   }

   public Procedure getEntry() {
      return this.theEntry;
   }

   public void setEntry(Procedure arg) {
      if (this.theEntry != arg) {
         Procedure temp = this.theEntry;
         this.theEntry = null;
         if (temp != null) {
            temp.setState1((State)null);
         }

         if (arg != null) {
            this.theEntry = arg;
            arg.setState1(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "entry", 0));
         }
      }

   }

   public Set getCollectionDeferrableEventList() {
      return this.theDeferrableEventList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theDeferrableEventList);
   }

   public Enumeration getDeferrableEventList() {
      return Collections.enumeration(this.getCollectionDeferrableEventList());
   }

   public void addDeferrableEvent(Event arg) {
      if (arg != null) {
         if (this.theDeferrableEventList == null) {
            this.theDeferrableEventList = new LinkedHashSet();
         }

         if (this.theDeferrableEventList.add(arg)) {
            arg.addState(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "deferrableEvent", 1));
            }
         }
      }

   }

   public void removeDeferrableEvent(Event arg) {
      if (this.theDeferrableEventList != null && arg != null && this.theDeferrableEventList.remove(arg)) {
         arg.removeState(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "deferrableEvent", 2));
         }
      }

   }

   public Set getCollectionClassifierInStateList() {
      return this.theClassifierInStateList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theClassifierInStateList);
   }

   public Enumeration getClassifierInStateList() {
      return Collections.enumeration(this.getCollectionClassifierInStateList());
   }

   public void addClassifierInState(ClassifierInState arg) {
      if (arg != null) {
         if (this.theClassifierInStateList == null) {
            this.theClassifierInStateList = new LinkedHashSet();
         }

         if (this.theClassifierInStateList.add(arg)) {
            arg.addInState(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "classifierInState", 1));
            }
         }
      }

   }

   public void removeClassifierInState(ClassifierInState arg) {
      if (this.theClassifierInStateList != null && arg != null && this.theClassifierInStateList.remove(arg)) {
         arg.removeInState(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "classifierInState", 2));
         }
      }

   }

   protected void internalRemove() {
      Procedure tmpExit = this.getExit();
      if (tmpExit != null) {
         tmpExit.remove();
      }

      Procedure tmpDoActivity = this.getDoActivity();
      if (tmpDoActivity != null) {
         tmpDoActivity.remove();
      }

      StateMachine tmpStateMachine = this.getStateMachine();
      if (tmpStateMachine != null) {
         tmpStateMachine.setTop((State)null);
      }

      Enumeration tmpInternalTransitionEnum = this.getInternalTransitionList();
      ArrayList tmpInternalTransitionList = new ArrayList();

      while(tmpInternalTransitionEnum.hasMoreElements()) {
         tmpInternalTransitionList.add(tmpInternalTransitionEnum.nextElement());
      }

      Iterator it = tmpInternalTransitionList.iterator();

      while(it.hasNext()) {
         ((Transition)it.next()).remove();
      }

      Procedure tmpEntry = this.getEntry();
      if (tmpEntry != null) {
         tmpEntry.remove();
      }

      Enumeration tmpDeferrableEventEnum = this.getDeferrableEventList();
      ArrayList tmpDeferrableEventList = new ArrayList();

      while(tmpDeferrableEventEnum.hasMoreElements()) {
         tmpDeferrableEventList.add(tmpDeferrableEventEnum.nextElement());
      }

       it = tmpDeferrableEventList.iterator();

      while(it.hasNext()) {
         Event tmpDeferrableEvent = (Event)it.next();
         tmpDeferrableEvent.removeState(this);
      }

      Enumeration tmpClassifierInStateEnum = this.getClassifierInStateList();
      ArrayList tmpClassifierInStateList = new ArrayList();

      while(tmpClassifierInStateEnum.hasMoreElements()) {
         tmpClassifierInStateList.add(tmpClassifierInStateEnum.nextElement());
      }

       it = tmpClassifierInStateList.iterator();

      while(it.hasNext()) {
         ClassifierInState tmpClassifierInState = (ClassifierInState)it.next();
         tmpClassifierInState.removeInState(this);
         if (tmpClassifierInState.getCollectionInStateList().size() < 1) {
            tmpClassifierInState.remove();
         }
      }

      super.internalRemove();
   }
}
