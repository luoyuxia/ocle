package ro.ubbcluj.lci.uml.behavioralElements.activityGraphs;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public interface Partition extends ModelElement {
   Enumeration getContentsList();

   Set getCollectionContentsList();

   void addContents(ModelElement var1);

   void removeContents(ModelElement var1);

   ActivityGraph getActivityGraph();

   void setActivityGraph(ActivityGraph var1);
}
