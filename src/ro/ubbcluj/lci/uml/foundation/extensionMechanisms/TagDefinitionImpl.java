package ro.ubbcluj.lci.uml.foundation.extensionMechanisms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;

public class TagDefinitionImpl extends ModelElementImpl implements TagDefinition {
   protected String theTagType;
   protected Multiplicity theMultiplicity;
   protected Stereotype theOwner;
   protected Set theTypedValueList;

   public TagDefinitionImpl() {
   }

   public String getTagType() {
      return this.theTagType;
   }

   public void setTagType(String tagType) {
      this.theTagType = tagType;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "tagType", 0));
      }

   }

   public Multiplicity getMultiplicity() {
      return this.theMultiplicity;
   }

   public void setMultiplicity(Multiplicity multiplicity) {
      this.theMultiplicity = multiplicity;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "multiplicity", 0));
      }

   }

   public Stereotype getOwner() {
      return this.theOwner;
   }

   public void setOwner(Stereotype arg) {
      if (this.theOwner != arg) {
         Stereotype temp = this.theOwner;
         this.theOwner = null;
         if (temp != null) {
            temp.removeDefinedTag(this);
         }

         if (arg != null) {
            this.theOwner = arg;
            arg.addDefinedTag(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "owner", 0));
         }
      }

   }

   public Set getCollectionTypedValueList() {
      return this.theTypedValueList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theTypedValueList);
   }

   public Enumeration getTypedValueList() {
      return Collections.enumeration(this.getCollectionTypedValueList());
   }

   public void addTypedValue(TaggedValue arg) {
      if (arg != null) {
         if (this.theTypedValueList == null) {
            this.theTypedValueList = new LinkedHashSet();
         }

         if (this.theTypedValueList.add(arg)) {
            arg.setType(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "typedValue", 1));
            }
         }
      }

   }

   public void removeTypedValue(TaggedValue arg) {
      if (this.theTypedValueList != null && arg != null && this.theTypedValueList.remove(arg)) {
         arg.setType((TagDefinition)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "typedValue", 2));
         }
      }

   }

   protected void internalRemove() {
      Stereotype tmpOwner = this.getOwner();
      if (tmpOwner != null) {
         tmpOwner.removeDefinedTag(this);
      }

      Enumeration tmpTypedValueEnum = this.getTypedValueList();
      ArrayList tmpTypedValueList = new ArrayList();

      while(tmpTypedValueEnum.hasMoreElements()) {
         tmpTypedValueList.add(tmpTypedValueEnum.nextElement());
      }

      Iterator it = tmpTypedValueList.iterator();

      while(it.hasNext()) {
         ((TaggedValue)it.next()).setType((TagDefinition)null);
      }

      super.internalRemove();
   }
}
