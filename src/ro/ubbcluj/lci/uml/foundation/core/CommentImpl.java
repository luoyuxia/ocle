package ro.ubbcluj.lci.uml.foundation.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class CommentImpl extends ModelElementImpl implements Comment {
   protected String theBody;
   protected Set theAnnotatedElementList;

   public CommentImpl() {
   }

   public String getBody() {
      return this.theBody;
   }

   public void setBody(String body) {
      this.theBody = body;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "body", 0));
      }

   }

   public Set getCollectionAnnotatedElementList() {
      return this.theAnnotatedElementList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theAnnotatedElementList);
   }

   public java.util.Enumeration getAnnotatedElementList() {
      return Collections.enumeration(this.getCollectionAnnotatedElementList());
   }

   public void addAnnotatedElement(ModelElement arg) {
      if (arg != null) {
         if (this.theAnnotatedElementList == null) {
            this.theAnnotatedElementList = new LinkedHashSet();
         }

         if (this.theAnnotatedElementList.add(arg)) {
            arg.addComment(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "annotatedElement", 1));
            }
         }
      }

   }

   public void removeAnnotatedElement(ModelElement arg) {
      if (this.theAnnotatedElementList != null && arg != null && this.theAnnotatedElementList.remove(arg)) {
         arg.removeComment(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "annotatedElement", 2));
         }
      }

   }

   protected void internalRemove() {
      java.util.Enumeration tmpAnnotatedElementEnum = this.getAnnotatedElementList();
      ArrayList tmpAnnotatedElementList = new ArrayList();

      while(tmpAnnotatedElementEnum.hasMoreElements()) {
         tmpAnnotatedElementList.add(tmpAnnotatedElementEnum.nextElement());
      }

      Iterator it = tmpAnnotatedElementList.iterator();

      while(it.hasNext()) {
         ModelElement tmpAnnotatedElement = (ModelElement)it.next();
         tmpAnnotatedElement.removeComment(this);
      }

      super.internalRemove();
   }
}
