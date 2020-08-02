package ro.ubbcluj.lci.uml.behavioralElements.actions.collectionActions;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.OutputPin;

public class ReduceActionImpl extends CollectionActionImpl implements ReduceAction {
   protected boolean isUnordered;
   protected Set theSuboutputList;
   protected Set theLeftSubinputList;
   protected Set theRightSubinputList;

   public ReduceActionImpl() {
   }

   public boolean isUnordered() {
      return this.isUnordered;
   }

   public void setUnordered(boolean isUnordered) {
      this.isUnordered = isUnordered;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isUnordered", 0));
      }

   }

   public Set getCollectionSuboutputList() {
      return this.theSuboutputList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theSuboutputList);
   }

   public Enumeration getSuboutputList() {
      return Collections.enumeration(this.getCollectionSuboutputList());
   }

   public void addSuboutput(OutputPin arg) {
      if (arg != null) {
         if (this.theSuboutputList == null) {
            this.theSuboutputList = new LinkedHashSet();
         }

         this.theSuboutputList.add(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "suboutput", 1));
         }
      }

   }

   public void removeSuboutput(OutputPin arg) {
      if (this.theSuboutputList != null && arg != null) {
         this.theSuboutputList.remove(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "suboutput", 2));
         }
      }

   }

   public Set getCollectionLeftSubinputList() {
      return this.theLeftSubinputList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theLeftSubinputList);
   }

   public Enumeration getLeftSubinputList() {
      return Collections.enumeration(this.getCollectionLeftSubinputList());
   }

   public void addLeftSubinput(OutputPin arg) {
      if (arg != null) {
         if (this.theLeftSubinputList == null) {
            this.theLeftSubinputList = new LinkedHashSet();
         }

         this.theLeftSubinputList.add(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "leftSubinput", 1));
         }
      }

   }

   public void removeLeftSubinput(OutputPin arg) {
      if (this.theLeftSubinputList != null && arg != null) {
         this.theLeftSubinputList.remove(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "leftSubinput", 2));
         }
      }

   }

   public Set getCollectionRightSubinputList() {
      return this.theRightSubinputList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theRightSubinputList);
   }

   public Enumeration getRightSubinputList() {
      return Collections.enumeration(this.getCollectionRightSubinputList());
   }

   public void addRightSubinput(OutputPin arg) {
      if (arg != null) {
         if (this.theRightSubinputList == null) {
            this.theRightSubinputList = new LinkedHashSet();
         }

         this.theRightSubinputList.add(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "rightSubinput", 1));
         }
      }

   }

   public void removeRightSubinput(OutputPin arg) {
      if (this.theRightSubinputList != null && arg != null) {
         this.theRightSubinputList.remove(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "rightSubinput", 2));
         }
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
