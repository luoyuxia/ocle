package ro.ubbcluj.lci.uml.behavioralElements.commonBehavior;

import java.util.Enumeration;
import java.util.Set;

public interface ComponentInstance extends Instance {
   NodeInstance getNodeInstance();

   void setNodeInstance(NodeInstance var1);

   Enumeration getResidentList();

   Set getCollectionResidentList();

   void addResident(Instance var1);

   void removeResident(Instance var1);
}
