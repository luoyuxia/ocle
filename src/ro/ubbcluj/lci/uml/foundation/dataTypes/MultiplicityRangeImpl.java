package ro.ubbcluj.lci.uml.foundation.dataTypes;

import java.math.BigInteger;
import ro.ubbcluj.lci.uml.ChangeEventObject;
import ro.ubbcluj.lci.uml.RepositoryChangeAgent;
import ro.ubbcluj.lci.uml.foundation.core.ElementImpl;

public class MultiplicityRangeImpl extends ElementImpl implements MultiplicityRange {
   protected int theLower;
   protected BigInteger theUpper;
   protected Multiplicity theMultiplicity;

   public MultiplicityRangeImpl() {
   }

   public int getLower() {
      return this.theLower;
   }

   public void setLower(int lower) {
      this.theLower = lower;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "lower", 0));
      }

   }

   public BigInteger getUpper() {
      return this.theUpper;
   }

   public void setUpper(BigInteger upper) {
      this.theUpper = upper;
      RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
      if (agent != null && agent.firesEvents()) {
         agent.fireChangeEvent(new ChangeEventObject(this, "upper", 0));
      }

   }

   public Multiplicity getMultiplicity() {
      return this.theMultiplicity;
   }

   public void setMultiplicity(Multiplicity arg) {
      if (this.theMultiplicity != arg) {
         Multiplicity temp = this.theMultiplicity;
         this.theMultiplicity = null;
         if (temp != null) {
            temp.removeRange(this);
         }

         if (arg != null) {
            this.theMultiplicity = arg;
            arg.addRange(this);
         }

         RepositoryChangeAgent agent = RepositoryChangeAgent.getAgent(this);
         if (agent != null && agent.firesEvents()) {
            agent.fireChangeEvent(new ChangeEventObject(this, "multiplicity", 0));
         }
      }

   }

   protected void internalRemove() {
      Multiplicity tmpMultiplicity = this.getMultiplicity();
      if (tmpMultiplicity != null) {
         tmpMultiplicity.removeRange(this);
         if (tmpMultiplicity.getCollectionRangeList().size() < 1) {
            tmpMultiplicity.remove();
         }
      }

      super.internalRemove();
   }
}
