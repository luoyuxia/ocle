package ro.ubbcluj.lci.uml.foundation.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;
import ro.ubbcluj.lci.codegen.framework.ocl.CollectionUtilities;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.dataTypes.BooleanExpression;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.Stereotype;

public class ConstraintImpl extends ModelElementImpl implements Constraint {
   protected BooleanExpression theBody;
   protected OrderedSet theConstrainedElementList;
   protected Stereotype theConstrainedStereotype;

   public ConstraintImpl() {
   }

   public BooleanExpression getBody() {
      return this.theBody;
   }

   public void setBody(BooleanExpression body) {
      this.theBody = body;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "body", 0));
      }

   }

   public OrderedSet getCollectionConstrainedElementList() {
      return this.theConstrainedElementList == null ? CollectionUtilities.newOrderedSet() : CollectionUtilities.newOrderedSet(this.theConstrainedElementList);
   }

   public java.util.Enumeration getConstrainedElementList() {
      return Collections.enumeration(this.getCollectionConstrainedElementList());
   }

   public void addConstrainedElement(ModelElement arg) {
      if (arg != null) {
         if (this.theConstrainedElementList == null) {
            this.theConstrainedElementList = new OrderedSet();
         }

         if (this.theConstrainedElementList.add(arg)) {
            arg.addConstraint(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "constrainedElement", 1));
            }
         }
      }

   }

   public void removeConstrainedElement(ModelElement arg) {
      if (this.theConstrainedElementList != null && arg != null && this.theConstrainedElementList.remove(arg)) {
         arg.removeConstraint(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "constrainedElement", 2));
         }
      }

   }

   public Stereotype getConstrainedStereotype() {
      return this.theConstrainedStereotype;
   }

   public void setConstrainedStereotype(Stereotype arg) {
      if (this.theConstrainedStereotype != arg) {
         Stereotype temp = this.theConstrainedStereotype;
         this.theConstrainedStereotype = null;
         if (temp != null) {
            temp.removeStereotypeConstraint(this);
         }

         if (arg != null) {
            this.theConstrainedStereotype = arg;
            arg.addStereotypeConstraint(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "constrainedStereotype", 0));
         }
      }

   }

   protected void internalRemove() {
      java.util.Enumeration tmpConstrainedElementEnum = this.getConstrainedElementList();
      ArrayList tmpConstrainedElementList = new ArrayList();

      while(tmpConstrainedElementEnum.hasMoreElements()) {
         tmpConstrainedElementList.add(tmpConstrainedElementEnum.nextElement());
      }

      Iterator it = tmpConstrainedElementList.iterator();

      while(it.hasNext()) {
         ModelElement tmpConstrainedElement = (ModelElement)it.next();
         tmpConstrainedElement.removeConstraint(this);
      }

      Stereotype tmpConstrainedStereotype = this.getConstrainedStereotype();
      if (tmpConstrainedStereotype != null) {
         tmpConstrainedStereotype.removeStereotypeConstraint(this);
      }

      super.internalRemove();
   }
}
