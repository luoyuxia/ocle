package ro.ubbcluj.lci.uml.foundation.core;

import java.util.Set;
import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Signal;

public interface BehavioralFeature extends Feature {
   boolean isQuery();

   void setQuery(boolean var1);

   java.util.Enumeration getRaisedSignalList();

   Set getCollectionRaisedSignalList();

   void addRaisedSignal(Signal var1);

   void removeRaisedSignal(Signal var1);

   java.util.Enumeration getParameterList();

   OrderedSet getCollectionParameterList();

   void addParameter(Parameter var1);

   void removeParameter(Parameter var1);

   boolean hasSame_Param_Size(BehavioralFeature var1);

   boolean hasSameSignature(BehavioralFeature var1);

   boolean matchesSignature(BehavioralFeature var1);
}
