package ro.ubbcluj.lci.uml.foundation.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.ObjectFlowState;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.Event;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Expression;

public class ParameterImpl extends ModelElementImpl implements Parameter {
   protected int theKind;
   protected Expression theDefaultValue;
   protected Event theEvent;
   protected Set theStateList;
   protected Classifier theType;
   protected BehavioralFeature theBehavioralFeature;

   public ParameterImpl() {
   }

   public int getKind() {
      return this.theKind;
   }

   public void setKind(int kind) {
      this.theKind = kind;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "kind", 0));
      }

   }

   public Expression getDefaultValue() {
      return this.theDefaultValue;
   }

   public void setDefaultValue(Expression defaultValue) {
      this.theDefaultValue = defaultValue;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "defaultValue", 0));
      }

   }

   public Event getEvent() {
      return this.theEvent;
   }

   public void setEvent(Event arg) {
      if (this.theEvent != arg) {
         Event temp = this.theEvent;
         this.theEvent = null;
         if (temp != null) {
            temp.removeParameter(this);
         }

         if (arg != null) {
            this.theEvent = arg;
            arg.addParameter(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "event", 0));
         }
      }

   }

   public Set getCollectionStateList() {
      return this.theStateList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theStateList);
   }

   public java.util.Enumeration getStateList() {
      return Collections.enumeration(this.getCollectionStateList());
   }

   public void addState(ObjectFlowState arg) {
      if (arg != null) {
         if (this.theStateList == null) {
            this.theStateList = new LinkedHashSet();
         }

         if (this.theStateList.add(arg)) {
            arg.addParameter(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "state", 1));
            }
         }
      }

   }

   public void removeState(ObjectFlowState arg) {
      if (this.theStateList != null && arg != null && this.theStateList.remove(arg)) {
         arg.removeParameter(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "state", 2));
         }
      }

   }

   public Classifier getType() {
      return this.theType;
   }

   public void setType(Classifier arg) {
      if (this.theType != arg) {
         Classifier temp = this.theType;
         this.theType = null;
         if (temp != null) {
            temp.removeTypedParameter(this);
         }

         if (arg != null) {
            this.theType = arg;
            arg.addTypedParameter(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "type", 0));
         }
      }

   }

   public BehavioralFeature getBehavioralFeature() {
      return this.theBehavioralFeature;
   }

   public void setBehavioralFeature(BehavioralFeature arg) {
      if (this.theBehavioralFeature != arg) {
         BehavioralFeature temp = this.theBehavioralFeature;
         this.theBehavioralFeature = null;
         if (temp != null) {
            temp.removeParameter(this);
         }

         if (arg != null) {
            this.theBehavioralFeature = arg;
            arg.addParameter(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "behavioralFeature", 0));
         }
      }

   }

   protected void internalRemove() {
      Event tmpEvent = this.getEvent();
      if (tmpEvent != null) {
         tmpEvent.removeParameter(this);
      }

      java.util.Enumeration tmpStateEnum = this.getStateList();
      ArrayList tmpStateList = new ArrayList();

      while(tmpStateEnum.hasMoreElements()) {
         tmpStateList.add(tmpStateEnum.nextElement());
      }

      Iterator it = tmpStateList.iterator();

      while(it.hasNext()) {
         ObjectFlowState tmpState = (ObjectFlowState)it.next();
         tmpState.removeParameter(this);
      }

      Classifier tmpType = this.getType();
      if (tmpType != null) {
         tmpType.removeTypedParameter(this);
      }

      BehavioralFeature tmpBehavioralFeature = this.getBehavioralFeature();
      if (tmpBehavioralFeature != null) {
         tmpBehavioralFeature.removeParameter(this);
      }

      super.internalRemove();
   }
}
