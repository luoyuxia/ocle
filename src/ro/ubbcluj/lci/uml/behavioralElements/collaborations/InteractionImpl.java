package ro.ubbcluj.lci.uml.behavioralElements.collaborations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;

public class InteractionImpl extends ModelElementImpl implements Interaction {
   protected Set theMessageList;
   protected Collaboration theContext;
   protected Set theInteractionInstanceSetList;

   public InteractionImpl() {
   }

   public Set getCollectionMessageList() {
      return this.theMessageList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theMessageList);
   }

   public Enumeration getMessageList() {
      return Collections.enumeration(this.getCollectionMessageList());
   }

   public void addMessage(Message arg) {
      if (arg != null) {
         if (this.theMessageList == null) {
            this.theMessageList = new LinkedHashSet();
         }

         if (this.theMessageList.add(arg)) {
            arg.setInteraction(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "message", 1));
            }
         }
      }

   }

   public void removeMessage(Message arg) {
      if (this.theMessageList != null && arg != null && this.theMessageList.remove(arg)) {
         arg.setInteraction((Interaction)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "message", 2));
         }
      }

   }

   public Collaboration getContext() {
      return this.theContext;
   }

   public void setContext(Collaboration arg) {
      if (this.theContext != arg) {
         Collaboration temp = this.theContext;
         this.theContext = null;
         if (temp != null) {
            temp.removeInteraction(this);
         }

         if (arg != null) {
            this.theContext = arg;
            arg.addInteraction(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "context", 0));
         }
      }

   }

   public Set getCollectionInteractionInstanceSetList() {
      return this.theInteractionInstanceSetList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theInteractionInstanceSetList);
   }

   public Enumeration getInteractionInstanceSetList() {
      return Collections.enumeration(this.getCollectionInteractionInstanceSetList());
   }

   public void addInteractionInstanceSet(InteractionInstanceSet arg) {
      if (arg != null) {
         if (this.theInteractionInstanceSetList == null) {
            this.theInteractionInstanceSetList = new LinkedHashSet();
         }

         if (this.theInteractionInstanceSetList.add(arg)) {
            arg.setInteraction(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "interactionInstanceSet", 1));
            }
         }
      }

   }

   public void removeInteractionInstanceSet(InteractionInstanceSet arg) {
      if (this.theInteractionInstanceSetList != null && arg != null && this.theInteractionInstanceSetList.remove(arg)) {
         arg.setInteraction((Interaction)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "interactionInstanceSet", 2));
         }
      }

   }

   protected void internalRemove() {
      Enumeration tmpMessageEnum = this.getMessageList();
      ArrayList tmpMessageList = new ArrayList();

      while(tmpMessageEnum.hasMoreElements()) {
         tmpMessageList.add(tmpMessageEnum.nextElement());
      }

      Iterator it = tmpMessageList.iterator();

      while(it.hasNext()) {
         ((Message)it.next()).remove();
      }

      Collaboration tmpContext = this.getContext();
      if (tmpContext != null) {
         tmpContext.removeInteraction(this);
      }

      Enumeration tmpInteractionInstanceSetEnum = this.getInteractionInstanceSetList();
      ArrayList tmpInteractionInstanceSetList = new ArrayList();

      while(tmpInteractionInstanceSetEnum.hasMoreElements()) {
         tmpInteractionInstanceSetList.add(tmpInteractionInstanceSetEnum.nextElement());
      }

       it = tmpInteractionInstanceSetList.iterator();

      while(it.hasNext()) {
         ((InteractionInstanceSet)it.next()).setInteraction((Interaction)null);
      }

      super.internalRemove();
   }
}
