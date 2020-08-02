package ro.ubbcluj.lci.gxapi;

import com.jgraph.graph.DefaultGraphCell;
import java.util.Hashtable;
import javax.swing.tree.DefaultMutableTreeNode;

public class GXDefaultGraphCell extends GXDefaultMutableTreeNode {
   protected GXHashtableStrings attributes = new GXHashtableStrings();

   public GXDefaultGraphCell() {
   }

   public void setAttributes(GXHashtableStrings attributes) {
      this.attributes = attributes;
   }

   public GXHashtableStrings getAttributes() {
      return this.attributes;
   }

   public void copy(DefaultMutableTreeNode dmtn, Hashtable clones) {
      super.copy(dmtn, clones);
      Hashtable a = (Hashtable)((DefaultGraphCell)dmtn).getAttributes();
      this.attributes.copy(a);
   }

   public void extractData(DefaultMutableTreeNode dmtn, Hashtable clones) {
      super.extractData(dmtn, clones);
      Hashtable ht = new Hashtable();
      this.attributes.extractData(ht);
      ((DefaultGraphCell)dmtn).setAttributes(ht);
   }
}
