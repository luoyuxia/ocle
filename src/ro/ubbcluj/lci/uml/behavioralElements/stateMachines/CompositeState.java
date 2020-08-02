package ro.ubbcluj.lci.uml.behavioralElements.stateMachines;

import java.util.Enumeration;
import java.util.Set;

public interface CompositeState extends State {
   boolean isConcurrent();

   void setConcurrent(boolean var1);

   boolean isRegion();

   void setRegion(boolean var1);

   Enumeration getSubvertexList();

   Set getCollectionSubvertexList();

   void addSubvertex(StateVertex var1);

   void removeSubvertex(StateVertex var1);
}
