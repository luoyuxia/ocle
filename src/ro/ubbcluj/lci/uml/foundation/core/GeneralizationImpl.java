package ro.ubbcluj.lci.uml.foundation.core;

import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;

public class GeneralizationImpl extends RelationshipImpl implements Generalization {
   protected String theDiscriminator;
   protected GeneralizableElement theChild;
   protected GeneralizableElement theParent;
   protected Classifier thePowertype;

   public GeneralizationImpl() {
   }

   public String getDiscriminator() {
      return this.theDiscriminator;
   }

   public void setDiscriminator(String discriminator) {
      this.theDiscriminator = discriminator;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "discriminator", 0));
      }

   }

   public GeneralizableElement getChild() {
      return this.theChild;
   }

   public void setChild(GeneralizableElement arg) {
      if (this.theChild != arg) {
         GeneralizableElement temp = this.theChild;
         this.theChild = null;
         if (temp != null) {
            temp.removeGeneralization(this);
         }

         if (arg != null) {
            this.theChild = arg;
            arg.addGeneralization(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "child", 0));
         }
      }

   }

   public GeneralizableElement getParent() {
      return this.theParent;
   }

   public void setParent(GeneralizableElement arg) {
      if (this.theParent != arg) {
         GeneralizableElement temp = this.theParent;
         this.theParent = null;
         if (temp != null) {
            temp.removeSpecialization(this);
         }

         if (arg != null) {
            this.theParent = arg;
            arg.addSpecialization(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "parent", 0));
         }
      }

   }

   public Classifier getPowertype() {
      return this.thePowertype;
   }

   public void setPowertype(Classifier arg) {
      if (this.thePowertype != arg) {
         Classifier temp = this.thePowertype;
         this.thePowertype = null;
         if (temp != null) {
            temp.removePowertypeRange(this);
         }

         if (arg != null) {
            this.thePowertype = arg;
            arg.addPowertypeRange(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "powertype", 0));
         }
      }

   }

   protected void internalRemove() {
      GeneralizableElement tmpChild = this.getChild();
      if (tmpChild != null) {
         tmpChild.removeGeneralization(this);
      }

      GeneralizableElement tmpParent = this.getParent();
      if (tmpParent != null) {
         tmpParent.removeSpecialization(this);
      }

      Classifier tmpPowertype = this.getPowertype();
      if (tmpPowertype != null) {
         tmpPowertype.removePowertypeRange(this);
      }

      super.internalRemove();
   }
}
