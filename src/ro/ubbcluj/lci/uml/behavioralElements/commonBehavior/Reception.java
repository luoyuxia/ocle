package ro.ubbcluj.lci.uml.behavioralElements.commonBehavior;

import ro.ubbcluj.lci.uml.foundation.core.BehavioralFeature;

public interface Reception extends BehavioralFeature {
   String getSpecification();

   void setSpecification(String var1);

   boolean isRoot();

   void setRoot(boolean var1);

   boolean isLeaf();

   void setLeaf(boolean var1);

   boolean isAbstract();

   void setAbstract(boolean var1);

   Signal getSignal();

   void setSignal(Signal var1);
}
