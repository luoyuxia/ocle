package ro.ubbcluj.lci.uml.behavioralElements.activityGraphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.StateMachineImpl;

public class ActivityGraphImpl extends StateMachineImpl implements ActivityGraph {
   protected Set thePartitionList;

   public ActivityGraphImpl() {
   }

   public Set getCollectionPartitionList() {
      return this.thePartitionList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.thePartitionList);
   }

   public Enumeration getPartitionList() {
      return Collections.enumeration(this.getCollectionPartitionList());
   }

   public void addPartition(Partition arg) {
      if (arg != null) {
         if (this.thePartitionList == null) {
            this.thePartitionList = new LinkedHashSet();
         }

         if (this.thePartitionList.add(arg)) {
            arg.setActivityGraph(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "partition", 1));
            }
         }
      }

   }

   public void removePartition(Partition arg) {
      if (this.thePartitionList != null && arg != null && this.thePartitionList.remove(arg)) {
         arg.setActivityGraph((ActivityGraph)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "partition", 2));
         }
      }

   }

   protected void internalRemove() {
      Enumeration tmpPartitionEnum = this.getPartitionList();
      ArrayList tmpPartitionList = new ArrayList();

      while(tmpPartitionEnum.hasMoreElements()) {
         tmpPartitionList.add(tmpPartitionEnum.nextElement());
      }

      Iterator it = tmpPartitionList.iterator();

      while(it.hasNext()) {
         ((Partition)it.next()).remove();
      }

      super.internalRemove();
   }
}
