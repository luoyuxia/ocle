package ro.ubbcluj.lci.uml.foundation.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class PresentationElementImpl extends ElementImpl implements PresentationElement {
   protected Set theSubjectList;

   public PresentationElementImpl() {
   }

   public Set getCollectionSubjectList() {
      return this.theSubjectList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theSubjectList);
   }

   public java.util.Enumeration getSubjectList() {
      return Collections.enumeration(this.getCollectionSubjectList());
   }

   public void addSubject(ModelElement arg) {
      if (arg != null) {
         if (this.theSubjectList == null) {
            this.theSubjectList = new LinkedHashSet();
         }

         if (this.theSubjectList.add(arg)) {
            arg.addPresentation(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "subject", 1));
            }
         }
      }

   }

   public void removeSubject(ModelElement arg) {
      if (this.theSubjectList != null && arg != null && this.theSubjectList.remove(arg)) {
         arg.removePresentation(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "subject", 2));
         }
      }

   }

   protected void internalRemove() {
      java.util.Enumeration tmpSubjectEnum = this.getSubjectList();
      ArrayList tmpSubjectList = new ArrayList();

      while(tmpSubjectEnum.hasMoreElements()) {
         tmpSubjectList.add(tmpSubjectEnum.nextElement());
      }

      Iterator it = tmpSubjectList.iterator();

      while(it.hasNext()) {
         ModelElement tmpSubject = (ModelElement)it.next();
         tmpSubject.removePresentation(this);
      }

      super.internalRemove();
   }
}
