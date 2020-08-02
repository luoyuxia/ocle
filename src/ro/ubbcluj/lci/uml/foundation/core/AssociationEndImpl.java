package ro.ubbcluj.lci.uml.foundation.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import ro.ubbcluj.lci.codegen.framework.dt.Integer;
import ro.ubbcluj.lci.codegen.framework.dt.OrderedSet;
import ro.ubbcluj.lci.codegen.framework.ocl.CollectionUtilities;
import ro.ubbcluj.lci.codegen.framework.ocl.Pairs;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.AssociationEndRole;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.LinkEnd;
import ro.ubbcluj.lci.uml.foundation.dataTypes.Multiplicity;
import ro.ubbcluj.lci.uml.foundation.dataTypes.MultiplicityRange;

public class AssociationEndImpl extends ModelElementImpl implements AssociationEnd {
   protected boolean isNavigable;
   protected int theOrdering;
   protected int theAggregation;
   protected int theTargetScope;
   protected int theChangeability;
   protected int theVisibility;
   protected Multiplicity theMultiplicity;
   protected Set theLinkEndList;
   protected OrderedSet theQualifierList;
   protected Set theSpecificationList;
   protected Classifier theParticipant;
   protected Association theAssociation;
   protected Set theAssociationEndRoleList;

   public AssociationEndImpl() {
   }

   public boolean isNavigable() {
      return this.isNavigable;
   }

   public void setNavigable(boolean isNavigable) {
      this.isNavigable = isNavigable;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isNavigable", 0));
      }

   }

   public int getOrdering() {
      return this.theOrdering;
   }

   public void setOrdering(int ordering) {
      this.theOrdering = ordering;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "ordering", 0));
      }

   }

   public int getAggregation() {
      return this.theAggregation;
   }

   public void setAggregation(int aggregation) {
      this.theAggregation = aggregation;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "aggregation", 0));
      }

   }

   public int getTargetScope() {
      return this.theTargetScope;
   }

   public void setTargetScope(int targetScope) {
      this.theTargetScope = targetScope;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "targetScope", 0));
      }

   }

   public int getChangeability() {
      return this.theChangeability;
   }

   public void setChangeability(int changeability) {
      this.theChangeability = changeability;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "changeability", 0));
      }

   }

   public int getVisibility() {
      return this.theVisibility;
   }

   public void setVisibility(int visibility) {
      this.theVisibility = visibility;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "visibility", 0));
      }

   }

   public Multiplicity getMultiplicity() {
      return this.theMultiplicity;
   }

   public void setMultiplicity(Multiplicity multiplicity) {
      this.theMultiplicity = multiplicity;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "multiplicity", 0));
      }

   }

   public Set getCollectionLinkEndList() {
      return this.theLinkEndList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theLinkEndList);
   }

   public java.util.Enumeration getLinkEndList() {
      return Collections.enumeration(this.getCollectionLinkEndList());
   }

   public void addLinkEnd(LinkEnd arg) {
      if (arg != null) {
         if (this.theLinkEndList == null) {
            this.theLinkEndList = new LinkedHashSet();
         }

         if (this.theLinkEndList.add(arg)) {
            arg.setAssociationEnd(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "linkEnd", 1));
            }
         }
      }

   }

   public void removeLinkEnd(LinkEnd arg) {
      if (this.theLinkEndList != null && arg != null && this.theLinkEndList.remove(arg)) {
         arg.setAssociationEnd((AssociationEnd)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "linkEnd", 2));
         }
      }

   }

   public OrderedSet getCollectionQualifierList() {
      return this.theQualifierList == null ? CollectionUtilities.newOrderedSet() : CollectionUtilities.newOrderedSet(this.theQualifierList);
   }

   public java.util.Enumeration getQualifierList() {
      return Collections.enumeration(this.getCollectionQualifierList());
   }

   public void addQualifier(Attribute arg) {
      if (arg != null) {
         if (this.theQualifierList == null) {
            this.theQualifierList = new OrderedSet();
         }

         if (this.theQualifierList.add(arg)) {
            arg.setAssociationEnd(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "qualifier", 1));
            }
         }
      }

   }

   public void removeQualifier(Attribute arg) {
      if (this.theQualifierList != null && arg != null && this.theQualifierList.remove(arg)) {
         arg.setAssociationEnd((AssociationEnd)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "qualifier", 2));
         }
      }

   }

   public Set getCollectionSpecificationList() {
      return this.theSpecificationList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theSpecificationList);
   }

   public java.util.Enumeration getSpecificationList() {
      return Collections.enumeration(this.getCollectionSpecificationList());
   }

   public void addSpecification(Classifier arg) {
      if (arg != null) {
         if (this.theSpecificationList == null) {
            this.theSpecificationList = new LinkedHashSet();
         }

         if (this.theSpecificationList.add(arg)) {
            arg.addSpecifiedEnd(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "specification", 1));
            }
         }
      }

   }

   public void removeSpecification(Classifier arg) {
      if (this.theSpecificationList != null && arg != null && this.theSpecificationList.remove(arg)) {
         arg.removeSpecifiedEnd(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "specification", 2));
         }
      }

   }

   public Classifier getParticipant() {
      return this.theParticipant;
   }

   public void setParticipant(Classifier arg) {
      if (this.theParticipant != arg) {
         Classifier temp = this.theParticipant;
         this.theParticipant = null;
         if (temp != null) {
            temp.removeAssociation(this);
         }

         if (arg != null) {
            this.theParticipant = arg;
            arg.addAssociation(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "participant", 0));
         }
      }

   }

   public Association getAssociation() {
      return this.theAssociation;
   }

   public void setAssociation(Association arg) {
      if (this.theAssociation != arg) {
         Association temp = this.theAssociation;
         this.theAssociation = null;
         if (temp != null) {
            temp.removeConnection(this);
         }

         if (arg != null) {
            this.theAssociation = arg;
            arg.addConnection(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "association", 0));
         }
      }

   }

   public Set getCollectionAssociationEndRoleList() {
      return this.theAssociationEndRoleList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theAssociationEndRoleList);
   }

   public java.util.Enumeration getAssociationEndRoleList() {
      return Collections.enumeration(this.getCollectionAssociationEndRoleList());
   }

   public void addAssociationEndRole(AssociationEndRole arg) {
      if (arg != null) {
         if (this.theAssociationEndRoleList == null) {
            this.theAssociationEndRoleList = new LinkedHashSet();
         }

         if (this.theAssociationEndRoleList.add(arg)) {
            arg.setBase(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "associationEndRole", 1));
            }
         }
      }

   }

   public void removeAssociationEndRole(AssociationEndRole arg) {
      if (this.theAssociationEndRoleList != null && arg != null && this.theAssociationEndRoleList.remove(arg)) {
         arg.setBase((AssociationEnd)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "associationEndRole", 2));
         }
      }

   }

   public int upperbound() {
      Multiplicity multiplicityMultiplicity = this.getMultiplicity();
      Set setRange = multiplicityMultiplicity.getCollectionRangeList();
      List bagCollect = CollectionUtilities.newBag();

      int nUpper;
      for(Iterator iter = setRange.iterator(); iter.hasNext(); bagCollect.add(Integer.toInteger(nUpper))) {
         MultiplicityRange decl = (MultiplicityRange)iter.next();
         nUpper = decl.getUpper().intValue();
         if (nUpper == -1) {
            nUpper = 2147483647;
         }
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      List seqAsSequence = CollectionUtilities.asSequence(bagCollect);
      Pairs sortedPairs = new Pairs();
      Iterator iter0 = seqAsSequence.iterator();

      int nLast;
      while(iter0.hasNext()) {
         Object temp = iter0.next();
         nLast = ((Integer)temp).asInteger();
         sortedPairs.addPair(temp, Integer.toInteger(nLast));
      }

      sortedPairs.sort();
      List seqSortedBy = sortedPairs.asSequence();
      nLast = ((Integer)CollectionUtilities.last(seqSortedBy)).asInteger();
      return nLast;
   }

   protected void internalRemove() {
      java.util.Enumeration tmpLinkEndEnum = this.getLinkEndList();
      ArrayList tmpLinkEndList = new ArrayList();

      while(tmpLinkEndEnum.hasMoreElements()) {
         tmpLinkEndList.add(tmpLinkEndEnum.nextElement());
      }

      Iterator it = tmpLinkEndList.iterator();

      while(it.hasNext()) {
         ((LinkEnd)it.next()).setAssociationEnd((AssociationEnd)null);
      }

      java.util.Enumeration tmpQualifierEnum = this.getQualifierList();
      ArrayList tmpQualifierList = new ArrayList();

      while(tmpQualifierEnum.hasMoreElements()) {
         tmpQualifierList.add(tmpQualifierEnum.nextElement());
      }

       it = tmpQualifierList.iterator();

      while(it.hasNext()) {
         ((Attribute)it.next()).remove();
      }

      java.util.Enumeration tmpSpecificationEnum = this.getSpecificationList();
      ArrayList tmpSpecificationList = new ArrayList();

      while(tmpSpecificationEnum.hasMoreElements()) {
         tmpSpecificationList.add(tmpSpecificationEnum.nextElement());
      }

       it = tmpSpecificationList.iterator();

      while(it.hasNext()) {
         Classifier tmpSpecification = (Classifier)it.next();
         tmpSpecification.removeSpecifiedEnd(this);
      }

      Classifier tmpParticipant = this.getParticipant();
      if (tmpParticipant != null) {
         tmpParticipant.removeAssociation(this);
      }

      Association tmpAssociation = this.getAssociation();
      if (tmpAssociation != null) {
         tmpAssociation.removeConnection(this);
         if (tmpAssociation.getCollectionConnectionList().size() < 2) {
            tmpAssociation.remove();
         }
      }

      java.util.Enumeration tmpAssociationEndRoleEnum = this.getAssociationEndRoleList();
      ArrayList tmpAssociationEndRoleList = new ArrayList();

      while(tmpAssociationEndRoleEnum.hasMoreElements()) {
         tmpAssociationEndRoleList.add(tmpAssociationEndRoleEnum.nextElement());
      }

       it = tmpAssociationEndRoleList.iterator();

      while(it.hasNext()) {
         ((AssociationEndRole)it.next()).setBase((AssociationEnd)null);
      }

      super.internalRemove();
   }
}
