package ro.ubbcluj.lci.gui.browser.BTree;

import java.util.ArrayList;
import java.util.List;
import ro.ubbcluj.lci.gui.browser.GBrowser;
import ro.ubbcluj.lci.uml.behavioralElements.activityGraphs.ActivityGraph;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.AssociationEndRole;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Collaboration;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.CollaborationInstanceSet;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.Interaction;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Link;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkEnd;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Procedure;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.CompositeState;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.Event;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.State;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.StateMachine;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.Transition;
import ro.ubbcluj.lci.uml.behavioralElements.useCases.UseCase;
import ro.ubbcluj.lci.uml.foundation.core.Association;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.BehavioralFeature;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Enumeration;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.Namespace;
import ro.ubbcluj.lci.uml.foundation.core.Operation;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;
import ro.ubbcluj.lci.uml.foundation.extensionMechanisms.Stereotype;

public class Navigation {
   protected Filter filter;

   public Navigation(Filter f) {
      this.filter = f;
   }

   public List getChildren(Object element) {
      List children = new ArrayList();
      List tmpResult;
      if (element instanceof Namespace) {
         tmpResult = new ArrayList(((Namespace)element).directGetCollectionOwnedElementList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            tmpResult = GBrowser.getOrder().order(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof Classifier) {
         tmpResult = new ArrayList(((Classifier)element).getCollectionFeatureList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            tmpResult = GBrowser.getOrder().order(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof BehavioralFeature) {
         tmpResult = new ArrayList(((BehavioralFeature)element).getCollectionParameterList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof Operation) {
         tmpResult = new ArrayList(((Operation)element).getCollectionMethodList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            tmpResult = GBrowser.getOrder().order(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof Association) {
         tmpResult = new ArrayList(((Association)element).getCollectionConnectionList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof AssociationEnd) {
         tmpResult = new ArrayList(((AssociationEnd)element).getCollectionQualifierList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof Enumeration) {
         tmpResult = new ArrayList(((Enumeration)element).getCollectionLiteralList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof ModelElement) {
         tmpResult = new ArrayList(((ModelElement)element).directGetCollectionParameterTemplateList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof ModelElement) {
         tmpResult = new ArrayList(((ModelElement)element).getCollectionTaggedValueList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            tmpResult = GBrowser.getOrder().order(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof Stereotype) {
         tmpResult = new ArrayList(((Stereotype)element).getCollectionDefinedTagList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            tmpResult = GBrowser.getOrder().order(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof Stereotype) {
         tmpResult = new ArrayList(((Stereotype)element).getCollectionStereotypeConstraintList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            tmpResult = GBrowser.getOrder().order(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof Multiplicity) {
         tmpResult = new ArrayList(((Multiplicity)element).getCollectionRangeList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            tmpResult = GBrowser.getOrder().order(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof Instance) {
         tmpResult = new ArrayList(((Instance)element).getCollectionOwnedInstanceList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            tmpResult = GBrowser.getOrder().order(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof Instance) {
         tmpResult = new ArrayList(((Instance)element).getCollectionSlotList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            tmpResult = GBrowser.getOrder().order(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof Instance) {
         tmpResult = new ArrayList(((Instance)element).getCollectionOwnedLinkList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            tmpResult = GBrowser.getOrder().order(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof Link) {
         tmpResult = new ArrayList(((Link)element).getCollectionConnectionList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof LinkEnd) {
         tmpResult = new ArrayList(((LinkEnd)element).getCollectionQualifierValueList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof AssociationEndRole) {
         tmpResult = new ArrayList(((AssociationEndRole)element).getCollectionAvailableQualifierList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            tmpResult = GBrowser.getOrder().order(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof Collaboration) {
         tmpResult = new ArrayList(((Collaboration)element).getCollectionInteractionList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            tmpResult = GBrowser.getOrder().order(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof Interaction) {
         tmpResult = new ArrayList(((Interaction)element).getCollectionMessageList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            tmpResult = GBrowser.getOrder().order(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof CollaborationInstanceSet) {
         tmpResult = new ArrayList(((CollaborationInstanceSet)element).getCollectionInteractionInstanceList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            tmpResult = GBrowser.getOrder().order(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof UseCase) {
         tmpResult = new ArrayList(((UseCase)element).getCollectionExtensionPointList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            tmpResult = GBrowser.getOrder().order(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof StateMachine) {
         Object tmpResult1 = ((StateMachine)element).getTop();
         if (this.filter.accept((Object)tmpResult1)) {
            GBrowser.getOrder().add(children, (Object)tmpResult1);
         }
      }

      if (element instanceof StateMachine) {
         tmpResult = new ArrayList(((StateMachine)element).getCollectionTransitionsList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            tmpResult = GBrowser.getOrder().order(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof State) {
         tmpResult = new ArrayList(((State)element).getCollectionInternalTransitionList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            tmpResult = GBrowser.getOrder().order(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      Procedure tmpResult1;
      if (element instanceof State) {
         tmpResult1 = ((State)element).getEntry();
         if (this.filter.accept((Object)tmpResult1)) {
            GBrowser.getOrder().add(children, (Object)tmpResult1);
         }
      }

      if (element instanceof State) {
         tmpResult1 = ((State)element).getExit();
         if (this.filter.accept((Object)tmpResult1)) {
            GBrowser.getOrder().add(children, (Object)tmpResult1);
         }
      }

      if (element instanceof State) {
         tmpResult1 = ((State)element).getDoActivity();
         if (this.filter.accept((Object)tmpResult1)) {
            GBrowser.getOrder().add(children, (Object)tmpResult1);
         }
      }

      if (element instanceof Transition) {
         Object object = ((Transition)element).getGuard();
         if (this.filter.accept((Object)object)) {
            GBrowser.getOrder().add(children, (Object)object);
         }
      }

      if (element instanceof Transition) {
         tmpResult1 = ((Transition)element).getEffect();
         if (this.filter.accept((Object)tmpResult1)) {
            GBrowser.getOrder().add(children, (Object)tmpResult1);
         }
      }

      if (element instanceof CompositeState) {
         tmpResult = new ArrayList(((CompositeState)element).getCollectionSubvertexList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            tmpResult = GBrowser.getOrder().order(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof Event) {
         tmpResult = new ArrayList(((Event)element).getCollectionParameterList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      if (element instanceof ActivityGraph) {
         tmpResult = new ArrayList(((ActivityGraph)element).getCollectionPartitionList());
         if (!tmpResult.isEmpty()) {
            this.filter.filter(tmpResult);
            tmpResult = GBrowser.getOrder().order(tmpResult);
            GBrowser.getOrder().add(children, (List)tmpResult);
         }
      }

      return children;
   }
}
