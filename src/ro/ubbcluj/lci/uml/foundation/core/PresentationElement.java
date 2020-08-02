package ro.ubbcluj.lci.uml.foundation.core;

import java.util.Set;

public interface PresentationElement extends Element {
   java.util.Enumeration getSubjectList();

   Set getCollectionSubjectList();

   void addSubject(ModelElement var1);

   void removeSubject(ModelElement var1);
}
