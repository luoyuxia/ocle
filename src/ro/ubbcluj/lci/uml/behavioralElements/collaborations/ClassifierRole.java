package ro.ubbcluj.lci.uml.behavioralElements.collaborations;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Feature;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;

public interface ClassifierRole extends Classifier {
   Multiplicity getMultiplicity();

   void setMultiplicity(Multiplicity var1);

   Enumeration getConformingInstanceList();

   Set getCollectionConformingInstanceList();

   void addConformingInstance(Instance var1);

   void removeConformingInstance(Instance var1);

   Enumeration getAvailableContentsList();

   Set getCollectionAvailableContentsList();

   void addAvailableContents(ModelElement var1);

   void removeAvailableContents(ModelElement var1);

   Enumeration getMessageList();

   Set getCollectionMessageList();

   void addMessage(Message var1);

   void removeMessage(Message var1);

   Enumeration getAvailableFeatureList();

   Set getCollectionAvailableFeatureList();

   void addAvailableFeature(Feature var1);

   void removeAvailableFeature(Feature var1);

   Enumeration getMessage1List();

   Set getCollectionMessage1List();

   void addMessage1(Message var1);

   void removeMessage1(Message var1);

   Enumeration getBaseList();

   Set getCollectionBaseList();

   void addBase(Classifier var1);

   void removeBase(Classifier var1);

   Set allAvailableFeatures();

   Set allAvailableContents();
}
