package ro.ubbcluj.lci.gxapi;

import java.util.Hashtable;
import javax.swing.tree.DefaultMutableTreeNode;
import ro.ubbcluj.lci.gui.diagrams.ActorCell;
import ro.ubbcluj.lci.gui.diagrams.ActorGProperty;
import ro.ubbcluj.lci.gui.diagrams.GProperty;

public class GXActorCell extends GXDefaultGraphCell {
   protected GXGProperty property;

   public GXActorCell() {
   }

   public void setProperty(GXGProperty property) {
      this.property = property;
   }

   public GXGProperty getProperty() {
      return this.property;
   }

   public void copy(DefaultMutableTreeNode dmtn, Hashtable clones) {
      super.copy(dmtn, clones);
      this.property = new GXActorGProperty();
      this.property.copy((ActorGProperty)((ActorCell)dmtn).getProperty());
   }

   public void extractData(DefaultMutableTreeNode dmtn, Hashtable clones) {
      super.extractData(dmtn, clones);
      GProperty gp = GProperty.createProperty((ActorCell)dmtn);
      this.property.extractData(gp);
      ((ActorCell)dmtn).setProperty(gp);
   }
}
