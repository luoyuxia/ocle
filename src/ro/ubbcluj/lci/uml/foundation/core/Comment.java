package ro.ubbcluj.lci.uml.foundation.core;

import java.util.Set;

public interface Comment extends ModelElement {
   String getBody();

   void setBody(String var1);

   java.util.Enumeration getAnnotatedElementList();

   Set getCollectionAnnotatedElementList();

   void addAnnotatedElement(ModelElement var1);

   void removeAnnotatedElement(ModelElement var1);
}
