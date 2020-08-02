package ro.ubbcluj.lci.gui.diagrams;

import com.jgraph.graph.DefaultGraphCell;
import java.awt.Point;
import ro.ubbcluj.lci.gui.diagrams.filters.AbstractFilter;
import ro.ubbcluj.lci.gui.diagrams.filters.ClassFilter;
import ro.ubbcluj.lci.uml.foundation.core.Classifier;

public class ClassCell extends DefaultGraphCell {
   private boolean IS_INTERFACE;
   public static int CELL_WIDTH = 130;
   public static int CELL_HEIGHT = 80;
   private boolean is_filtered;
   private boolean hasAutoR;
   private boolean showPackage;
   private GProperty property;
   private AbstractFilter filter;
   private transient Point location;

   public ClassCell() {
      this((Object)null);
   }

   public ClassCell(Object userObject) {
      super(userObject);
      this.IS_INTERFACE = false;
      this.is_filtered = false;
      this.hasAutoR = false;
      this.showPackage = false;
      this.filter = new ClassFilter();
   }

   public boolean getIsInterface() {
      return this.IS_INTERFACE;
   }

   public void setIsInterface(boolean b) {
      this.IS_INTERFACE = b;
   }

   public boolean isFiltered() {
      return this.is_filtered;
   }

   public void setFiltered(boolean state) {
      this.is_filtered = state;
   }

   public void setProperty(GProperty the_prop) {
      this.property = the_prop;
   }

   public GProperty getProperty() {
      return this.property;
   }

   public AbstractFilter getFilter() {
      return this.filter;
   }

   public void setFilter(AbstractFilter af) {
      this.filter = af;
   }

   public Point getLocation() {
      return this.location;
   }

   public void setLocation(Point loc) {
      this.location = loc;
   }

   public boolean hasAutoRelation() {
      return this.hasAutoR;
   }

   public void setAutoRelation(boolean value) {
      this.hasAutoR = value;
   }

   public void setShowPackage(boolean val) {
      this.showPackage = val;
   }

   public boolean getShowPackage() {
      return this.showPackage;
   }

   public String getName() {
      return ((Classifier)this.getUserObject()).getName();
   }
}
