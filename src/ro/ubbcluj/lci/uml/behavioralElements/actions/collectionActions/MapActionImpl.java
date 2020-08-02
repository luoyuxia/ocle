package ro.ubbcluj.lci.uml.behavioralElements.actions.collectionActions;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.OutputPin;

public class MapActionImpl extends CollectionActionImpl implements MapAction {
   protected Set theSubinputList;
   protected Set theSuboutputList;

   public MapActionImpl() {
   }

   public Set getCollectionSubinputList() {
      return this.theSubinputList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theSubinputList);
   }

   public Enumeration getSubinputList() {
      return Collections.enumeration(this.getCollectionSubinputList());
   }

   public void addSubinput(OutputPin arg) {
      if (arg != null) {
         if (this.theSubinputList == null) {
            this.theSubinputList = new LinkedHashSet();
         }

         this.theSubinputList.add(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "subinput", 1));
         }
      }

   }

   public void removeSubinput(OutputPin arg) {
      if (this.theSubinputList != null && arg != null) {
         this.theSubinputList.remove(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "subinput", 2));
         }
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

   protected void internalRemove() {
      super.internalRemove();
   }
}
