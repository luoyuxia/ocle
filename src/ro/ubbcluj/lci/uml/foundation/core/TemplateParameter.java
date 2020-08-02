package ro.ubbcluj.lci.uml.foundation.core;

public interface TemplateParameter extends Element {
   ModelElement getDefaultElement();

   void setDefaultElement(ModelElement var1);

   ModelElement getTemplate();

   void setTemplate(ModelElement var1);

   ModelElement getParameterTemplate();

   void setParameterTemplate(ModelElement var1);
}
