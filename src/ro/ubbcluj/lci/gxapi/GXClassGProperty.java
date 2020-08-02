package ro.ubbcluj.lci.gxapi;

import java.awt.Color;
import ro.ubbcluj.lci.gui.diagrams.ClassGProperty;
import ro.ubbcluj.lci.gui.diagrams.GProperty;

public class GXClassGProperty extends GXDefaultGProperty {
   protected String fillColor;
   protected int titlefontSize;
   protected int bodyfontSize;

   public GXClassGProperty() {
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
      this.fillColor = ((ClassGProperty)gp).getFillColor().toString();
      this.titlefontSize = ((ClassGProperty)gp).getTitleFontSize();
      this.bodyfontSize = ((ClassGProperty)gp).getBodyFontSize();
   }

   public void extractData(GProperty gp) {
      super.extractData(gp);
      ((ClassGProperty)gp).setFillColor(stringToColor(this.fillColor));
      ((ClassGProperty)gp).setTitleFontSize(this.titlefontSize);
      ((ClassGProperty)gp).setBodyFontSize(this.bodyfontSize);
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
