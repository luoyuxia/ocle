package ro.ubbcluj.lci.uml.behavioralElements.activityGraphs;

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
import ro.ubbcluj.lci.uml.behavioralElements.actions.actionFoundation.Pin;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Procedure;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.SimpleStateImpl;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.State;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.StateVertex;
import ro.ubbcluj.lci.uml.behavioralElements.stateMachines.Transition;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;
import ro.ubbcluj.lci.uml.foundation.core.Parameter;

public class ObjectFlowStateImpl extends SimpleStateImpl implements ObjectFlowState {
   protected boolean isSynch;
   protected Set theParameterList;
   protected Classifier theType;

   public ObjectFlowStateImpl() {
   }

   public boolean isSynch() {
      return this.isSynch;
   }

   public void setSynch(boolean isSynch) {
      this.isSynch = isSynch;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "isSynch", 0));
      }

   }

   public Set getCollectionParameterList() {
      return this.theParameterList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theParameterList);
   }

   public Enumeration getParameterList() {
      return Collections.enumeration(this.getCollectionParameterList());
   }

   public void addParameter(Parameter arg) {
      if (arg != null) {
         if (this.theParameterList == null) {
            this.theParameterList = new LinkedHashSet();
         }

         if (this.theParameterList.add(arg)) {
            arg.addState(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "parameter", 1));
            }
         }
      }

   }

   public void removeParameter(Parameter arg) {
      if (this.theParameterList != null && arg != null && this.theParameterList.remove(arg)) {
         arg.removeState(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "parameter", 2));
         }
      }

   }

   public Classifier getType() {
      return this.theType;
   }

   public void setType(Classifier arg) {
      if (this.theType != arg) {
         Classifier temp = this.theType;
         this.theType = null;
         if (temp != null) {
            temp.removeObjectFlowState(this);
         }

         if (arg != null) {
            this.theType = arg;
            arg.addObjectFlowState(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "type", 0));
         }
      }

   }

   public Set allNextLeafStates() {
      Set setOutgoing = this.getCollectionOutgoingList();
      List bagCollect = CollectionUtilities.newBag();
      Iterator iter = setOutgoing.iterator();

      while(iter.hasNext()) {
         Transition decl = (Transition)iter.next();
         StateVertex stateVertexTarget = decl.getTarget();
         bagCollect.add(stateVertexTarget);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      List bagCollect0 = CollectionUtilities.newBag();
      Iterator iter0 = bagCollect.iterator();

      while(iter0.hasNext()) {
         StateVertex decl0 = (StateVertex)iter0.next();
         State stateOclAsType = (State)decl0;
         bagCollect0.add(stateOclAsType);
      }

      bagCollect0 = CollectionUtilities.flatten(bagCollect0);
      Set setAsSet = CollectionUtilities.asSet(bagCollect0);
      return setAsSet;
   }

   public Set allPreviousLeafStates() {
      Set setIncoming = this.getCollectionIncomingList();
      List bagCollect = CollectionUtilities.newBag();
      Iterator iter = setIncoming.iterator();

      while(iter.hasNext()) {
         Transition decl = (Transition)iter.next();
         StateVertex stateVertexSource = decl.getSource();
         bagCollect.add(stateVertexSource);
      }

      bagCollect = CollectionUtilities.flatten(bagCollect);
      List bagCollect0 = CollectionUtilities.newBag();
      Iterator iter0 = bagCollect.iterator();

      while(iter0.hasNext()) {
         StateVertex decl0 = (StateVertex)iter0.next();
         State stateOclAsType = (State)decl0;
         bagCollect0.add(stateOclAsType);
      }

      bagCollect0 = CollectionUtilities.flatten(bagCollect0);
      Set setAsSet = CollectionUtilities.asSet(bagCollect0);
      return setAsSet;
   }

   public boolean isInputAction(Procedure p) {
      List seqArgument = p.getCollectionArgumentList();
      boolean bExists = false;

      boolean bEquals;
      for(Iterator iter = seqArgument.iterator(); !bExists && iter.hasNext(); bExists = bEquals) {
         Pin i = (Pin)iter.next();
         Classifier classifierType = i.getType();
         Classifier classifierType0 = this.getType();
         bEquals = classifierType.equals(classifierType0);
      }

      return bExists;
   }

   public boolean isOutputAction(Procedure p) {
      List seqResult = p.getCollectionResultList();
      boolean bExists = false;

      boolean bEquals;
      for(Iterator iter = seqResult.iterator(); !bExists && iter.hasNext(); bExists = bEquals) {
         Pin i = (Pin)iter.next();
         Classifier classifierType = i.getType();
         Classifier classifierType0 = this.getType();
         bEquals = classifierType.equals(classifierType0);
      }

      return bExists;
   }

   protected void internalRemove() {
      Enumeration tmpParameterEnum = this.getParameterList();
      ArrayList tmpParameterList = new ArrayList();

      while(tmpParameterEnum.hasMoreElements()) {
         tmpParameterList.add(tmpParameterEnum.nextElement());
      }

      Iterator it = tmpParameterList.iterator();

      while(it.hasNext()) {
         Parameter tmpParameter = (Parameter)it.next();
         tmpParameter.removeState(this);
      }

      Classifier tmpType = this.getType();
      if (tmpType != null) {
         tmpType.removeObjectFlowState(this);
      }

      super.internalRemove();
   }
}
