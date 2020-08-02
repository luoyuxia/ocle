package ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions;

import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.PrimitiveAction;

public interface CodeAction extends PrimitiveAction {
   String getLanguage();

   void setLanguage(String var1);

   String getEncoding();

   void setEncoding(String var1);
}
