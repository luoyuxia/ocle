package ro.ubbcluj.lci.uml.foundation.core;

import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;

public interface Binding extends Dependency {
   java.util.Enumeration getArgumentList();

   OrderedSet getCollectionArgumentList();

   void addArgument(TemplateArgument var1);

   void removeArgument(TemplateArgument var1);
}
