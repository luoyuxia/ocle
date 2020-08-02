package ro.ubbcluj.lci.uml.foundation.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class ArtifactImpl extends ClassifierImpl implements Artifact {
   protected Set theImplementationLocationList;

   public ArtifactImpl() {
   }

   public Set getCollectionImplementationLocationList() {
      return this.theImplementationLocationList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theImplementationLocationList);
   }

   public java.util.Enumeration getImplementationLocationList() {
      return Collections.enumeration(this.getCollectionImplementationLocationList());
   }

   public void addImplementationLocation(Component arg) {
      if (arg != null) {
         if (this.theImplementationLocationList == null) {
            this.theImplementationLocationList = new LinkedHashSet();
         }

         if (this.theImplementationLocationList.add(arg)) {
            arg.addImplementation(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "implementationLocation", 1));
            }
         }
      }

   }

   public void removeImplementationLocation(Component arg) {
      if (this.theImplementationLocationList != null && arg != null && this.theImplementationLocationList.remove(arg)) {
         arg.removeImplementation(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "implementationLocation", 2));
         }
      }

   }

   protected void internalRemove() {
      java.util.Enumeration tmpImplementationLocationEnum = this.getImplementationLocationList();
      ArrayList tmpImplementationLocationList = new ArrayList();

      while(tmpImplementationLocationEnum.hasMoreElements()) {
         tmpImplementationLocationList.add(tmpImplementationLocationEnum.nextElement());
      }

      Iterator it = tmpImplementationLocationList.iterator();

      while(it.hasNext()) {
         Component tmpImplementationLocation = (Component)it.next();
         tmpImplementationLocation.removeImplementation(this);
      }

      super.internalRemove();
   }
}
