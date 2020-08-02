package ro.ubbcluj.lci.uml.foundation.core;

import ro.ubbcluj.lci.uml.foundation.dataTypes.MappingExpression;

public interface Abstraction extends Dependency {
   MappingExpression getMapping();

   void setMapping(MappingExpression var1);
}
