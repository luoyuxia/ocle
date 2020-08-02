package ro.ubbcluj.lci.uml.behavioralElements.activityGraphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;

public class PartitionImpl extends ModelElementImpl implements Partition {
   protected Set theContentsList;
   protected ActivityGraph theActivityGraph;

   public PartitionImpl() {
   }

   public Set getCollectionContentsList() {
      return this.theContentsList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theContentsList);
   }

   public Enumeration getContentsList() {
      return Collections.enumeration(this.getCollectionContentsList());
   }

   public void addContents(ModelElement arg) {
      if (arg != null) {
         if (this.theContentsList == null) {
            this.theContentsList = new LinkedHashSet();
         }

         if (this.theContentsList.add(arg)) {
            arg.addPartition(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "contents", 1));
            }
         }
      }

   }

   public void removeContents(ModelElement arg) {
      if (this.theContentsList != null && arg != null && this.theContentsList.remove(arg)) {
         arg.removePartition(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "contents", 2));
         }
      }

   }

   public ActivityGraph getActivityGraph() {
      return this.theActivityGraph;
   }

   public void setActivityGraph(ActivityGraph arg) {
      if (this.theActivityGraph != arg) {
         ActivityGraph temp = this.theActivityGraph;
         this.theActivityGraph = null;
         if (temp != null) {
            temp.removePartition(this);
         }

         if (arg != null) {
            this.theActivityGraph = arg;
            arg.addPartition(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "activityGraph", 0));
         }
      }

   }

   protected void internalRemove() {
      Enumeration tmpContentsEnum = this.getContentsList();
      ArrayList tmpContentsList = new ArrayList();

      while(tmpContentsEnum.hasMoreElements()) {
         tmpContentsList.add(tmpContentsEnum.nextElement());
      }

      Iterator it = tmpContentsList.iterator();

      while(it.hasNext()) {
         ModelElement tmpContents = (ModelElement)it.next();
         tmpContents.removePartition(this);
      }

      ActivityGraph tmpActivityGraph = this.getActivityGraph();
      if (tmpActivityGraph != null) {
         tmpActivityGraph.removePartition(this);
      }

      super.internalRemove();
   }
}
