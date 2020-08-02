package ro.ubbcluj.lci.uml.behavioralElements.useCases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.RelationshipImpl;
import ro.ubbcluj.lci.uml.foundation.dataTypes.BooleanExpression;

public class ExtendImpl extends RelationshipImpl implements Extend {
   protected BooleanExpression theCondition;
   protected UseCase theBase;
   protected Set theExtensionPointList;
   protected UseCase theExtension;

   public ExtendImpl() {
   }

   public BooleanExpression getCondition() {
      return this.theCondition;
   }

   public void setCondition(BooleanExpression condition) {
      this.theCondition = condition;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "condition", 0));
      }

   }

   public UseCase getBase() {
      return this.theBase;
   }

   public void setBase(UseCase arg) {
      if (this.theBase != arg) {
         UseCase temp = this.theBase;
         this.theBase = null;
         if (temp != null) {
            temp.removeExtend(this);
         }

         if (arg != null) {
            this.theBase = arg;
            arg.addExtend(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "base", 0));
         }
      }

   }

   public Set getCollectionExtensionPointList() {
      return this.theExtensionPointList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theExtensionPointList);
   }

   public Enumeration getExtensionPointList() {
      return Collections.enumeration(this.getCollectionExtensionPointList());
   }

   public void addExtensionPoint(ExtensionPoint arg) {
      if (arg != null) {
         if (this.theExtensionPointList == null) {
            this.theExtensionPointList = new LinkedHashSet();
         }

         if (this.theExtensionPointList.add(arg)) {
            arg.addExtend(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "extensionPoint", 1));
            }
         }
      }

   }

   public void removeExtensionPoint(ExtensionPoint arg) {
      if (this.theExtensionPointList != null && arg != null && this.theExtensionPointList.remove(arg)) {
         arg.removeExtend(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "extensionPoint", 2));
         }
      }

   }

   public UseCase getExtension() {
      return this.theExtension;
   }

   public void setExtension(UseCase arg) {
      if (this.theExtension != arg) {
         UseCase temp = this.theExtension;
         this.theExtension = null;
         if (temp != null) {
            temp.removeExtender(this);
         }

         if (arg != null) {
            this.theExtension = arg;
            arg.addExtender(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "extension", 0));
         }
      }

   }

   protected void internalRemove() {
      UseCase tmpBase = this.getBase();
      if (tmpBase != null) {
         tmpBase.removeExtend(this);
      }

      Enumeration tmpExtensionPointEnum = this.getExtensionPointList();
      ArrayList tmpExtensionPointList = new ArrayList();

      while(tmpExtensionPointEnum.hasMoreElements()) {
         tmpExtensionPointList.add(tmpExtensionPointEnum.nextElement());
      }

      Iterator it = tmpExtensionPointList.iterator();

      while(it.hasNext()) {
         ExtensionPoint tmpExtensionPoint = (ExtensionPoint)it.next();
         tmpExtensionPoint.removeExtend(this);
      }

      UseCase tmpExtension = this.getExtension();
      if (tmpExtension != null) {
         tmpExtension.removeExtender(this);
      }

      super.internalRemove();
   }
}
