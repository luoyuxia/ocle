package ro.ubbcluj.lci.uml.behavioralElements.collaborations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Stimulus;
import ro.ubbcluj.lci.uml.foundation.core.ModelElementImpl;

public class InteractionInstanceSetImpl extends ModelElementImpl implements InteractionInstanceSet {
   protected Set theParticipatingStimulusList;
   protected CollaborationInstanceSet theContext;
   protected Interaction theInteraction;

   public InteractionInstanceSetImpl() {
   }

   public Set getCollectionParticipatingStimulusList() {
      return this.theParticipatingStimulusList == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.theParticipatingStimulusList);
   }

   public Enumeration getParticipatingStimulusList() {
      return Collections.enumeration(this.getCollectionParticipatingStimulusList());
   }

   public void addParticipatingStimulus(Stimulus arg) {
      if (arg != null) {
         if (this.theParticipatingStimulusList == null) {
            this.theParticipatingStimulusList = new LinkedHashSet();
         }

         if (this.theParticipatingStimulusList.add(arg)) {
            arg.addInteractionInstanceSet(this);
            RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
            if (agent != null && agent.firesEvents()) {
               agent.fireChangeEvent(new ChangeEventObject(this, "participatingStimulus", 1));
            }
         }
      }

   }

   public void removeParticipatingStimulus(Stimulus arg) {
      if (this.theParticipatingStimulusList != null && arg != null && this.theParticipatingStimulusList.remove(arg)) {
         arg.removeInteractionInstanceSet(this);
         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "participatingStimulus", 2));
         }
      }

   }

   public CollaborationInstanceSet getContext() {
      return this.theContext;
   }

   public void setContext(CollaborationInstanceSet arg) {
      if (this.theContext != arg) {
         CollaborationInstanceSet temp = this.theContext;
         this.theContext = null;
         if (temp != null) {
            temp.removeInteractionInstance(this);
         }

         if (arg != null) {
            this.theContext = arg;
            arg.addInteractionInstance(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "context", 0));
         }
      }

   }

   public Interaction getInteraction() {
      return this.theInteraction;
   }

   public void setInteraction(Interaction arg) {
      if (this.theInteraction != arg) {
         Interaction temp = this.theInteraction;
         this.theInteraction = null;
         if (temp != null) {
            temp.removeInteractionInstanceSet(this);
         }

         if (arg != null) {
            this.theInteraction = arg;
            arg.addInteractionInstanceSet(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "interaction", 0));
         }
      }

   }

   protected void internalRemove() {
      Enumeration tmpParticipatingStimulusEnum = this.getParticipatingStimulusList();
      ArrayList tmpParticipatingStimulusList = new ArrayList();

      while(tmpParticipatingStimulusEnum.hasMoreElements()) {
         tmpParticipatingStimulusList.add(tmpParticipatingStimulusEnum.nextElement());
      }

      Iterator it = tmpParticipatingStimulusList.iterator();

      while(it.hasNext()) {
         Stimulus tmpParticipatingStimulus = (Stimulus)it.next();
         tmpParticipatingStimulus.removeInteractionInstanceSet(this);
      }

      CollaborationInstanceSet tmpContext = this.getContext();
      if (tmpContext != null) {
         tmpContext.removeInteractionInstance(this);
      }

      Interaction tmpInteraction = this.getInteraction();
      if (tmpInteraction != null) {
         tmpInteraction.removeInteractionInstanceSet(this);
      }

      super.internalRemove();
   }
}
