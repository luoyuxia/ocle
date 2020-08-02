package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.codegen.framework.ocl.CollectionUtilities;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;

public class StateMachineImpl extends ModelElementImpl implements StateMachine {
   protected Set theSubmachineStateList;
   protected State theTop;
   protected ModelElement theContext;
   protected Set theTransitionsList;

   public StateMachineImpl() {
   }

   public Set getCollectionSubmachineStateList() {
      return this.theSubmachineStateList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theSubmachineStateList);
   }

   public Enumeration getSubmachineStateList() {
      return Collections.enumeration(this.getCollectionSubmachineStateList());
   }

   public void addSubmachineState(SubmachineState arg) {
      if (arg != null) {
         if (this.theSubmachineStateList == null) {
            this.theSubmachineStateList = new LinkedHashSet();
         }

         if (this.theSubmachineStateList.add(arg)) {
            arg.setSubmachine(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "submachineState", 1));
            }
         }
      }

   }

   public void removeSubmachineState(SubmachineState arg) {
      if (this.theSubmachineStateList != null && arg != null && this.theSubmachineStateList.remove(arg)) {
         arg.setSubmachine((StateMachine)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "submachineState", 2));
         }
      }

   }

   public State getTop() {
      return this.theTop;
   }

   public void setTop(State arg) {
      if (this.theTop != arg) {
         State temp = this.theTop;
         this.theTop = null;
         if (temp != null) {
            temp.setStateMachine((StateMachine)null);
         }

         if (arg != null) {
            this.theTop = arg;
            arg.setStateMachine(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "top", 0));
         }
      }

   }

   public ModelElement getContext() {
      return this.theContext;
   }

   public void setContext(ModelElement arg) {
      if (this.theContext != arg) {
         ModelElement temp = this.theContext;
         this.theContext = null;
         if (temp != null) {
            temp.removeBehavior(this);
         }

         if (arg != null) {
            this.theContext = arg;
            arg.addBehavior(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "context", 0));
         }
      }

   }

   public Set getCollectionTransitionsList() {
      return this.theTransitionsList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theTransitionsList);
   }

   public Enumeration getTransitionsList() {
      return Collections.enumeration(this.getCollectionTransitionsList());
   }

   public void addTransitions(Transition arg) {
      if (arg != null) {
         if (this.theTransitionsList == null) {
            this.theTransitionsList = new LinkedHashSet();
         }

         if (this.theTransitionsList.add(arg)) {
            arg.setStateMachine(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "transitions", 1));
            }
         }
      }

   }

   public void removeTransitions(Transition arg) {
      if (this.theTransitionsList != null && arg != null && this.theTransitionsList.remove(arg)) {
         arg.setStateMachine((StateMachine)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "transitions", 2));
         }
      }

   }

   public CompositeState LCA(State s1, State s2) {
      boolean bancestor = this.ancestor(s1, s2);
      CompositeState compositeStateIf;
      CompositeState compositeStateIf0;
      if (bancestor) {
         compositeStateIf0 = (CompositeState)s1;
         compositeStateIf = compositeStateIf0;
      } else {
         boolean bancestor0 = this.ancestor(s2, s1);
         if (bancestor0) {
            CompositeState compositeStateOclAsType0 = (CompositeState)s2;
            compositeStateIf0 = compositeStateOclAsType0;
         } else {
            CompositeState compositeStateContainer = s1.getContainer();
            CompositeState compositeStateContainer0 = s2.getContainer();
            CompositeState compositeStateLCA = this.LCA(compositeStateContainer, compositeStateContainer0);
            compositeStateIf0 = compositeStateLCA;
         }

         compositeStateIf = compositeStateIf0;
      }

      return compositeStateIf;
   }

   public boolean ancestor(State s1, State s2) {
      boolean bEquals = s2.equals(s1);
      boolean bIf;
      if (bEquals) {
         bIf = true;
      } else {
         CompositeState compositeStateContainer = s1.getContainer();
         boolean bIsEmpty = CollectionUtilities.isEmpty((Object)compositeStateContainer);
         boolean bIf0;
         if (bIsEmpty) {
            bIf0 = true;
         } else {
            CompositeState compositeStateContainer0 = s2.getContainer();
            boolean bIsEmpty0 = CollectionUtilities.isEmpty((Object)compositeStateContainer0);
            boolean bIf1;
            if (bIsEmpty0) {
               bIf1 = false;
            } else {
               CompositeState compositeStateContainer1 = s2.getContainer();
               boolean bancestor = this.ancestor(s1, compositeStateContainer1);
               bIf1 = bancestor;
            }

            bIf0 = bIf1;
         }

         bIf = bIf0;
      }

      return bIf;
   }

   protected void internalRemove() {
      Enumeration tmpSubmachineStateEnum = this.getSubmachineStateList();
      ArrayList tmpSubmachineStateList = new ArrayList();

      while(tmpSubmachineStateEnum.hasMoreElements()) {
         tmpSubmachineStateList.add(tmpSubmachineStateEnum.nextElement());
      }

      Iterator it = tmpSubmachineStateList.iterator();

      while(it.hasNext()) {
         ((SubmachineState)it.next()).setSubmachine((StateMachine)null);
      }

      State tmpTop = this.getTop();
      if (tmpTop != null) {
         tmpTop.remove();
      }

      ModelElement tmpContext = this.getContext();
      if (tmpContext != null) {
         tmpContext.removeBehavior(this);
      }

      Enumeration tmpTransitionsEnum = this.getTransitionsList();
      ArrayList tmpTransitionsList = new ArrayList();

      while(tmpTransitionsEnum.hasMoreElements()) {
         tmpTransitionsList.add(tmpTransitionsEnum.nextElement());
      }

       it = tmpTransitionsList.iterator();

      while(it.hasNext()) {
         ((Transition)it.next()).remove();
      }

      super.internalRemove();
   }
}
