package ro.ubbcluj.lci.gxapi;

import com.jgraph.graph.DefaultEdge;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.Port;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.tree.DefaultMutableTreeNode;

public class GXDefaultPort extends GXDefaultGraphCell {
   protected GXDefaultPort anchor;
   protected HashSet edges = new HashSet();

   public GXDefaultPort() {
   }

   public void setAnchor(GXDefaultPort anchor) {
      this.anchor = anchor;
   }

   public GXDefaultPort getAnchor() {
      return this.anchor;
   }

   public void addEdges(GXDefaultEdge gde) {
      this.edges.add(gde);
   }

   public Enumeration getEdgesList() {
      return Collections.enumeration(this.edges);
   }

   public void copy(DefaultMutableTreeNode dmtn, Hashtable clones) {
      super.copy(dmtn, clones);
      Port dp = ((DefaultPort)dmtn).getAnchor();
      if (dp != null) {
         this.anchor = (GXDefaultPort)clones.get(dp);
      } else {
         this.anchor = null;
      }

   }

   public void extractData(DefaultMutableTreeNode dmtn, Hashtable clones) {
      super.extractData(dmtn, clones);
      if (this.anchor != null) {
         DefaultPort dp = (DefaultPort)clones.get(this.anchor);
         if (dp != null) {
            ((DefaultPort)dmtn).setAnchor(dp);
         }
      }

      Iterator it = this.edges.iterator();

      while(it.hasNext()) {
         DefaultEdge defaultEdge = (DefaultEdge)clones.get(it.next());
         ((DefaultPort)dmtn).add(defaultEdge);
      }

   }
}
