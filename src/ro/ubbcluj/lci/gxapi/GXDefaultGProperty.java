package ro.ubbcluj.lci.gxapi;

import java.awt.Color;
import ro.ubbcluj.lci.gui.diagrams.DefaultGProperty;
import ro.ubbcluj.lci.gui.diagrams.GProperty;

public class GXDefaultGProperty extends GXGProperty {
   protected String outlineColor;
   protected int outlineWidth;
   protected String selectionColor;

   public GXDefaultGProperty() {
   }

   public void setOutlineColor(String outlineColor) {
      this.outlineColor = outlineColor;
   }

   public String getOutlineColor() {
      return this.outlineColor;
   }

   public void setOutlineWidth(int outlineWidth) {
      this.outlineWidth = outlineWidth;
   }

   public int getOutlineWidth() {
      return this.outlineWidth;
   }

   public void setSelectionColor(String selectionColor) {
      this.selectionColor = selectionColor;
   }

   public String getSelectionColor() {
      return this.selectionColor;
   }

   public void copy(GProperty gp) {
      super.copy(gp);
      this.outlineColor = ((DefaultGProperty)gp).getOutlineColor().toString();
      this.outlineWidth = ((DefaultGProperty)gp).getOutlineWidth();
      this.selectionColor = ((DefaultGProperty)gp).getSelectionColor().toString();
   }

   public void extractData(GProperty gp) {
      super.extractData(gp);
      ((DefaultGProperty)gp).setOutlineColor(this.stringToColor(this.outlineColor));
      ((DefaultGProperty)gp).setOutlineWidth(this.outlineWidth);
      ((DefaultGProperty)gp).setSelectionColor(this.stringToColor(this.selectionColor));
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
