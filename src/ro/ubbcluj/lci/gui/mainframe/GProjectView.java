package ro.ubbcluj.lci.gui.mainframe;

public abstract class GProjectView extends GView {
   public GProjectView() {
   }

   public GAbstractProject getProject() {
      return (GAbstractProject)this.getUserObject();
   }
}
