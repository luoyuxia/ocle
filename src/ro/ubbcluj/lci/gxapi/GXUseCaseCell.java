package ro.ubbcluj.lci.gxapi;

import java.util.Hashtable;
import javax.swing.tree.DefaultMutableTreeNode;
import ro.ubbcluj.lci.gui.diagrams.GProperty;
import ro.ubbcluj.lci.gui.diagrams.PackageGProperty;
import ro.ubbcluj.lci.gui.diagrams.UseCaseCell;

public class GXUseCaseCell extends GXDefaultGraphCell {
   protected GXGProperty property;

   public GXUseCaseCell() {
   }

   public void setProperty(GXGProperty property) {
      this.property = property;
   }

   public GXGProperty getProperty() {
      return this.property;
   }

   public void copy(DefaultMutableTreeNode dmtn, Hashtable clones) {
      super.copy(dmtn, clones);
      this.property = new GXPackageGProperty();
      this.property.copy((PackageGProperty)((UseCaseCell)dmtn).getProperty());
   }

   public void extractData(DefaultMutableTreeNode dmtn, Hashtable clones) {
      super.extractData(dmtn, clones);
      GProperty gp = GProperty.createProperty((UseCaseCell)dmtn);
      this.property.extractData(gp);
      ((UseCaseCell)dmtn).setProperty(gp);
   }
}
