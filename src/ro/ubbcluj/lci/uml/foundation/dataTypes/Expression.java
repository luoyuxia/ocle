package ro.ubbcluj.lci.uml.foundation.dataTypes;

import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Procedure;
import ro.ubbcluj.lci.uml.foundation.core.Element;

public interface Expression extends Element {
   String getLanguage();

   void setLanguage(String var1);

   String getBody();

   void setBody(String var1);

   Procedure getProcedure();

   void setProcedure(Procedure var1);
}
