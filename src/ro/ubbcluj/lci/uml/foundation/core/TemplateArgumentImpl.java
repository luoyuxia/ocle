package ro.ubbcluj.lci.uml.foundation.core;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class TemplateArgumentImpl extends ElementImpl implements TemplateArgument {
   protected Binding theBinding;
   protected ModelElement theModelElement;

   public TemplateArgumentImpl() {
   }

   public Binding getBinding() {
      return this.theBinding;
   }

   public void setBinding(Binding arg) {
      if (this.theBinding != arg) {
         Binding temp = this.theBinding;
         this.theBinding = null;
         if (temp != null) {
            temp.removeArgument(this);
         }

         if (arg != null) {
            this.theBinding = arg;
            arg.addArgument(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "binding", 0));
         }
      }

   }

   public ModelElement getModelElement() {
      return this.theModelElement;
   }

   public void setModelElement(ModelElement arg) {
      if (this.theModelElement != arg) {
         ModelElement temp = this.theModelElement;
         this.theModelElement = null;
         if (temp != null) {
            temp.removeTemplateArgument(this);
         }

         if (arg != null) {
            this.theModelElement = arg;
            arg.addTemplateArgument(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "modelElement", 0));
         }
      }

   }

   protected void internalRemove() {
      Binding tmpBinding = this.getBinding();
      if (tmpBinding != null) {
         tmpBinding.removeArgument(this);
         if (tmpBinding.getCollectionArgumentList().size() < 1) {
            tmpBinding.remove();
         }
      }

      ModelElement tmpModelElement = this.getModelElement();
      if (tmpModelElement != null) {
         tmpModelElement.removeTemplateArgument(this);
      }

      super.internalRemove();
   }
}
