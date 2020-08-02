package ro.ubbcluj.lci.uml.behavioralElements.commonBehavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import ro.ubbcluj.lci.codegen.framework.ocl.CollectionUtilities;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.ClassifierRole;
import ro.ubbcluj.lci.uml.behavioralElements.collaborations.CollaborationInstanceSet;
import ro.ubbcluj.lci.uml.foundation.core.AssociationEnd;
import ro.ubbcluj.lci.uml.foundation.core.Attribute;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;

public class InstanceImpl extends ModelElementImpl implements Instance {
   protected Set theClassifierList;
   protected Set theStimulus1List;
   protected Set theSlotList;
   protected Set theOwnedInstanceList;
   protected Set thePlayedRoleList;
   protected Set theCollaborationInstanceSetList;
   protected Set theStimulus2List;
   protected Set theAttributeLinkList;
   protected Instance theOwner;
   protected Set theStimulus3List;
   protected Set theOwnedLinkList;
   protected Set theLinkEndList;
   protected ComponentInstance theComponentInstance;

   public InstanceImpl() {
   }

   public Set getCollectionClassifierList() {
      return this.theClassifierList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theClassifierList);
   }

   public Enumeration getClassifierList() {
      return Collections.enumeration(this.getCollectionClassifierList());
   }

   public void addClassifier(Classifier arg) {
      if (arg != null) {
         if (this.theClassifierList == null) {
            this.theClassifierList = new LinkedHashSet();
         }

         if (this.theClassifierList.add(arg)) {
            arg.addInstance(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "classifier", 1));
            }
         }
      }

   }

   public void removeClassifier(Classifier arg) {
      if (this.theClassifierList != null && arg != null && this.theClassifierList.remove(arg)) {
         arg.removeInstance(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "classifier", 2));
         }
      }

   }

   public Set getCollectionStimulus1List() {
      return this.theStimulus1List == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theStimulus1List);
   }

   public Enumeration getStimulus1List() {
      return Collections.enumeration(this.getCollectionStimulus1List());
   }

   public void addStimulus1(Stimulus arg) {
      if (arg != null) {
         if (this.theStimulus1List == null) {
            this.theStimulus1List = new LinkedHashSet();
         }

         if (this.theStimulus1List.add(arg)) {
            arg.addArgument(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "stimulus1", 1));
            }
         }
      }

   }

   public void removeStimulus1(Stimulus arg) {
      if (this.theStimulus1List != null && arg != null && this.theStimulus1List.remove(arg)) {
         arg.removeArgument(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "stimulus1", 2));
         }
      }

   }

   public Set getCollectionSlotList() {
      return this.theSlotList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theSlotList);
   }

   public Enumeration getSlotList() {
      return Collections.enumeration(this.getCollectionSlotList());
   }

   public void addSlot(AttributeLink arg) {
      if (arg != null) {
         if (this.theSlotList == null) {
            this.theSlotList = new LinkedHashSet();
         }

         if (this.theSlotList.add(arg)) {
            arg.setInstance(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "slot", 1));
            }
         }
      }

   }

   public void removeSlot(AttributeLink arg) {
      if (this.theSlotList != null && arg != null && this.theSlotList.remove(arg)) {
         arg.setInstance((Instance)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "slot", 2));
         }
      }

   }

   public Set getCollectionOwnedInstanceList() {
      return this.theOwnedInstanceList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theOwnedInstanceList);
   }

   public Enumeration getOwnedInstanceList() {
      return Collections.enumeration(this.getCollectionOwnedInstanceList());
   }

   public void addOwnedInstance(Instance arg) {
      if (arg != null) {
         if (this.theOwnedInstanceList == null) {
            this.theOwnedInstanceList = new LinkedHashSet();
         }

         if (this.theOwnedInstanceList.add(arg)) {
            arg.setOwner(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "ownedInstance", 1));
            }
         }
      }

   }

   public void removeOwnedInstance(Instance arg) {
      if (this.theOwnedInstanceList != null && arg != null && this.theOwnedInstanceList.remove(arg)) {
         arg.setOwner((Instance)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "ownedInstance", 2));
         }
      }

   }

   public Set getCollectionPlayedRoleList() {
      return this.thePlayedRoleList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.thePlayedRoleList);
   }

   public Enumeration getPlayedRoleList() {
      return Collections.enumeration(this.getCollectionPlayedRoleList());
   }

   public void addPlayedRole(ClassifierRole arg) {
      if (arg != null) {
         if (this.thePlayedRoleList == null) {
            this.thePlayedRoleList = new LinkedHashSet();
         }

         if (this.thePlayedRoleList.add(arg)) {
            arg.addConformingInstance(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "playedRole", 1));
            }
         }
      }

   }

   public void removePlayedRole(ClassifierRole arg) {
      if (this.thePlayedRoleList != null && arg != null && this.thePlayedRoleList.remove(arg)) {
         arg.removeConformingInstance(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "playedRole", 2));
         }
      }

   }

   public Set getCollectionCollaborationInstanceSetList() {
      return this.theCollaborationInstanceSetList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theCollaborationInstanceSetList);
   }

   public Enumeration getCollaborationInstanceSetList() {
      return Collections.enumeration(this.getCollectionCollaborationInstanceSetList());
   }

   public void addCollaborationInstanceSet(CollaborationInstanceSet arg) {
      if (arg != null) {
         if (this.theCollaborationInstanceSetList == null) {
            this.theCollaborationInstanceSetList = new LinkedHashSet();
         }

         if (this.theCollaborationInstanceSetList.add(arg)) {
            arg.addParticipatingInstance(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "collaborationInstanceSet", 1));
            }
         }
      }

   }

   public void removeCollaborationInstanceSet(CollaborationInstanceSet arg) {
      if (this.theCollaborationInstanceSetList != null && arg != null && this.theCollaborationInstanceSetList.remove(arg)) {
         arg.removeParticipatingInstance(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "collaborationInstanceSet", 2));
         }
      }

   }

   public Set getCollectionStimulus2List() {
      return this.theStimulus2List == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theStimulus2List);
   }

   public Enumeration getStimulus2List() {
      return Collections.enumeration(this.getCollectionStimulus2List());
   }

   public void addStimulus2(Stimulus arg) {
      if (arg != null) {
         if (this.theStimulus2List == null) {
            this.theStimulus2List = new LinkedHashSet();
         }

         if (this.theStimulus2List.add(arg)) {
            arg.setSender(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "stimulus2", 1));
            }
         }
      }

   }

   public void removeStimulus2(Stimulus arg) {
      if (this.theStimulus2List != null && arg != null && this.theStimulus2List.remove(arg)) {
         arg.setSender((Instance)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "stimulus2", 2));
         }
      }

   }

   public Set getCollectionAttributeLinkList() {
      return this.theAttributeLinkList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theAttributeLinkList);
   }

   public Enumeration getAttributeLinkList() {
      return Collections.enumeration(this.getCollectionAttributeLinkList());
   }

   public void addAttributeLink(AttributeLink arg) {
      if (arg != null) {
         if (this.theAttributeLinkList == null) {
            this.theAttributeLinkList = new LinkedHashSet();
         }

         if (this.theAttributeLinkList.add(arg)) {
            arg.setValue(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "attributeLink", 1));
            }
         }
      }

   }

   public void removeAttributeLink(AttributeLink arg) {
      if (this.theAttributeLinkList != null && arg != null && this.theAttributeLinkList.remove(arg)) {
         arg.setValue((Instance)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "attributeLink", 2));
         }
      }

   }

   public Instance getOwner() {
      return this.theOwner;
   }

   public void setOwner(Instance arg) {
      if (this.theOwner != arg) {
         Instance temp = this.theOwner;
         this.theOwner = null;
         if (temp != null) {
            temp.removeOwnedInstance(this);
         }

         if (arg != null) {
            this.theOwner = arg;
            arg.addOwnedInstance(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "owner", 0));
         }
      }

   }

   public Set getCollectionStimulus3List() {
      return this.theStimulus3List == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theStimulus3List);
   }

   public Enumeration getStimulus3List() {
      return Collections.enumeration(this.getCollectionStimulus3List());
   }

   public void addStimulus3(Stimulus arg) {
      if (arg != null) {
         if (this.theStimulus3List == null) {
            this.theStimulus3List = new LinkedHashSet();
         }

         if (this.theStimulus3List.add(arg)) {
            arg.setReceiver(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "stimulus3", 1));
            }
         }
      }

   }

   public void removeStimulus3(Stimulus arg) {
      if (this.theStimulus3List != null && arg != null && this.theStimulus3List.remove(arg)) {
         arg.setReceiver((Instance)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "stimulus3", 2));
         }
      }

   }

   public Set getCollectionOwnedLinkList() {
      return this.theOwnedLinkList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theOwnedLinkList);
   }

   public Enumeration getOwnedLinkList() {
      return Collections.enumeration(this.getCollectionOwnedLinkList());
   }

   public void addOwnedLink(Link arg) {
      if (arg != null) {
         if (this.theOwnedLinkList == null) {
            this.theOwnedLinkList = new LinkedHashSet();
         }

         if (this.theOwnedLinkList.add(arg)) {
            arg.setOwner(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "ownedLink", 1));
            }
         }
      }

   }

   public void removeOwnedLink(Link arg) {
      if (this.theOwnedLinkList != null && arg != null && this.theOwnedLinkList.remove(arg)) {
         arg.setOwner((Instance)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "ownedLink", 2));
         }
      }

   }

   public Set getCollectionLinkEndList() {
      return this.theLinkEndList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theLinkEndList);
   }

   public Enumeration getLinkEndList() {
      return Collections.enumeration(this.getCollectionLinkEndList());
   }

   public void addLinkEnd(LinkEnd arg) {
      if (arg != null) {
         if (this.theLinkEndList == null) {
            this.theLinkEndList = new LinkedHashSet();
         }

         if (this.theLinkEndList.add(arg)) {
            arg.setInstance(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "linkEnd", 1));
            }
         }
      }

   }

   public void removeLinkEnd(LinkEnd arg) {
      if (this.theLinkEndList != null && arg != null && this.theLinkEndList.remove(arg)) {
         arg.setInstance((Instance)null);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "linkEnd", 2));
         }
      }

   }

   public ComponentInstance getComponentInstance() {
      return this.theComponentInstance;
   }

   public void setComponentInstance(ComponentInstance arg) {
      if (this.theComponentInstance != arg) {
         ComponentInstance temp = this.theComponentInstance;
         this.theComponentInstance = null;
         if (temp != null) {
            temp.removeResident(this);
         }

         if (arg != null) {
            this.theComponentInstance = arg;
            arg.addResident(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "componentInstance", 0));
         }
      }

   }

   public Set allLinks() {
      Set setLinkEnd = this.getCollectionLinkEndList();
      List bagCollect = CollectionUtilities.newBag();
      Iterator iter = setLinkEnd.iterator();

      while(iter.hasNext()) {
         LinkEnd decl = (LinkEnd)iter.next();
         Link linkLink = decl.getLink();
         bagCollect.add(linkLink);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      Set setAsSet = CollectionUtilities.asSet(bagCollect);
      return setAsSet;
   }

   public Set allOppositeLinkEnds() {
      Set setallLinks = this.allLinks();
      List bagCollect = CollectionUtilities.newBag();
      Iterator iter = setallLinks.iterator();

      while(iter.hasNext()) {
         Link decl = (Link)iter.next();
         Set setConnection = decl.getCollectionConnectionList();
         bagCollect.add(setConnection);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      Set setAsSet = CollectionUtilities.asSet(bagCollect);
      Set setSelect = CollectionUtilities.newSet();
      Iterator iter0 = setAsSet.iterator();

      while(iter0.hasNext()) {
         LinkEnd le = (LinkEnd)iter0.next();
         Instance instanceInstance = le.getInstance();
         boolean bNotEquals = !instanceInstance.equals(this);
         if (bNotEquals) {
            CollectionUtilities.add(setSelect, le);
         }
      }

      return setSelect;
   }

   public Set selectedLinkEnds(AssociationEnd ae) {
      Set setallOppositeLinkEnds = this.allOppositeLinkEnds();
      Set setSelect = CollectionUtilities.newSet();
      Iterator iter = setallOppositeLinkEnds.iterator();

      while(iter.hasNext()) {
         LinkEnd le = (LinkEnd)iter.next();
         AssociationEnd associationEndAssociationEnd = le.getAssociationEnd();
         boolean bEquals = associationEndAssociationEnd.equals(ae);
         if (bEquals) {
            CollectionUtilities.add(setSelect, le);
         }
      }

      return setSelect;
   }

   public Set selectedAttributeLinks(Attribute ae) {
      Set setSlot = this.getCollectionSlotList();
      Set setSelect = CollectionUtilities.newSet();
      Iterator iter = setSlot.iterator();

      while(iter.hasNext()) {
         AttributeLink s = (AttributeLink)iter.next();
         Attribute attributeAttribute = s.getAttribute();
         boolean bEquals = attributeAttribute.equals(ae);
         if (bEquals) {
            CollectionUtilities.add(setSelect, s);
         }
      }

      return setSelect;
   }

   public Set contents() {
      Set setOwnedInstance = this.getCollectionOwnedInstanceList();
      List bagCollect = CollectionUtilities.newBag();
      Iterator iter = setOwnedInstance.iterator();

      while(iter.hasNext()) {
         Instance decl = (Instance)iter.next();
         bagCollect.add(decl);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      Set setOwnedLink = this.getCollectionOwnedLinkList();
      List bagUnion = CollectionUtilities.union(bagCollect, setOwnedLink);
      List bagCollect0 = CollectionUtilities.newBag();
      Iterator iter0 = bagUnion.iterator();

      while(iter0.hasNext()) {
         ModelElement decl0 = (ModelElement)iter0.next();
         bagCollect0.add(decl0);
      }

      bagCollect0 = CollectionUtilities.flatten(bagCollect0);
      Set setAsSet = CollectionUtilities.asSet(bagCollect0);
      return setAsSet;
   }

   protected void internalRemove() {
      Enumeration tmpClassifierEnum = this.getClassifierList();
      ArrayList tmpClassifierList = new ArrayList();

      while(tmpClassifierEnum.hasMoreElements()) {
         tmpClassifierList.add(tmpClassifierEnum.nextElement());
      }

      Iterator it = tmpClassifierList.iterator();

      while(it.hasNext()) {
         Classifier tmpClassifier = (Classifier)it.next();
         tmpClassifier.removeInstance(this);
      }

      Enumeration tmpStimulus1Enum = this.getStimulus1List();
      ArrayList tmpStimulus1List = new ArrayList();

      while(tmpStimulus1Enum.hasMoreElements()) {
         tmpStimulus1List.add(tmpStimulus1Enum.nextElement());
      }

       it = tmpStimulus1List.iterator();

      while(it.hasNext()) {
         Stimulus tmpStimulus1 = (Stimulus)it.next();
         tmpStimulus1.removeArgument(this);
      }

      Enumeration tmpSlotEnum = this.getSlotList();
      ArrayList tmpSlotList = new ArrayList();

      while(tmpSlotEnum.hasMoreElements()) {
         tmpSlotList.add(tmpSlotEnum.nextElement());
      }

       it = tmpSlotList.iterator();

      while(it.hasNext()) {
         ((AttributeLink)it.next()).remove();
      }

      Enumeration tmpOwnedInstanceEnum = this.getOwnedInstanceList();
      ArrayList tmpOwnedInstanceList = new ArrayList();

      while(tmpOwnedInstanceEnum.hasMoreElements()) {
         tmpOwnedInstanceList.add(tmpOwnedInstanceEnum.nextElement());
      }

       it = tmpOwnedInstanceList.iterator();

      while(it.hasNext()) {
         ((Instance)it.next()).remove();
      }

      Enumeration tmpPlayedRoleEnum = this.getPlayedRoleList();
      ArrayList tmpPlayedRoleList = new ArrayList();

      while(tmpPlayedRoleEnum.hasMoreElements()) {
         tmpPlayedRoleList.add(tmpPlayedRoleEnum.nextElement());
      }

       it = tmpPlayedRoleList.iterator();

      while(it.hasNext()) {
         ClassifierRole tmpPlayedRole = (ClassifierRole)it.next();
         tmpPlayedRole.removeConformingInstance(this);
      }

      Enumeration tmpCollaborationInstanceSetEnum = this.getCollaborationInstanceSetList();
      ArrayList tmpCollaborationInstanceSetList = new ArrayList();

      while(tmpCollaborationInstanceSetEnum.hasMoreElements()) {
         tmpCollaborationInstanceSetList.add(tmpCollaborationInstanceSetEnum.nextElement());
      }

       it = tmpCollaborationInstanceSetList.iterator();

      while(it.hasNext()) {
         CollaborationInstanceSet tmpCollaborationInstanceSet = (CollaborationInstanceSet)it.next();
         tmpCollaborationInstanceSet.removeParticipatingInstance(this);
         if (tmpCollaborationInstanceSet.getCollectionParticipatingInstanceList().size() < 1) {
            tmpCollaborationInstanceSet.remove();
         }
      }

      Enumeration tmpStimulus2Enum = this.getStimulus2List();
      ArrayList tmpStimulus2List = new ArrayList();

      while(tmpStimulus2Enum.hasMoreElements()) {
         tmpStimulus2List.add(tmpStimulus2Enum.nextElement());
      }

       it = tmpStimulus2List.iterator();

      while(it.hasNext()) {
         ((Stimulus)it.next()).setSender((Instance)null);
      }

      Enumeration tmpAttributeLinkEnum = this.getAttributeLinkList();
      ArrayList tmpAttributeLinkList = new ArrayList();

      while(tmpAttributeLinkEnum.hasMoreElements()) {
         tmpAttributeLinkList.add(tmpAttributeLinkEnum.nextElement());
      }

       it = tmpAttributeLinkList.iterator();

      while(it.hasNext()) {
         ((AttributeLink)it.next()).setValue((Instance)null);
      }

      Instance tmpOwner = this.getOwner();
      if (tmpOwner != null) {
         tmpOwner.removeOwnedInstance(this);
      }

      Enumeration tmpStimulus3Enum = this.getStimulus3List();
      ArrayList tmpStimulus3List = new ArrayList();

      while(tmpStimulus3Enum.hasMoreElements()) {
         tmpStimulus3List.add(tmpStimulus3Enum.nextElement());
      }

       it = tmpStimulus3List.iterator();

      while(it.hasNext()) {
         ((Stimulus)it.next()).setReceiver((Instance)null);
      }

      Enumeration tmpOwnedLinkEnum = this.getOwnedLinkList();
      ArrayList tmpOwnedLinkList = new ArrayList();

      while(tmpOwnedLinkEnum.hasMoreElements()) {
         tmpOwnedLinkList.add(tmpOwnedLinkEnum.nextElement());
      }

       it = tmpOwnedLinkList.iterator();

      while(it.hasNext()) {
         ((Link)it.next()).remove();
      }

      Enumeration tmpLinkEndEnum = this.getLinkEndList();
      ArrayList tmpLinkEndList = new ArrayList();

      while(tmpLinkEndEnum.hasMoreElements()) {
         tmpLinkEndList.add(tmpLinkEndEnum.nextElement());
      }

       it = tmpLinkEndList.iterator();

      while(it.hasNext()) {
         Link link = ((LinkEnd)it.next()).getLink();
         if (link != null) {
            link.remove();
         }
      }

      ComponentInstance tmpComponentInstance = this.getComponentInstance();
      if (tmpComponentInstance != null) {
         tmpComponentInstance.removeResident(this);
      }

      super.internalRemove();
   }
}
