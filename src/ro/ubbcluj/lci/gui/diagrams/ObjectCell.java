package ro.ubbcluj.lci.gui.diagrams;

import com.jgraph.graph.DefaultGraphCell;
import java.awt.Point;
import ro.ubbcluj.lci.uml.behavioralElements.commonBehavior.Instance;

public class ObjectCell extends DefaultGraphCell {
   public static int CELL_WIDTH = 100;
   public static int CELL_HEIGHT = 80;
   private GProperty property;
   private transient Point location;

   public ObjectCell() {
      this((Object)null);
   }

   public ObjectCell(Object userObject) {
      super(userObject);
   }

   public void setProperty(GProperty the_prop) {
      this.property = the_prop;
   }

   public GProperty getProperty() {
      return this.property;
   }

   public Point getLocation() {
      return this.location;
   }

   public void setLocation(Point loc) {
      this.location = loc;
   }

   public String getName() {
      return ((Instance)this.getUserObject()).getName();
   }
}
