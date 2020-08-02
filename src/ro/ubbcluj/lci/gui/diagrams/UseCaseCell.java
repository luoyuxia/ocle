package ro.ubbcluj.lci.gui.diagrams;

import com.jgraph.graph.DefaultGraphCell;
import java.awt.Point;
import ro.ubbcluj.lci.uml.foundation.core.ModelElement;

public class UseCaseCell extends DefaultGraphCell {
   private GProperty property;
   private Point location;
   private boolean hasAutoR;
   static final int CELL_WIDTH = 120;
   static final int CELL_HEIGHT = 70;

   public void setProperty(GProperty prop) {
      this.property = prop;
   }

   public GProperty getProperty() {
      return this.property;
   }

   public UseCaseCell() {
      this((Object)null);
   }

   public UseCaseCell(Object userObject) {
      super(userObject);
      this.hasAutoR = false;
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

   public boolean hasAutoRelation() {
      return this.hasAutoR;
   }

   public void setAutoRelation(boolean value) {
      this.hasAutoR = value;
   }
}
