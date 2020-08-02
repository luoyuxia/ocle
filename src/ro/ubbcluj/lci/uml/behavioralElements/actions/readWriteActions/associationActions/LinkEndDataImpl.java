package ro.ubbcluj.lci.uml.behavioralElements.actions.readWriteActions.associationActions;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.ElementImpl;

public class LinkEndDataImpl extends ElementImpl implements LinkEndData {
   protected Set theQualifierList;
   protected AssociationEnd theEnd;

   public LinkEndDataImpl() {
   }

   public Set getCollectionQualifierList() {
      return this.theQualifierList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theQualifierList);
   }

   public Enumeration getQualifierList() {
      return Collections.enumeration(this.getCollectionQualifierList());
   }

   public void addQualifier(QualifierValue arg) {
      if (arg != null) {
         if (this.theQualifierList == null) {
            this.theQualifierList = new LinkedHashSet();
         }

         this.theQualifierList.add(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "qualifier", 1));
         }
      }

   }

   public void removeQualifier(QualifierValue arg) {
      if (this.theQualifierList != null && arg != null) {
         this.theQualifierList.remove(arg);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "qualifier", 2));
         }
      }

   }

   public AssociationEnd getEnd() {
      return this.theEnd;
   }

   public void setEnd(AssociationEnd arg) {
      this.theEnd = arg;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "end", 0));
      }

   }

   protected void internalRemove() {
      super.internalRemove();
   }
}
