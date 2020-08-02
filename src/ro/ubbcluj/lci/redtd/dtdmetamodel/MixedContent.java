package ro.ubbcluj.lci.redtd.dtdmetamodel;

import java.util.ArrayList;
import java.util.Collection;
import ro.ubbcluj.lci.redtd.DTDElementContentVisitor;

public class MixedContent implements ElementContent {
   private ArrayList leafs = new ArrayList();

   public MixedContent() {
   }

   public Collection getLeafs() {
      return this.leafs;
   }

   public void addLeaf(LeafContent leafContent) {
      this.leafs.add(leafContent);
   }

   public Object accept(DTDElementContentVisitor visitor) {
      return visitor.visitMixedContent(this);
   }
}
