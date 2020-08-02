package ro.ubbcluj.lci.redtd.dtdmetamodel;

public abstract class ChildrenContent implements ElementContent {
   protected int multiplicity = 6;

   public ChildrenContent() {
   }

   public int getMultiplicity() {
      return this.multiplicity;
   }

   public void setMultiplicity(int multiplicity) {
      this.multiplicity = multiplicity;
   }
}
