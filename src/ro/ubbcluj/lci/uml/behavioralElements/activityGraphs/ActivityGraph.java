package ro.ubbcluj.lci.uml.behavioralElements.activityGraphs;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.StateMachine;

public interface ActivityGraph extends StateMachine {
   Enumeration getPartitionList();

   Set getCollectionPartitionList();

   void addPartition(Partition var1);

   void removePartition(Partition var1);
}
