package ro.ubbcluj.lci.uml.behavioralElements.commonBehavior;

import java.util.Enumeration;
import java.util.Set;

public interface NodeInstance extends Instance {
   Enumeration getResidentList();

   Set getCollectionResidentList();

   void addResident(ComponentInstance var1);

   void removeResident(ComponentInstance var1);
}
