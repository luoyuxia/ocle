package ro.ubbcluj.lci.uml.foundation.core;

import ro.ubbcluj.lci.uml.foundation.dataTypes.TypeExpression;

public interface ProgrammingLanguageDataType extends DataType {
   TypeExpression getExpression();

   void setExpression(TypeExpression var1);
}
