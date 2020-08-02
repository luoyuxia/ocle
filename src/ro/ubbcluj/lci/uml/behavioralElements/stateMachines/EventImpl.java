package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;
import ro.ubbcluj.lci.uml.foundation.core.Parameter;

public class EventImpl extends ModelElementImpl implements Event {
   protected Set theParameterList;
   protected Set theStateList;
   protected Set theTransitionList;

   public EventImpl() {
   }

   public Set getCollectionParameterList() {
      return this.theParameterList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theParameterList);
   }

   public Enumeration getParameterList() {
      return Collections.enumeration(this.getCollectionParameterList());
   }

   public void addParameter(Parameter arg) {
      if (arg != null) {
         if (this.theParameterList == null) {
            this.theParameterList = new LinkedHashSet();
         }

         if (this.theParameterList.add(arg)) {
            arg.setEvent(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "parameter", 1));
            }
         }
      }

   }

   public void removeParameter(Parameter arg) {
      if (this.theParameterList != null && arg != null && this.theParameterList.remove(arg)) {
         arg.setEvent((Event)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "parameter", 2));
         }
      }

   }

   public Set getCollectionStateList() {
      return this.theStateList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theStateList);
   }

   public Enumeration getStateList() {
      return Collections.enumeration(this.getCollectionStateList());
   }

   public void addState(State arg) {
      if (arg != null) {
         if (this.theStateList == null) {
            this.theStateList = new LinkedHashSet();
         }

         if (this.theStateList.add(arg)) {
            arg.addDeferrableEvent(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "state", 1));
            }
         }
      }

   }

   public void removeState(State arg) {
      if (this.theStateList != null && arg != null && this.theStateList.remove(arg)) {
         arg.removeDeferrableEvent(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "state", 2));
         }
      }

   }

   public Set getCollectionTransitionList() {
      return this.theTransitionList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theTransitionList);
   }

   public Enumeration getTransitionList() {
      return Collections.enumeration(this.getCollectionTransitionList());
   }

   public void addTransition(Transition arg) {
      if (arg != null) {
         if (this.theTransitionList == null) {
            this.theTransitionList = new LinkedHashSet();
         }

         if (this.theTransitionList.add(arg)) {
            arg.setTrigger(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "transition", 1));
            }
         }
      }

   }

   public void removeTransition(Transition arg) {
      if (this.theTransitionList != null && arg != null && this.theTransitionList.remove(arg)) {
         arg.setTrigger((Event)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "transition", 2));
         }
      }

   }

   protected void internalRemove() {
      Enumeration tmpParameterEnum = this.getParameterList();
      ArrayList tmpParameterList = new ArrayList();

      while(tmpParameterEnum.hasMoreElements()) {
         tmpParameterList.add(tmpParameterEnum.nextElement());
      }

      Iterator it = tmpParameterList.iterator();

      while(it.hasNext()) {
         ((Parameter)it.next()).remove();
      }

      Enumeration tmpStateEnum = this.getStateList();
      ArrayList tmpStateList = new ArrayList();

      while(tmpStateEnum.hasMoreElements()) {
         tmpStateList.add(tmpStateEnum.nextElement());
      }

       it = tmpStateList.iterator();

      while(it.hasNext()) {
         State tmpState = (State)it.next();
         tmpState.removeDeferrableEvent(this);
      }

      Enumeration tmpTransitionEnum = this.getTransitionList();
      ArrayList tmpTransitionList = new ArrayList();

      while(tmpTransitionEnum.hasMoreElements()) {
         tmpTransitionList.add(tmpTransitionEnum.nextElement());
      }

       it = tmpTransitionList.iterator();

      while(it.hasNext()) {
         ((Transition)it.next()).setTrigger((Event)null);
      }

      super.internalRemove();
   }
}
