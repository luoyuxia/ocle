package ro.ubbcluj.lci.uml.behavioralElements.collaborations;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Link;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public interface CollaborationInstanceSet extends ModelElement {
   Enumeration getInteractionInstanceList();

   Set getCollectionInteractionInstanceList();

   void addInteractionInstance(InteractionInstanceSet var1);

   void removeInteractionInstance(InteractionInstanceSet var1);

   Enumeration getParticipatingLinkList();

   Set getCollectionParticipatingLinkList();

   void addParticipatingLink(Link var1);

   void removeParticipatingLink(Link var1);

   Enumeration getConstrainingElementList();

   Set getCollectionConstrainingElementList();

   void addConstrainingElement(ModelElement var1);

   void removeConstrainingElement(ModelElement var1);

   Collaboration getCollaboration();

   void setCollaboration(Collaboration var1);

   Enumeration getParticipatingInstanceList();

   Set getCollectionParticipatingInstanceList();

   void addParticipatingInstance(Instance var1);

   void removeParticipatingInstance(Instance var1);
}
