package ro.ubbcluj.lci.uml.foundation.dataTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.ElementImpl;

public class MultiplicityImpl extends ElementImpl implements Multiplicity {
   protected Set theRangeList;

   public MultiplicityImpl() {
   }

   public Set getCollectionRangeList() {
      return this.theRangeList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theRangeList);
   }

   public Enumeration getRangeList() {
      return Collections.enumeration(this.getCollectionRangeList());
   }

   public void addRange(MultiplicityRange arg) {
      if (arg != null) {
         if (this.theRangeList == null) {
            this.theRangeList = new LinkedHashSet();
         }

         if (this.theRangeList.add(arg)) {
            arg.setMultiplicity(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "range", 1));
            }
         }
      }

   }

   public void removeRange(MultiplicityRange arg) {
      if (this.theRangeList != null && arg != null && this.theRangeList.remove(arg)) {
         arg.setMultiplicity((Multiplicity)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "range", 2));
         }
      }

   }

   protected void internalRemove() {
      Enumeration tmpRangeEnum = this.getRangeList();
      ArrayList tmpRangeList = new ArrayList();

      while(tmpRangeEnum.hasMoreElements()) {
         tmpRangeList.add(tmpRangeEnum.nextElement());
      }

      Iterator it = tmpRangeList.iterator();

      while(it.hasNext()) {
         ((MultiplicityRange)it.next()).remove();
      }

      super.internalRemove();
   }
}
