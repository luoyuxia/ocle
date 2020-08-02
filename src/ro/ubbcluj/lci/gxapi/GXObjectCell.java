package ro.ubbcluj.lci.gxapi;

import java.util.Hashtable;
import javax.swing.tree.DefaultMutableTreeNode;
import ro.ubbcluj.lci.gui.diagrams.GProperty;
import ro.ubbcluj.lci.gui.diagrams.ObjectCell;

public class GXObjectCell extends GXDefaultGraphCell {
   protected GXGProperty property;

   public GXObjectCell() {
   }

   public GXObjectCell(ObjectCell oc) {
      this.allowsChildren = oc.getAllowsChildren();
      this.userObject = oc.getUserObject();
   }

   public void setProperty(GXGProperty property) {
      this.property = property;
   }

   public GXGProperty getProperty() {
      return this.property;
   }

   public void copy(DefaultMutableTreeNode dmtn, Hashtable clones) {
      super.copy(dmtn, clones);
      this.property = new GXObjectGProperty();
      this.property.copy(((ObjectCell)dmtn).getProperty());
   }

   public void extractData(DefaultMutableTreeNode dmtn, Hashtable clones) {
      super.extractData(dmtn, clones);
      GProperty gp = GProperty.createProperty((ObjectCell)dmtn);
      this.property.extractData(gp);
      ((ObjectCell)dmtn).setProperty(gp);
   }
}
