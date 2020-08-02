package ro.ubbcluj.lci.redtd.dtdmetamodel;

import java.util.ArrayList;
import java.util.Collection;
import ro.ubbcluj.lci.redtd.DTDElementContentVisitor;

public class SequenceContent extends ChildrenContent {
   private ArrayList children = new ArrayList();

   public SequenceContent() {
   }

   public Collection getChildren() {
      return this.children;
   }

   public void addChild(ChildrenContent childrenContent) {
      this.children.add(childrenContent);
   }

   public Object accept(DTDElementContentVisitor visitor) {
      return visitor.visitSequenceContent(this);
   }
}
