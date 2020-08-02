package ro.ubbcluj.lci.uml.foundation.extensionMechanisms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;

public class TaggedValueImpl extends ModelElementImpl implements TaggedValue {
   protected String theDataValue;
   protected ModelElement theModelElement;
   protected Set theReferenceValueList;
   protected TagDefinition theType;

   public TaggedValueImpl() {
   }

   public String getDataValue() {
      return this.theDataValue;
   }

   public void setDataValue(String dataValue) {
      this.theDataValue = dataValue;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "dataValue", 0));
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
            temp.removeTaggedValue(this);
         }

         if (arg != null) {
            this.theModelElement = arg;
            arg.addTaggedValue(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "modelElement", 0));
         }
      }

   }

   public Set getCollectionReferenceValueList() {
      return this.theReferenceValueList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theReferenceValueList);
   }

   public Enumeration getReferenceValueList() {
      return Collections.enumeration(this.getCollectionReferenceValueList());
   }

   public void addReferenceValue(ModelElement arg) {
      if (arg != null) {
         if (this.theReferenceValueList == null) {
            this.theReferenceValueList = new LinkedHashSet();
         }

         if (this.theReferenceValueList.add(arg)) {
            arg.addReferenceTag(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "referenceValue", 1));
            }
         }
      }

   }

   public void removeReferenceValue(ModelElement arg) {
      if (this.theReferenceValueList != null && arg != null && this.theReferenceValueList.remove(arg)) {
         arg.removeReferenceTag(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "referenceValue", 2));
         }
      }

   }

   public TagDefinition getType() {
      return this.theType;
   }

   public void setType(TagDefinition arg) {
      if (this.theType != arg) {
         TagDefinition temp = this.theType;
         this.theType = null;
         if (temp != null) {
            temp.removeTypedValue(this);
         }

         if (arg != null) {
            this.theType = arg;
            arg.addTypedValue(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "type", 0));
         }
      }

   }

   protected void internalRemove() {
      ModelElement tmpModelElement = this.getModelElement();
      if (tmpModelElement != null) {
         tmpModelElement.removeTaggedValue(this);
      }

      Enumeration tmpReferenceValueEnum = this.getReferenceValueList();
      ArrayList tmpReferenceValueList = new ArrayList();

      while(tmpReferenceValueEnum.hasMoreElements()) {
         tmpReferenceValueList.add(tmpReferenceValueEnum.nextElement());
      }

      Iterator it = tmpReferenceValueList.iterator();

      while(it.hasNext()) {
         ModelElement tmpReferenceValue = (ModelElement)it.next();
         tmpReferenceValue.removeReferenceTag(this);
      }

      TagDefinition tmpType = this.getType();
      if (tmpType != null) {
         tmpType.removeTypedValue(this);
      }

      super.internalRemove();
   }
}
