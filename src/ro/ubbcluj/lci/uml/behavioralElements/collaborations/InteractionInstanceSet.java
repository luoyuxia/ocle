package ro.ubbcluj.lci.uml.behavioralElements.collaborations;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Stimulus;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public interface InteractionInstanceSet extends ModelElement {
   Enumeration getParticipatingStimulusList();

   Set getCollectionParticipatingStimulusList();

   void addParticipatingStimulus(Stimulus var1);

   void removeParticipatingStimulus(Stimulus var1);

   CollaborationInstanceSet getContext();

   void setContext(CollaborationInstanceSet var1);

   Interaction getInteraction();

   void setInteraction(Interaction var1);
}
