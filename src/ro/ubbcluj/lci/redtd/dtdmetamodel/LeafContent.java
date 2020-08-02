package ro.ubbcluj.lci.redtd.dtdmetamodel;

import ro.ubbcluj.lci.redtd.DTDElementContentVisitor;

public class LeafContent extends ChildrenContent {
   private String name;

   public LeafContent(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Object accept(DTDElementContentVisitor visitor) {
      return visitor.visitLeafContent(this);
   }
}
