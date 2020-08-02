package ro.ubbcluj.lci.gui.diagrams;

import com.jgraph.graph.DefaultGraphCell;
import java.awt.Point;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public class PackageCell extends DefaultGraphCell {
   Point location;
   GProperty property;
   private boolean showPackage;
   static final int CELL_WIDTH = 150;
   static final int CELL_HEIGHT = 100;

   public void setProperty(GProperty prop) {
      this.property = prop;
   }

   public GProperty getProperty() {
      return this.property;
   }

   public PackageCell() {
      this((Object)null);
   }

   public PackageCell(Object userObject) {
      super(userObject);
   }

   public String getName() {
      return ((ModelElement)this.getUserObject()).getName();
   }

   public Point getLocation() {
      return this.location;
   }

   public void setLocation(Point p) {
      this.location = p;
   }

   public void setShowPackage(boolean val) {
      this.showPackage = val;
   }

   public boolean getShowPackage() {
      return this.showPackage;
   }
}
