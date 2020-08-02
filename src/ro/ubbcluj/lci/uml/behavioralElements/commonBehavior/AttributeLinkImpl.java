package ro.ubbcluj.lci.uml.behavioralElements.commonBehavior;

import java.util.Enumeration;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;
import ro.ubbcluj.lci.utils.ModelFactory;

public class AttributeLinkImpl extends ModelElementImpl implements AttributeLink {
   protected Instance theValue;
   protected Attribute theAttribute;
   protected Instance theInstance;
   protected LinkEnd theLinkEnd;

   public AttributeLinkImpl() {
   }

   public Instance getValue() {
      return this.theValue;
   }

   public void setValue(Instance arg) {
      if (this.theValue != arg) {
         Instance temp = this.theValue;
         this.theValue = null;
         if (temp != null) {
            temp.removeAttributeLink(this);
         }

         if (arg != null) {
            this.theValue = arg;
            arg.addAttributeLink(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "value", 0));
         }
      }

   }

   public Attribute getAttribute() {
      return this.theAttribute;
   }

   public void setAttribute(Attribute arg) {
      if (this.theAttribute != arg) {
         Attribute temp = this.theAttribute;
         this.theAttribute = null;
         if (temp != null) {
            temp.removeAttributeLink(this);
         }

         if (arg != null) {
            this.theAttribute = arg;
            arg.addAttributeLink(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "attribute", 0));
         }
      }

   }

   public Instance getInstance() {
      return this.theInstance;
   }

   public void setInstance(Instance arg) {
      if (this.theInstance != arg) {
         Instance temp = this.theInstance;
         this.theInstance = null;
         if (temp != null) {
            temp.removeSlot(this);
         }

         if (arg != null) {
            this.theInstance = arg;
            arg.addSlot(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "instance", 0));
         }
      }

   }

   public LinkEnd getLinkEnd() {
      return this.theLinkEnd;
   }

   public void setLinkEnd(LinkEnd arg) {
      if (this.theLinkEnd != arg) {
         LinkEnd temp = this.theLinkEnd;
         this.theLinkEnd = null;
         if (temp != null) {
            temp.removeQualifierValue(this);
         }

         if (arg != null) {
            this.theLinkEnd = arg;
            arg.addQualifierValue(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "linkEnd", 0));
         }
      }

   }

   protected void internalRemove() {
      Instance tmpValue = this.getValue();
      if (tmpValue != null) {
         tmpValue.removeAttributeLink(this);
         Enumeration classifiers = tmpValue.getClassifierList();
         Classifier classifier = null;
         if (classifiers.hasMoreElements()) {
            classifier = (Classifier)classifiers.nextElement();
         }

         if (!tmpValue.getAttributeLinkList().hasMoreElements() && !tmpValue.getLinkEndList().hasMoreElements()) {
            ModelFactory.removeDataValue(tmpValue.getName(), classifier);
            tmpValue.remove();
         }
      }

      Attribute tmpAttribute = this.getAttribute();
      if (tmpAttribute != null) {
         tmpAttribute.removeAttributeLink(this);
      }

      Instance tmpInstance = this.getInstance();
      if (tmpInstance != null) {
         tmpInstance.removeSlot(this);
      }

      LinkEnd tmpLinkEnd = this.getLinkEnd();
      if (tmpLinkEnd != null) {
         tmpLinkEnd.removeQualifierValue(this);
      }

      super.internalRemove();
   }
}
