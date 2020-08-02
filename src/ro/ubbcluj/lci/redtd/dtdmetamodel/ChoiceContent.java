package ro.ubbcluj.lci.redtd.dtdmetamodel;

import java.util.ArrayList;
import java.util.Collection;
import ro.ubbcluj.lci.redtd.DTDElementContentVisitor;

public class ChoiceContent extends ChildrenContent {
   private ArrayList children = new ArrayList();

   public ChoiceContent() {
   }

   public Collection getChildren() {
      return this.children;
   }

   public void addChild(ChildrenContent childrenContent) {
      this.children.add(childrenContent);
   }

   public Object accept(DTDElementContentVisitor visitor) {
      return visitor.visitChoiceContent(this);
   }
}
