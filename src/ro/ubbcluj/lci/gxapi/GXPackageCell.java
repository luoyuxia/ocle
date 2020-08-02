package ro.ubbcluj.lci.gxapi;

import java.util.Hashtable;
import javax.swing.tree.DefaultMutableTreeNode;
import ro.ubbcluj.lci.gui.diagrams.GProperty;
import ro.ubbcluj.lci.gui.diagrams.PackageCell;
import ro.ubbcluj.lci.gui.diagrams.PackageGProperty;

public class GXPackageCell extends GXDefaultGraphCell {
   protected GXGProperty property;
   protected boolean showPackage;

   public GXPackageCell() {
   }

   public void setProperty(GXGProperty property) {
      this.property = property;
   }

   public GXGProperty getProperty() {
      return this.property;
   }

   public void setShowPackage(boolean showPackage) {
      this.showPackage = showPackage;
   }

   public boolean getShowPackage() {
      return this.showPackage;
   }

   public void copy(DefaultMutableTreeNode dmtn, Hashtable clones) {
      super.copy(dmtn, clones);
      this.property = new GXPackageGProperty();
      this.property.copy((PackageGProperty)((PackageCell)dmtn).getProperty());
      this.showPackage = ((PackageCell)dmtn).getShowPackage();
   }

   public void extractData(DefaultMutableTreeNode dmtn, Hashtable clones) {
      super.extractData(dmtn, clones);
      GProperty gp = GProperty.createProperty((PackageCell)dmtn);
      this.property.extractData(gp);
      ((PackageCell)dmtn).setProperty(gp);
      ((PackageCell)dmtn).setShowPackage(this.showPackage);
   }
}
