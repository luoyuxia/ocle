package ro.ubbcluj.lci.gxapi;

import java.awt.Color;
import ro.ubbcluj.lci.gui.diagrams.GProperty;
import ro.ubbcluj.lci.gui.diagrams.ObjectGProperty;

public class GXObjectGProperty extends GXDefaultGProperty {
   protected String fillColor;
   protected int titlefontSize;
   protected int bodyfontSize;

   public GXObjectGProperty() {
   }

   public void setFillColor(String fill_color) {
      this.fillColor = fill_color;
   }

   public String getFillColor() {
      return this.fillColor;
   }

   public void setTitlefontSize(int titlefontSize) {
      this.titlefontSize = titlefontSize;
   }

   public int getTitlefontSize() {
      return this.titlefontSize;
   }

   public void setBodyfontSize(int bodyfontSize) {
      this.bodyfontSize = bodyfontSize;
   }

   public int getBodyfontSize() {
      return this.bodyfontSize;
   }

   public void copy(GProperty gp) {
      super.copy(gp);
      this.fillColor = ((ObjectGProperty)gp).getFillColor().toString();
      this.titlefontSize = ((ObjectGProperty)gp).getTitleFontSize();
      this.bodyfontSize = ((ObjectGProperty)gp).getBodyFontSize();
   }

   public void extractData(GProperty gp) {
      super.extractData(gp);
      ((ObjectGProperty)gp).setFillColor(stringToColor(this.fillColor));
      ((ObjectGProperty)gp).setTitleFontSize(this.titlefontSize);
      ((ObjectGProperty)gp).setBodyFontSize(this.bodyfontSize);
   }

   public static Color stringToColor(String val) {
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
