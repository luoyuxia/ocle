package ro.ubbcluj.lci.gui.diagrams;

import com.jgraph.graph.DefaultEdge;

public class AssociationClassEdge extends DefaultEdge {
   public AssociationClassEdge() {
      super((Object)null);
      this.setAllowsChildren(true);
   }

   public Object clone() {
      AssociationClassEdge c = (AssociationClassEdge)super.clone();
      c.source = null;
      c.target = null;
      return c;
   }
}
