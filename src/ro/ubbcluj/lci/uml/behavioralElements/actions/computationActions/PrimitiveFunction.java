package ro.ubbcluj.lci.uml.behavioralElements.actions.computationActions;

import java.util.Enumeration;
import java.util.Set;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public interface PrimitiveFunction extends ModelElement {
   String getLanguage();

   void setLanguage(String var1);

   String getEncoding();

   void setEncoding(String var1);

   Enumeration getOutputSpecList();

   Set getCollectionOutputSpecList();

   void addOutputSpec(ArgumentSpecification var1);

   void removeOutputSpec(ArgumentSpecification var1);

   Enumeration getInputSpecList();

   Set getCollectionInputSpecList();

   void addInputSpec(ArgumentSpecification var1);

   void removeInputSpec(ArgumentSpecification var1);
}
