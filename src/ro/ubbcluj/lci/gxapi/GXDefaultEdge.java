package ro.ubbcluj.lci.gxapi;

import com.jgraph.graph.DefaultEdge;
import com.jgraph.graph.DefaultPort;
import java.util.Hashtable;
import javax.swing.tree.DefaultMutableTreeNode;

public class GXDefaultEdge extends GXDefaultGraphCell {
   protected GXDefaultPort source;
   protected GXDefaultPort target;

   public GXDefaultEdge() {
   }

   public void setSource(GXDefaultPort source) {
      this.source = source;
      this.source.addEdges(this);
   }

   public GXDefaultPort getSource() {
      return this.source;
   }

   public void setTarget(GXDefaultPort target) {
      this.target = target;
      this.target.addEdges(this);
   }

   public GXDefaultPort getTarget() {
      return this.target;
   }

   public void copy(DefaultMutableTreeNode dmtn, Hashtable clones) {
      super.copy(dmtn, clones);
      DefaultPort dmtnSource = (DefaultPort)((DefaultEdge)dmtn).getSource();
      DefaultPort dmtnTarget = (DefaultPort)((DefaultEdge)dmtn).getTarget();
      if (dmtnSource == null) {
         this.source = null;
      } else {
         this.source = (GXDefaultPort)clones.get(dmtnSource);
      }

      if (dmtnTarget == null) {
         this.target = null;
      } else {
         this.target = (GXDefaultPort)clones.get(dmtnTarget);
      }

   }

   public void extractData(DefaultMutableTreeNode dmtn, Hashtable clones) {
      super.extractData(dmtn, clones);
      if (this.source != null) {
         ((DefaultEdge)dmtn).setSource(clones.get(this.source));
      }

      if (this.target != null) {
         ((DefaultEdge)dmtn).setTarget(clones.get(this.target));
      }

   }
}
