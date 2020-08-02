package ro.ubbcluj.lci.uml.behavioralElements.commonBehavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.SignalEvent;
import ro.ubbcluj.lci.uml.foundation.core.BehavioralFeature;
import ro.ubbcluj.lci.uml.foundation.core.ClassifierImpl;

public class SignalImpl extends ClassifierImpl implements Signal {
   protected Set theOccurenceList;
   protected Set theReceptionList;
   protected Set theContextList;

   public SignalImpl() {
   }

   public Set getCollectionOccurenceList() {
      return this.theOccurenceList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theOccurenceList);
   }

   public Enumeration getOccurenceList() {
      return Collections.enumeration(this.getCollectionOccurenceList());
   }

   public void addOccurence(SignalEvent arg) {
      if (arg != null) {
         if (this.theOccurenceList == null) {
            this.theOccurenceList = new LinkedHashSet();
         }

         if (this.theOccurenceList.add(arg)) {
            arg.setSignal(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "occurence", 1));
            }
         }
      }

   }

   public void removeOccurence(SignalEvent arg) {
      if (this.theOccurenceList != null && arg != null && this.theOccurenceList.remove(arg)) {
         arg.setSignal((Signal)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "occurence", 2));
         }
      }

   }

   public Set getCollectionReceptionList() {
      return this.theReceptionList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theReceptionList);
   }

   public Enumeration getReceptionList() {
      return Collections.enumeration(this.getCollectionReceptionList());
   }

   public void addReception(Reception arg) {
      if (arg != null) {
         if (this.theReceptionList == null) {
            this.theReceptionList = new LinkedHashSet();
         }

         if (this.theReceptionList.add(arg)) {
            arg.setSignal(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "reception", 1));
            }
         }
      }

   }

   public void removeReception(Reception arg) {
      if (this.theReceptionList != null && arg != null && this.theReceptionList.remove(arg)) {
         arg.setSignal((Signal)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "reception", 2));
         }
      }

   }

   public Set getCollectionContextList() {
      return this.theContextList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theContextList);
   }

   public Enumeration getContextList() {
      return Collections.enumeration(this.getCollectionContextList());
   }

   public void addContext(BehavioralFeature arg) {
      if (arg != null) {
         if (this.theContextList == null) {
            this.theContextList = new LinkedHashSet();
         }

         if (this.theContextList.add(arg)) {
            arg.addRaisedSignal(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "context", 1));
            }
         }
      }

   }

   public void removeContext(BehavioralFeature arg) {
      if (this.theContextList != null && arg != null && this.theContextList.remove(arg)) {
         arg.removeRaisedSignal(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "context", 2));
         }
      }

   }

   protected void internalRemove() {
      Enumeration tmpOccurenceEnum = this.getOccurenceList();
      ArrayList tmpOccurenceList = new ArrayList();

      while(tmpOccurenceEnum.hasMoreElements()) {
         tmpOccurenceList.add(tmpOccurenceEnum.nextElement());
      }

      Iterator it = tmpOccurenceList.iterator();

      while(it.hasNext()) {
         ((SignalEvent)it.next()).setSignal((Signal)null);
      }

      Enumeration tmpReceptionEnum = this.getReceptionList();
      ArrayList tmpReceptionList = new ArrayList();

      while(tmpReceptionEnum.hasMoreElements()) {
         tmpReceptionList.add(tmpReceptionEnum.nextElement());
      }

       it = tmpReceptionList.iterator();

      while(it.hasNext()) {
         ((Reception)it.next()).setSignal((Signal)null);
      }

      Enumeration tmpContextEnum = this.getContextList();
      ArrayList tmpContextList = new ArrayList();

      while(tmpContextEnum.hasMoreElements()) {
         tmpContextList.add(tmpContextEnum.nextElement());
      }

       it = tmpContextList.iterator();

      while(it.hasNext()) {
         BehavioralFeature tmpContext = (BehavioralFeature)it.next();
         tmpContext.removeRaisedSignal(this);
      }

      super.internalRemove();
   }
}
