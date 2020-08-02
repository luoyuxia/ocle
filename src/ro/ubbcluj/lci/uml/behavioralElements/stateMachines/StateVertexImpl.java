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

public class StateVertexImpl extends ModelElementImpl implements StateVertex {
   protected Set theOutgoingList;
   protected CompositeState theContainer;
   protected Set theIncomingList;

   public StateVertexImpl() {
   }

   public Set getCollectionOutgoingList() {
      return this.theOutgoingList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theOutgoingList);
   }

   public Enumeration getOutgoingList() {
      return Collections.enumeration(this.getCollectionOutgoingList());
   }

   public void addOutgoing(Transition arg) {
      if (arg != null) {
         if (this.theOutgoingList == null) {
            this.theOutgoingList = new LinkedHashSet();
         }

         if (this.theOutgoingList.add(arg)) {
            arg.setSource(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "outgoing", 1));
            }
         }
      }

   }

   public void removeOutgoing(Transition arg) {
      if (this.theOutgoingList != null && arg != null && this.theOutgoingList.remove(arg)) {
         arg.setSource((StateVertex)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "outgoing", 2));
         }
      }

   }

   public CompositeState getContainer() {
      return this.theContainer;
   }

   public void setContainer(CompositeState arg) {
      if (this.theContainer != arg) {
         CompositeState temp = this.theContainer;
         this.theContainer = null;
         if (temp != null) {
            temp.removeSubvertex(this);
         }

         if (arg != null) {
            this.theContainer = arg;
            arg.addSubvertex(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "container", 0));
         }
      }

   }

   public Set getCollectionIncomingList() {
      return this.theIncomingList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theIncomingList);
   }

   public Enumeration getIncomingList() {
      return Collections.enumeration(this.getCollectionIncomingList());
   }

   public void addIncoming(Transition arg) {
      if (arg != null) {
         if (this.theIncomingList == null) {
            this.theIncomingList = new LinkedHashSet();
         }

         if (this.theIncomingList.add(arg)) {
            arg.setTarget(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "incoming", 1));
            }
         }
      }

   }

   public void removeIncoming(Transition arg) {
      if (this.theIncomingList != null && arg != null && this.theIncomingList.remove(arg)) {
         arg.setTarget((StateVertex)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "incoming", 2));
         }
      }

   }

   protected void internalRemove() {
      Enumeration tmpOutgoingEnum = this.getOutgoingList();
      ArrayList tmpOutgoingList = new ArrayList();

      while(tmpOutgoingEnum.hasMoreElements()) {
         tmpOutgoingList.add(tmpOutgoingEnum.nextElement());
      }

      Iterator it = tmpOutgoingList.iterator();

      while(it.hasNext()) {
         ((Transition)it.next()).setSource((StateVertex)null);
      }

      CompositeState tmpContainer = this.getContainer();
      if (tmpContainer != null) {
         tmpContainer.removeSubvertex(this);
      }

      Enumeration tmpIncomingEnum = this.getIncomingList();
      ArrayList tmpIncomingList = new ArrayList();

      while(tmpIncomingEnum.hasMoreElements()) {
         tmpIncomingList.add(tmpIncomingEnum.nextElement());
      }

       it = tmpIncomingList.iterator();

      while(it.hasNext()) {
         ((Transition)it.next()).setTarget((StateVertex)null);
      }

      super.internalRemove();
   }
}
