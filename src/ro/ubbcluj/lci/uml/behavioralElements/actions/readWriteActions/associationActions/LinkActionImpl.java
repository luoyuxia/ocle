package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveActionImpl;

public class LinkActionImpl extends PrimitiveActionImpl implements LinkAction {
   protected Set theEndDataList;

   public LinkActionImpl() {
   }

   public Set getCollectionEndDataList() {
      return this.theEndDataList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theEndDataList);
   }

   public Enumeration getEndDataList() {
      return Collections.enumeration(this.getCollectionEndDataList());
   }

   public void addEndData(LinkEndData arg) {
      if (arg != null) {
         if (this.theEndDataList == null) {
            this.theEndDataList = new LinkedHashSet();
         }

         this.theEndDataList.add(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "endData", 1));
         }
      }

   }

   public void removeEndData(LinkEndData arg) {
      if (this.theEndDataList != null && arg != null) {
         this.theEndDataList.remove(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "endData", 2));
         }
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
