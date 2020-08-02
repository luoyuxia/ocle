package ro.ubbcluj.lci.gxapi;

import java.awt.Color;
import ro.ubbcluj.lci.gui.diagrams.ActorGProperty;
import ro.ubbcluj.lci.gui.diagrams.GProperty;

public class GXActorGProperty extends GXDefaultGProperty {
   protected String fillColor;

   public GXActorGProperty() {
   }

   public void setFillColor(String fill_color) {
      this.fillColor = fill_color;
   }

   public String getFillColor() {
      return this.fillColor;
   }

   public void copy(GProperty gp) {
      super.copy(gp);
      this.fillColor = ((ActorGProperty)gp).getFillColor().toString();
   }

   public void extractData(GProperty gp) {
      super.extractData(gp);
      ((ActorGProperty)gp).setFillColor(this.stringToColor(this.fillColor));
   }

   private Color stringToColor(String val) {

      int index = val.indexOf("r=");
      String temp = val.substring(index + 2, val.indexOf(",", index));
      int r = Integer.parseInt(temp);
      index = val.indexOf("g=");
      temp = val.substring(index + 2, val.indexOf(",", index));
      int g = Integer.parseInt(temp);
      index = val.indexOf("b=");
      temp = val.substring(index + 2, val.indexOf("]", index));
      int b = Integer.parseInt(temp);
      return new Color(r, g, b);
   }
}
