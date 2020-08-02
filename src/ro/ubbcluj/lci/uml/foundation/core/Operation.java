package ro.ubbcluj.lci.uml.foundation.core;

import java.util.Set;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Collaboration;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.CallEvent;

public interface Operation extends BehavioralFeature {
   int getConcurrency();

   void setConcurrency(int var1);

   boolean isRoot();

   void setRoot(boolean var1);

   boolean isLeaf();

   void setLeaf(boolean var1);

   boolean isAbstract();

   void setAbstract(boolean var1);

   String getSpecification();

   void setSpecification(String var1);

   java.util.Enumeration getMethodList();

   Set getCollectionMethodList();

   void addMethod(Method var1);

   void removeMethod(Method var1);

   java.util.Enumeration getCollaborationList();

   Set getCollectionCollaborationList();

   void addCollaboration(Collaboration var1);

   void removeCollaboration(Collaboration var1);

   java.util.Enumeration getOccurenceList();

   Set getCollectionOccurenceList();

   void addOccurence(CallEvent var1);

   void removeOccurence(CallEvent var1);
}
