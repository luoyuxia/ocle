package ro.ubbcluj.lci.utils.uml;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import ro.ubbcluj.lci.ocl.eval.OclDirect;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.ActivityGraph;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.AssociationEndRole;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.AssociationRole;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.ClassifierRole;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Collaboration;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Interaction;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Link;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkEnd;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.CompositeState;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.Event;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.State;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.StateMachine;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.Transition;
import ro.ubbcluj.lci.uml.foundation.core.Abstraction;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.BehavioralFeature;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Component;
import ro.ubbcluj.lci.uml.foundation.core.Constraint;
import ro.ubbcluj.lci.uml.foundation.core.Element;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;
import ro.ubbcluj.lci.uml.foundation.core.Parameter;
import ro.ubbcluj.lci.uml.foundation.core.StructuralFeature;
import ro.ubbcluj.lci.uml.foundation.core.TemplateParameter;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.Stereotype;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.TaggedValue;
import ro.ubbcluj.lci.uml.modelManagement.Model;

public class UMLNavigationUtilities {
   private List results = null;

   public UMLNavigationUtilities() {
   }

   public void navigate(Object entryObject, UMLActionListener ap) {
      this.results = new ArrayList();
      this.rec(entryObject);
      Iterator it = this.results.iterator();

      while(it.hasNext()) {
         Object o = it.next();
         ap.actionPerform((Element)o);
      }

      this.results = null;
   }

   private void rec(Object elem) {
      if (elem != null) {
         this.results.add(elem);
         if (elem instanceof Association) {
            this.recEnum(((Association)elem).getConnectionList(), elem);
         }

         if (elem instanceof Classifier) {
            this.recEnum(((Classifier)elem).getFeatureList(), elem);
         }

         if (elem instanceof Classifier) {
            this.recEnum(((Classifier)elem).getInstanceList(), elem);
         }

         if (elem instanceof Namespace) {
            this.recEnum((new OclDirect((Namespace)elem)).directGetOwnedElementList(), elem);
         }

         if (elem instanceof ModelElement) {
            this.rec(((ModelElement)elem).getNamespace());
         }

         if (elem instanceof BehavioralFeature) {
            this.recEnum(((BehavioralFeature)elem).getParameterList(), elem);
         }

         if (elem instanceof AssociationEnd) {
            this.recEnum(((AssociationEnd)elem).getQualifierList(), elem);
         }

         if (elem instanceof ModelElement) {
            this.recEnum(((ModelElement)elem).getParameterTemplateList(), elem);
         }

         if (elem instanceof TemplateParameter) {
            this.rec(((TemplateParameter)elem).getParameterTemplate());
         }

         if (elem instanceof Multiplicity) {
            this.recEnum(((Multiplicity)elem).getRangeList(), elem);
         }

         if (elem instanceof Stereotype) {
            this.recEnum(((Stereotype)elem).getDefinedTagList(), elem);
         }

         if (elem instanceof Stereotype) {
            this.recEnum(((Stereotype)elem).getStereotypeConstraintList(), elem);
         }

         if (elem instanceof ModelElement) {
            this.recEnum(((ModelElement)elem).getTaggedValueList(), elem);
         }

         if (elem instanceof TaggedValue) {
            this.rec(((TaggedValue)elem).getType());
         }

         if (elem instanceof Instance) {
            this.recEnum(((Instance)elem).getSlotList(), elem);
         }

         if (elem instanceof Link) {
            this.recEnum(((Link)elem).getConnectionList(), elem);
         }

         if (elem instanceof LinkEnd) {
            this.recEnum(((LinkEnd)elem).getQualifierValueList(), elem);
         }

         if (elem instanceof State) {
            this.rec(((State)elem).getEntry());
         }

         if (elem instanceof State) {
            this.rec(((State)elem).getExit());
         }

         if (elem instanceof Event) {
            this.recEnum(((Event)elem).getParameterList(), elem);
         }

         if (elem instanceof Transition) {
            this.rec(((Transition)elem).getGuard());
         }

         if (elem instanceof StateMachine) {
            this.rec(((StateMachine)elem).getTop());
         }

         if (elem instanceof CompositeState) {
            this.recEnum(((CompositeState)elem).getSubvertexList(), elem);
         }

         if (elem instanceof Transition) {
            this.rec(((Transition)elem).getEffect());
         }

         if (elem instanceof State) {
            this.recEnum(((State)elem).getInternalTransitionList(), elem);
         }

         if (elem instanceof StateMachine) {
            this.recEnum(((StateMachine)elem).getTransitionsList(), elem);
         }

         if (elem instanceof State) {
            this.rec(((State)elem).getDoActivity());
         }

         if (elem instanceof Interaction) {
            this.recEnum(((Interaction)elem).getMessageList(), elem);
         }

         if (elem instanceof Collaboration) {
            this.recEnum(((Collaboration)elem).getInteractionList(), elem);
         }

         if (elem instanceof ActivityGraph) {
            this.recEnum(((ActivityGraph)elem).getPartitionList(), elem);
         }

         if (elem instanceof StructuralFeature) {
            this.rec(((StructuralFeature)elem).getMultiplicity());
         }

         if (elem instanceof AssociationEnd) {
            this.rec(((AssociationEnd)elem).getMultiplicity());
         }

         if (elem instanceof Attribute) {
            this.rec(((Attribute)elem).getInitialValue());
         }

         if (elem instanceof Parameter) {
            this.rec(((Parameter)elem).getDefaultValue());
         }

         if (elem instanceof Constraint) {
            this.rec(((Constraint)elem).getBody());
         }

         if (elem instanceof Abstraction) {
            this.rec(((Abstraction)elem).getMapping());
         }

         if (elem instanceof ClassifierRole) {
            this.rec(((ClassifierRole)elem).getMultiplicity());
         }

         if (elem instanceof AssociationRole) {
            this.rec(((AssociationRole)elem).getMultiplicity());
         }

         if (elem instanceof TaggedValue) {
            this.recEnum(((TaggedValue)elem).getTaggedValueList(), elem);
         }

         if (elem instanceof AssociationEndRole) {
            this.rec(((AssociationEndRole)elem).getCollaborationMultiplicity());
         }

         if (elem instanceof Component) {
            this.recEnum(((Component)elem).getResidentList(), elem);
         }

      }
   }

   private void recEnum(Enumeration en, Object elem) {
      while(en.hasMoreElements()) {
         Object obj = en.nextElement();
         if (obj != elem) {
            this.rec(obj);
         }
      }

   }

   public static void setOwnerModel(Model ownerModel) {
      UMLNavigationUtilities.TraversalActionListener tal = new UMLNavigationUtilities.TraversalActionListener(ownerModel);
      UMLNavigationUtilities unu = new UMLNavigationUtilities();
      unu.navigate(ownerModel, tal);
   }

   private static class TraversalActionListener implements UMLActionListener {
      private Model ownerModel;

      public TraversalActionListener(Model ownerModel) {
         this.ownerModel = ownerModel;
      }

      public void actionPerform(Element o) {
         o.setOwnerModel(this.ownerModel);
      }
   }
}
