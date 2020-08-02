package ro.ubbcluj.lci.gui.diagrams;

import com.jgraph.graph.DefaultGraphCell;

public class ActorCell extends DefaultGraphCell {
   private GProperty property;
   private boolean hasAutoR;
   static final int CELL_WIDTH = 110;
   static final int CELL_HEIGHT = 110;

   public void setProperty(GProperty the_prop) {
      this.property = the_prop;
   }

   public GProperty getProperty() {
      return this.property;
   }

   public ActorCell() {
      this((Object)null);
   }

   public ActorCell(Object userObject) {
      super(userObject);
      this.hasAutoR = false;
   }

   public boolean hasAutoRelation() {
      return this.hasAutoR;
   }

   public void setAutoRelation(boolean value) {
      this.hasAutoR = value;
   }
}
