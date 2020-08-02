package ro.ubbcluj.lci.uml.behavioralElements.commonBehavior;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.SignalEvent;
import ro.ubbcluj.lci.uml.foundation.core.BehavioralFeature;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;

public interface Signal extends Classifier {
   Enumeration getOccurenceList();

   Set getCollectionOccurenceList();

   void addOccurence(SignalEvent var1);

   void removeOccurence(SignalEvent var1);

   Enumeration getReceptionList();

   Set getCollectionReceptionList();

   void addReception(Reception var1);

   void removeReception(Reception var1);

   Enumeration getContextList();

   Set getCollectionContextList();

   void addContext(BehavioralFeature var1);

   void removeContext(BehavioralFeature var1);
}
