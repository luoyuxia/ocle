package ro.ubbcluj.lci.uml.behavioralElements.activityGraphs;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.State;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;

public interface ClassifierInState extends Classifier {
   Classifier getType();

   void setType(Classifier var1);

   Enumeration getInStateList();

   Set getCollectionInStateList();

   void addInState(State var1);

   void removeInState(State var1);
}
