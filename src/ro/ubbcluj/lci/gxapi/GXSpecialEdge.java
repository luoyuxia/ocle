package ro.ubbcluj.lci.gxapi;

import com.jgraph.graph.DefaultGraphCell;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.tree.DefaultMutableTreeNode;
import ro.ubbcluj.lci.gui.diagrams.SpecialEdge;

public class GXSpecialEdge extends GXDefaultEdge {
   protected ArrayList labels = new ArrayList();
   protected boolean isRouted;

   public GXSpecialEdge() {
   }

   public void addLabels(GXDefaultGraphCell label) {
      this.labels.add(label);
   }

   public Enumeration getLabelsList() {
      return Collections.enumeration(this.labels);
   }

   public void setIsRouted(boolean isRouted) {
      this.isRouted = isRouted;
   }

   public boolean getIsRouted() {
      return this.isRouted;
   }

   public void copy(DefaultMutableTreeNode dmtn, Hashtable clones) {
      super.copy(dmtn, clones);
      ArrayList labels = ((SpecialEdge)dmtn).getLabels();
      GXDefaultGraphCell gxdgc;
      if (labels != null) {
         for(Iterator it = labels.iterator(); it.hasNext(); this.labels.add(gxdgc)) {
            DefaultGraphCell dgc = (DefaultGraphCell)it.next();
            gxdgc = (GXDefaultGraphCell)clones.get(dgc);
            if (gxdgc == null) {
               gxdgc = new GXDefaultGraphCell();
               clones.put(dgc, gxdgc);
               gxdgc.copy(dgc, clones);
            }
         }
      }

      this.isRouted = ((SpecialEdge)dmtn).getRouted();
   }

   public void extractData(DefaultMutableTreeNode dmtn, Hashtable clones) {
      super.extractData(dmtn, clones);
      if (this.labels != null) {
         ArrayList l = new ArrayList();

         DefaultGraphCell dgc;
         for(Iterator it = this.labels.iterator(); it.hasNext(); l.add(dgc)) {
            GXDefaultGraphCell gxdgc = (GXDefaultGraphCell)it.next();
            dgc = (DefaultGraphCell)clones.get(gxdgc);
            if (dgc == null) {
               dgc = new DefaultGraphCell();
               clones.put(gxdgc, dgc);
               gxdgc.extractData(dgc, clones);
            }
         }

         ((SpecialEdge)dmtn).setLabels(l);
      }

      ((SpecialEdge)dmtn).setRouted(this.isRouted);
   }
}
