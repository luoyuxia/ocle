package ro.ubbcluj.lci.uml.foundation.core;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class TemplateParameterImpl extends ElementImpl implements TemplateParameter {
   protected ModelElement theDefaultElement;
   protected ModelElement theTemplate;
   protected ModelElement theParameterTemplate;

   public TemplateParameterImpl() {
   }

   public ModelElement getDefaultElement() {
      return this.theDefaultElement;
   }

   public void setDefaultElement(ModelElement arg) {
      if (this.theDefaultElement != arg) {
         ModelElement temp = this.theDefaultElement;
         this.theDefaultElement = null;
         if (temp != null) {
            temp.removeDefaultedParameter(this);
         }

         if (arg != null) {
            this.theDefaultElement = arg;
            arg.addDefaultedParameter(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "defaultElement", 0));
         }
      }

   }

   public ModelElement getTemplate() {
      return this.theTemplate;
   }

   public void setTemplate(ModelElement arg) {
      if (this.theTemplate != arg) {
         ModelElement temp = this.theTemplate;
         this.theTemplate = null;
         if (temp != null) {
            temp.removeParameterTemplate(this);
         }

         if (arg != null) {
            this.theTemplate = arg;
            arg.addParameterTemplate(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "template", 0));
         }
      }

   }

   public ModelElement getParameterTemplate() {
      return this.theParameterTemplate;
   }

   public void setParameterTemplate(ModelElement arg) {
      if (this.theParameterTemplate != arg) {
         ModelElement temp = this.theParameterTemplate;
         this.theParameterTemplate = null;
         if (temp != null) {
            temp.setTemplate((TemplateParameter)null);
         }

         if (arg != null) {
            this.theParameterTemplate = arg;
            arg.setTemplate(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "parameterTemplate", 0));
         }
      }

   }

   protected void internalRemove() {
      ModelElement tmpDefaultElement = this.getDefaultElement();
      if (tmpDefaultElement != null) {
         tmpDefaultElement.removeDefaultedParameter(this);
      }

      super.internalRemove();
   }
}
