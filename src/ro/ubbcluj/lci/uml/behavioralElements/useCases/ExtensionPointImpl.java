package ro.ubbcluj.lci.uml.behavioralElements.useCases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;
import ro.ubbcluj.lci.uml.foundation.dataTypes.LocationReference;

public class ExtensionPointImpl extends ModelElementImpl implements ExtensionPoint {
   protected LocationReference theLocation;
   protected UseCase theUseCase;
   protected Set theExtendList;

   public ExtensionPointImpl() {
   }

   public LocationReference getLocation() {
      return this.theLocation;
   }

   public void setLocation(LocationReference location) {
      this.theLocation = location;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "location", 0));
      }

   }

   public UseCase getUseCase() {
      return this.theUseCase;
   }

   public void setUseCase(UseCase arg) {
      if (this.theUseCase != arg) {
         UseCase temp = this.theUseCase;
         this.theUseCase = null;
         if (temp != null) {
            temp.removeExtensionPoint(this);
         }

         if (arg != null) {
            this.theUseCase = arg;
            arg.addExtensionPoint(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "useCase", 0));
         }
      }

   }

   public Set getCollectionExtendList() {
      return this.theExtendList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theExtendList);
   }

   public Enumeration getExtendList() {
      return Collections.enumeration(this.getCollectionExtendList());
   }

   public void addExtend(Extend arg) {
      if (arg != null) {
         if (this.theExtendList == null) {
            this.theExtendList = new LinkedHashSet();
         }

         if (this.theExtendList.add(arg)) {
            arg.addExtensionPoint(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "extend", 1));
            }
         }
      }

   }

   public void removeExtend(Extend arg) {
      if (this.theExtendList != null && arg != null && this.theExtendList.remove(arg)) {
         arg.removeExtensionPoint(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "extend", 2));
         }
      }

   }

   protected void internalRemove() {
      UseCase tmpUseCase = this.getUseCase();
      if (tmpUseCase != null) {
         tmpUseCase.removeExtensionPoint(this);
      }

      Enumeration tmpExtendEnum = this.getExtendList();
      ArrayList tmpExtendList = new ArrayList();

      while(tmpExtendEnum.hasMoreElements()) {
         tmpExtendList.add(tmpExtendEnum.nextElement());
      }

      Iterator it = tmpExtendList.iterator();

      while(it.hasNext()) {
         Extend tmpExtend = (Extend)it.next();
         tmpExtend.removeExtensionPoint(this);
         if (tmpExtend.getCollectionExtensionPointList().size() < 1) {
            tmpExtend.remove();
         }
      }

      super.internalRemove();
   }
}
