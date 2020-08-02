package ro.ubbcluj.lci.gxapi;

import com.jgraph.graph.ConnectionSet;
import com.jgraph.graph.DefaultEdge;
import com.jgraph.graph.ConnectionSet.Connection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

public class GXConnectionSet extends ConnectionSet {
   public GXConnectionSet() {
      this.connections = new HashSet();
      this.edges = new HashSet();
   }

   public void addConnections(GXConnectionSet.GXConnection gc) {
      this.connections.add(gc);
   }

   public Enumeration getConnectionsList() {
      return Collections.enumeration(this.connections);
   }

   public void addEdges(GXDefaultEdge gde) {
      this.edges.add(gde);
   }

   public Enumeration getEdgesList() {
      return Collections.enumeration(this.edges);
   }

   public void copy(ConnectionSet cs, Hashtable clones) {
      this.edges.clear();
      Iterator it = cs.getChangedEdges().iterator();

      while(it.hasNext()) {
         DefaultEdge e = (DefaultEdge)it.next();
         this.edges.add(clones.get(e));
      }

      this.connections.clear();
      it = cs.connections();

      while(it.hasNext()) {
         GXConnectionSet.GXConnection gxc = new GXConnectionSet.GXConnection();
         Connection c = (Connection)it.next();
         gxc.copy(c, clones);
         this.connections.add(gxc);
      }

   }

   public void extractData(ConnectionSet cs, Hashtable clones) {
      Iterator it = this.connections.iterator();

      while(it.hasNext()) {
         GXConnectionSet.GXConnection gxc = (GXConnectionSet.GXConnection)it.next();
         GXDefaultEdge gxde = gxc.getEdge();
         GXDefaultPort gxdp = gxc.getPort();
         cs.connect(clones.get(gxde), clones.get(gxdp), gxc.getIsSource());
      }

   }

   public static class GXConnection {
      private GXDefaultEdge edge;
      private boolean isSource;
      private GXDefaultPort port;

      public GXConnection() {
      }

      public void setEdge(GXDefaultEdge edge) {
         this.edge = edge;
      }

      public GXDefaultEdge getEdge() {
         return this.edge;
      }

      public void setIsSource(boolean isSource) {
         this.isSource = isSource;
      }

      public boolean getIsSource() {
         return this.isSource;
      }

      public void setPort(GXDefaultPort port) {
         this.port = port;
      }

      public GXDefaultPort getPort() {
         return this.port;
      }

      public void copy(Connection c, Hashtable clones) {
         this.isSource = c.isSource();
         this.edge = c.getEdge() != null ? (GXDefaultEdge)clones.get(c.getEdge()) : null;
         this.port = c.getPort() != null ? (GXDefaultPort)clones.get(c.getPort()) : null;
      }
   }
}
