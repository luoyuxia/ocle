package ro.ubbcluj.lci.gui.mainframe;

public abstract class GUMLModelView extends GView {
   public GUMLModelView() {
   }

   public GUMLModel getModel() {
      return (GUMLModel)super.getUserObject();
   }
}
